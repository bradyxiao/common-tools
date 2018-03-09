package com.tencent.qcloud.download_tool.core;


import android.util.Log;

import com.tencent.qcloud.download_tool.Config;
import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.listener.OnTaskStateListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class DownloadManager {

    private static final String TAG = DownloadManager.class.getName();

    private Config config;
    private OkHttpClient okHttpClient;
    private DownloadTask downloadTask;
    private static Map<Integer, DownloadTask> downloadTaskMap;
    private DownloadManager(Builder builder){
        this.config = builder.config;
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(config.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(config.socketTimeout, TimeUnit.MILLISECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        boolean isVerified = HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
                        if(!isVerified && hostname.endsWith(config.hostnameVerifier)) {
                            isVerified = true;
                        }
                        return isVerified;
                    }
                });

        okHttpClient = okhttpClientBuilder.build();
        downloadTaskMap = new Hashtable<>();

    }

    public DownloadResult download(DownloadRequest downloadRequest) throws ClientException, ServerException {
        return download(downloadRequest, config.isMultiThreadDownload);
    }

    public DownloadResult download(DownloadRequest downloadRequest, boolean isMultiThreads) throws ClientException, ServerException {
        if(isMultiThreads){
            downloadTask = new MultiThreadDownloadTask(okHttpClient);
        }else {
            downloadTask = new SingleThreadDownloadTask(okHttpClient);
        }
        downloadTask.setDownloadRequest(downloadRequest);
        downloadTask.setOnTaskStateListener(new TaskStateHander(downloadRequest.getTaskId()));
        downloadTaskMap.put(downloadRequest.getTaskId(), downloadTask);
        return downloadTask.syncDownload();
    }

    public void download(DownloadRequest downloadRequest, OnDownloadListener onDownloadListener){
        download(downloadRequest, onDownloadListener, config.isMultiThreadDownload);
    }
    public void download(DownloadRequest downloadRequest, OnDownloadListener onDownloadListener, boolean isMultiThreads){
        if(isMultiThreads){
            downloadTask = new MultiThreadDownloadTask(okHttpClient);
        }else {
            downloadTask = new SingleThreadDownloadTask(okHttpClient);
        }
        downloadTask.setDownloadRequest(downloadRequest);
        downloadTask.setOnDownloadListener(onDownloadListener);
        downloadTask.setOnTaskStateListener(new TaskStateHander(downloadRequest.getTaskId()));
        downloadTaskMap.put(downloadRequest.getTaskId(), downloadTask);
        downloadTask.asyncDownload();
    }

    public void cancel(DownloadRequest downloadRequest){
        if(downloadRequest != null){
            boolean isCancel = false;
           if(downloadTaskMap.containsKey(downloadRequest.getTaskId())){
               isCancel = downloadTaskMap.get(downloadRequest.getTaskId()).cancel();
           }
            Log.d(TAG, "task[" + downloadRequest.getTaskId() + "] canceled [" + isCancel + "]");
        }
    }


    public static class Builder{

        private Config config;

        public Builder setConfig(Config config){
            this.config = config;
            return this;
        }

        public DownloadManager build(){
            DownloadManager downloadManager = new DownloadManager(this);
            return downloadManager;
        }
    }

    private static class TaskStateHander implements OnTaskStateListener{

        private int taskId;
        public TaskStateHander(int taskId){
            this.taskId = taskId;
        }

        @Override
        public void onWaiting() {
            Log.d(TAG, "task[" + taskId + "] on wait" );
        }

        @Override
        public void onRunning() {
            Log.d(TAG, "task[" + taskId + "] on run" );
        }

        @Override
        public void onCompleted() {
            Log.d(TAG, "task[" + taskId + "] on completed" );
            if(downloadTaskMap.containsKey(taskId)){
                //delete
                downloadTaskMap.remove(taskId);
            }
        }
    }
}
