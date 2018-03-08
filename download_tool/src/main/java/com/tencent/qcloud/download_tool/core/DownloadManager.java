package com.tencent.qcloud.download_tool.core;


import com.tencent.qcloud.download_tool.Config;
import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

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
