package com.common.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import com.heqingwei.iflashlight.IFLConfig;

import android.os.Environment;
import android.os.Handler;

public class HttpDownloader {
	
	public static final int UPDATE_PROGRESS = 0;
	public static final int NOSDCARD = 1;
	public static final int DOWNLOAD_FINISHED = 2;
	public static final int APK_ALREADY_EXIST = 3;
	
	private String mApkNameWithoutExt;
	private String mKeepDir;
	private String mDownloadUrl;
	
	private DownloadProgress mDP; // 通过handle把这个数据发送出去
	private Handler mHandler;
	
	public HttpDownloader(Handler handler, String apkNameWithoutExt, String keepDir, String downloadUrl) {
		mHandler = handler;
		
		mApkNameWithoutExt = apkNameWithoutExt;
		mKeepDir = keepDir;
		mDownloadUrl = downloadUrl;
		
		mDP = new DownloadProgress();
	}
	
	public class DownloadProgress {
		public String mApkFileSize;
		public String mTmpFileSize;
		public int mProgressVal;
	}
	
	public void downloadApk(){
		Thread downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	public String getApkFilePath() {
		return makeSavePath() + mApkNameWithoutExt + ".apk";
	}
	
	private String makeSavePath() {
		return  Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + IFLConfig.APP_PREFIX + "/" + mKeepDir + "/";
	}
	
	// TODO 需要进一步抽象复用
	private Runnable mdownApkRunnable = new Runnable() {	
		@Override
		public void run() {
			String tmpFilePath = "";
			String mApkFilePath = "";
			try {
				//String apkName = mApkNameWithoutExt + ".apk";
				String tmpApk  = mApkNameWithoutExt + ".tmp";
				//判断是否挂载了SD卡
				String storageState = Environment.getExternalStorageState();		
				if(storageState.equals(Environment.MEDIA_MOUNTED)){
					String savePath = makeSavePath();
					File file = new File(savePath);
					if(!file.exists()){
						file.mkdirs();
					}
					mApkFilePath = getApkFilePath();//savePath + apkName;
					tmpFilePath = savePath + tmpApk;
				}
				
				//没有挂载SD卡，无法下载文件
				if(mApkFilePath == null || mApkFilePath == ""){
					mHandler.sendEmptyMessage(NOSDCARD);
					return;
				}
				
				File ApkFile = new File(mApkFilePath);
				
				//是否已下载更新文件
				if(ApkFile.exists()){
					mHandler.sendEmptyMessage(APK_ALREADY_EXIST);
					return;
				}
				
				//输出临时下载文件
				File tmpFile = new File(tmpFilePath);
				FileOutputStream fos = new FileOutputStream(tmpFile);
				
				URL url = new URL(mDownloadUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				
				//显示文件大小格式：2个小数点显示
		    	DecimalFormat df = new DecimalFormat("0.00");
		    	//进度条下面显示的总文件大小
		    	mDP.mApkFileSize = df.format((float) length / 1024 / 1024) + "MB";
				
				int count = 0;
				byte buf[] = new byte[1024];
				
				do {
		    		int numread = is.read(buf);
		    		count += numread;
		    		//进度条下面显示的当前下载文件大小
		    		mDP.mTmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
		    		//当前进度值
		    		mDP.mProgressVal =(int)(((float)count / length) * 100);
		    	    //更新进度
		    	    mHandler.obtainMessage(UPDATE_PROGRESS, mDP).sendToTarget();
		    		if(numread <= 0){	
		    			//下载完成 - 将临时下载文件转成APK文件
						if(tmpFile.renameTo(ApkFile)){
							//通知安装
							mHandler.sendEmptyMessage(DOWNLOAD_FINISHED);
						}
		    			break;
		    		}
		    		fos.write(buf,0,numread);
		    	}while(true);
				
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	};
}
