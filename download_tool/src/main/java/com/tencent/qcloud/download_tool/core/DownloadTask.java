package com.tencent.qcloud.download_tool.core;

import android.support.annotation.Nullable;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.listener.OnProgressListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public abstract class DownloadTask {

    protected OkHttpClient okHttpClient;
    protected DownloadRequest downloadRequest;
    protected ListenerHandler listenerHandler;

    public DownloadTask(OkHttpClient okHttpClient){
        this.okHttpClient = okHttpClient;
        this.listenerHandler = new ListenerHandler();
    }

    public void setDownloadRequest(DownloadRequest downloadRequest) {
        this.downloadRequest = downloadRequest;
        listenerHandler.setOnProgressListener(downloadRequest.getOnProgressListener());
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        listenerHandler.setOnDownloadListener(onDownloadListener);
    }

    public abstract DownloadResult syncDownload() throws ClientException, ServerException;
    public abstract void asyncDownload();

    protected Request createRequest(DownloadRequest downloadRequest){
        Request request = null;

        return request;
    }

    protected static class ListenerHandler implements OnDownloadListener, OnProgressListener{

        private OnProgressListener onProgressListener;
        private OnDownloadListener onDownloadListener;

        public ListenerHandler(){
        }

        public void setOnProgressListener(OnProgressListener onProgressListener){
            this.onProgressListener = onProgressListener;
        }

        public void setOnDownloadListener(OnDownloadListener onDownloadListener){
            this.onDownloadListener = onDownloadListener;
        }

        @Override
        public void onSuccess(DownloadRequest downloadRequest, DownloadResult downloadResult) {
            if(onDownloadListener != null){
                onDownloadListener.onSuccess(downloadRequest, downloadResult);
            }
        }

        @Override
        public void onFailed(DownloadRequest downloadRequest, ClientException clientException, ServerException serverException) {
            if(onDownloadListener != null){
                onDownloadListener.onFailed(downloadRequest, clientException, serverException);
            }
        }

        @Override
        public void onProgress(long receivedLength, long totalLength) {
            if(onProgressListener != null){
                onProgressListener.onProgress(receivedLength, totalLength);
            }
        }
    }

}
