package com.common.update;

import org.json.JSONException;

import com.common.http.HttpDownloader;
import com.common.http.HttpHelper;
import com.common.http.IDownloader;
import com.heqingwei.iflashlight.IFLConfig;
import com.heqingwei.iflashlight.IFLSysUtil;
import com.heqingwei.iflashlight.IFLUiHelper;
import com.tencent.qcloud.flashligth.R;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdateManager implements IDownloader {

	private final static String TAG = "UpdateManager";
	
	public static final int MSG_UPDATE_GET_UPDATE_INFO_ERROR = 1;
	public static final int MSG_UPDATE_ALLREADY_LATEAST = 2;
	public static final int MSG_UPDATE_NEED_UPDATE = 3;
	public static final int MSG_DO_UPDATE = 4;
	
	private final static String UPDATE_CONFIG_URL = "http://61.151.217.100/app_update/iflashlight/update.cfg";
	
	private UpdateInfo mUpdateInfo;
	private int mCurVersionCode;
	
	private Context mContext;
	
	
	public  HttpDownloader mHttpDownloader;
	
	public UpdateManager(Context ctx) {
		mContext = ctx;
		mUpdateInfo = null;
		//mHandler = new GMCDownloadHandler(this);
		getCurrentVersion();
	}

	public boolean hasNewVersion() {
		return mUpdateInfo != null;
	}
	
	public UpdateInfo getUpdateInfo() {
		Log.d(TAG, "mUpdateInfo : " + mUpdateInfo);
		return mUpdateInfo;
	}
	
	private static class DownloadHandler extends Handler {};
	private Handler mHandler = new DownloadHandler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what) {
			case HttpDownloader.UPDATE_PROGRESS:
				updateDownloadProgress((HttpDownloader.DownloadProgress)msg.obj);
				break;
			case HttpDownloader.NOSDCARD:
				onSDCardNotFound();
				break;
			case HttpDownloader.DOWNLOAD_FINISHED:
			case HttpDownloader.APK_ALREADY_EXIST:
				installApk(mHttpDownloader.getApkFilePath());
				break;
			
			}
		}};
	/**
	 * 启动更新
	 */
	private static class __Handler extends Handler{};
	public void checkUpdate(final Handler handler) {
		
		final Handler __handler = new __Handler() {
			public void handleMessage(Message msg) {
				if(msg.what != 0 || msg.obj == null) {
					//TODO: 更新失败提示
					Log.d(TAG, "更新失败!!");
					handler.sendEmptyMessage(MSG_UPDATE_GET_UPDATE_INFO_ERROR);
					return;
				}
				UpdateInfo updateInfo = (UpdateInfo) msg.obj;
				if(mCurVersionCode < updateInfo.getVersionCode() ) {
					mUpdateInfo = updateInfo;
					handler.obtainMessage(MSG_UPDATE_NEED_UPDATE,  updateInfo).sendToTarget();
				} else {
					//TODO: 已经是最新版本
					handler.sendEmptyMessage(MSG_UPDATE_ALLREADY_LATEAST);
				}
			}
		};
		
		new Thread() {
			public void run() {
				UpdateInfo updateInfo = queryUpdateInfo();
				__handler.obtainMessage(0, updateInfo).sendToTarget();
			}
		}.start();
	}
	
	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion(){
        try { 
        	PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        	mCurVersionCode = info.versionCode;
        } catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		} 
	}
	
	private UpdateInfo queryUpdateInfo() {
		String jsonStr = HttpHelper.getText(UPDATE_CONFIG_URL);
		if(jsonStr == null || jsonStr.isEmpty()) {
			return null;  // 检查更新失败
		}
		UpdateInfo updateInfo = null;
		try {
			updateInfo = UpdateInfo.parse(jsonStr);
		} catch (JSONException e) {
			// 解析JSON数据包出错了
			Log.e(TAG, "parse json error!");
			e.printStackTrace();
		}
		return updateInfo;
	}

	public void downloadApk(){
		Log.d(TAG, "开始下载...");
		mHttpDownloader = new HttpDownloader(mHandler, IFLConfig.APP_PREFIX + mUpdateInfo.getVersionName(),
				"update", mUpdateInfo.getDownloadUrl());
		
		mHttpDownloader.downloadApk();
	}
 	
 	@Override
 	public void updateDownloadProgress(final HttpDownloader.DownloadProgress dp) {
		//mUpdateButton.setText(dp.mTmpFileSize + "/" + dp.mApkFileSize); // 解除该行注释会导致崩溃
 		//System.out.println(dp.mProgressVal);
 	}
	
 	@Override
 	public void installApk(String apkFilePath) {
 		IFLSysUtil.installApk(mContext, apkFilePath);
 	}
	
	@Override
	public void onSDCardNotFound() {
		IFLUiHelper.showCommMsgBox(mContext, mContext.getResources().getString(R.string.no_sdcard_tip), false);
	};
	
	@Override
	public String getApkFilePath() {
		return mHttpDownloader.getApkFilePath();
	}
 	
}
