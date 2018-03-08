package com.tencent.qcloud.download_tool.core;


import com.tencent.qcloud.download_tool.Config;
import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import okhttp3.OkHttpClient;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class DownloadManager {

    private Config config;
    private OkHttpClient okHttpClient;
    private DownloadTask downloadTask;

    private DownloadManager(Builder builder){
        this.config = builder.config;
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        okHttpClient = okhttpClientBuilder.build();
        if(config.isMultiThreadDownload){
            downloadTask = new MultiThreadDownloadTask(okHttpClient);
        }else {
            downloadTask = new SingleThreadDownloadTask(okHttpClient);
        }
    }

    public DownloadResult download(DownloadRequest downloadRequest) throws ClientException, ServerException {
        downloadTask.setDownloadRequest(downloadRequest);
        return downloadTask.syncDownload();
    }

    public void Download(DownloadRequest downloadRequest, OnDownloadListener onDownloadListener){
        downloadTask.setDownloadRequest(downloadRequest);
        downloadTask.setOnDownloadListener(onDownloadListener);
        downloadTask.asyncDownload();
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
}
