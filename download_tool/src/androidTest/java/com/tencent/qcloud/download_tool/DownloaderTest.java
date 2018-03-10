package com.tencent.qcloud.download_tool;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.listener.OnProgressListener;
import com.tencent.qcloud.download_tool.log.QLogger;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/9.
 */
@RunWith(AndroidJUnit4.class)
public class DownloaderTest {

    volatile boolean isOver = false;

    @Test
    public void testDownload() throws Exception{
        Context appContext = InstrumentationRegistry.getContext();
        String url = "http://androidtest-1253653367.cosgz.myqcloud.com/upload_service5.txt";
        String localDir = appContext.getExternalCacheDir().getPath();
        String localFile = "download.txt";
        final DownloadRequest downloadRequest = new DownloadRequest(url, localDir, localFile);
        downloadRequest.setRange(1024 * 0);
        downloadRequest.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(long receivedLength, long totalLength) {
                float progess = (float) (receivedLength * 1.00 / totalLength);
                progess = progess * 100;
                Log.d("DownloadTest",  receivedLength + "/" + totalLength + "-->" + (long)progess
                 + "%");
            }
        });
        final Downloader downloader = new Downloader(new Config());

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(400);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                downloader.cancel(downloadRequest);
//            }
//        }).start();
        DownloadResult downloadResult = downloader.download(downloadRequest);

        File file = new File(downloadRequest.getLocalSavePath());
        Log.d("DownloadTest", "file size =" + file.length());

    }

    @Test
    public void testAsyncDownload() throws Exception{
        Context appContext = InstrumentationRegistry.getContext();
        String url = "http://androidtest-1253653367.cosgz.myqcloud.com/upload_service5.txt";
        String localDir = appContext.getExternalCacheDir().getPath();
        String localFile = "download.txt";
        final DownloadRequest downloadRequest = new DownloadRequest(url, localDir, localFile);
        downloadRequest.setRange(1024 * 10);
        downloadRequest.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(long receivedLength, long totalLength) {
                float progess = (float) (receivedLength * 1.00 / totalLength);
                progess = progess * 100;
                Log.d("DownloadTest",  receivedLength + "/" + totalLength + "-->" + (long)progess
                        + "%");
            }
        });
        final Downloader downloader = new Downloader();

        downloader.download(downloadRequest, new OnDownloadListener() {
            @Override
            public void onSuccess(DownloadRequest downloadRequest, DownloadResult downloadResult) {
                QLogger.xml("<H><a>DownloadTest Download Success</a><b>DownloadTest Download Success2</b></H>");
                File file = null;
                try {
                    file = new File(downloadRequest.getLocalSavePath());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
                Log.d("DownloadTest", "file size =" + file.length());
                isOver = true;
            }

            @Override
            public void onFailed(DownloadRequest downloadRequest, ClientException clientException, ServerException serverException) {
                Exception e = clientException == null ? serverException : clientException;
                Log.d("DownloadTest", clientException == null ? serverException.getMessage() : clientException.getMessage());
                isOver = true;
                e.printStackTrace();
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                downloader.cancel(downloadRequest);
//            }
//        }).start();

        while (!isOver){
            Thread.sleep(500);
        }

    }

}