package com.tencent.qcloud.download_tool.core;

import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.listener.OnProgressListener;
import com.tencent.qcloud.download_tool.listener.OnTaskStateListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;
import com.tencent.qcloud.download_tool.util.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public abstract class DownloadTask {

    protected OkHttpClient okHttpClient;
    volatile protected Call call;
    protected DownloadRequest downloadRequest;
    protected ListenerHandler listenerHandler;
    protected int maxRetryNums = 3;
    protected int currentRetryNum = 0;

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

    public void setOnTaskStateListener(OnTaskStateListener onTaskStateListener){
        this.listenerHandler.setOnTaskStateListener(onTaskStateListener);
    }

    public abstract DownloadResult syncDownload() throws ClientException, ServerException;
    public abstract void asyncDownload();

    protected Request createRequest(DownloadRequest downloadRequest) throws ClientException {
        downloadRequest.checkParamters();
        Request request = null;
        Request.Builder requestBuilder = new Request.Builder()
                .url(downloadRequest.getDownloadUrl())
                .get();
        List<String> headers = downloadRequest.getRequestHeader();
        int size = headers.size();
        for(int i = 0; i <= size -2; i += 2){
            requestBuilder.addHeader(headers.get(i), headers.get(i + 1));
        }
        request = requestBuilder.build();
        return request;
    }

    protected DownloadResult handleResponse(DownloadRequest downloadRequest, Response response,
                                            ListenerHandler listenerHandler) throws ServerException, ClientException {
        if(response.code() != 200 && response.code() != 206){
            try {
                String errorMsg = response.body().string();
                throw new ServerException(errorMsg);
            } catch (IOException e) {
                throw new ServerException(e);
            }
        }else{
            long start = downloadRequest.getRange().getStart();
            long hasReceivedLength = start;
            long contentLength = response.body().contentLength();
            InputStream inputStream = response.body().byteStream();
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(downloadRequest.getLocalSavePath(),
                        "rws");
                randomAccessFile.seek(start);
                byte[] buffer = new byte[2 * 1024];
                int len = inputStream.read(buffer, 0, buffer.length);
                while (len != -1){
                    randomAccessFile.write(buffer, 0, len);
                    hasReceivedLength += len;
                    listenerHandler.onProgress(hasReceivedLength, start + contentLength);
                    len = inputStream.read(buffer, 0, buffer.length);
                }
                DownloadResult downloadResult = new DownloadResult();
                return downloadResult;
            } catch (FileNotFoundException e) {
                throw new ClientException(e);
            } catch (IOException e) {
                if(call.isCanceled()){
                    throw new ServerException("Canceled");
                }else {
                    throw new ServerException(e);
                }
            }finally {
                Utils.close(randomAccessFile);
                Utils.close(inputStream);
            }
        }
    }

    public boolean cancel(){
        if(call != null && !call.isCanceled()){
            call.cancel();
            return true;
        }
        return false;
    }

    protected static class ListenerHandler implements OnDownloadListener, OnProgressListener, OnTaskStateListener{

        private OnProgressListener onProgressListener;
        private OnDownloadListener onDownloadListener;
        private OnTaskStateListener onTaskStateListener;

        public ListenerHandler(){
        }

        public void setOnProgressListener(OnProgressListener onProgressListener){
            this.onProgressListener = onProgressListener;
        }

        public void setOnDownloadListener(OnDownloadListener onDownloadListener){
            this.onDownloadListener = onDownloadListener;
        }

        public void setOnTaskStateListener(OnTaskStateListener onTaskStateListener){
            this.onTaskStateListener = onTaskStateListener;
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

        @Override
        public void onWaiting() {
            if(onTaskStateListener != null){
                onTaskStateListener.onWaiting();
            }
        }

        @Override
        public void onRunning() {
            if(onTaskStateListener != null){
                onTaskStateListener.onRunning();
            }
        }

        @Override
        public void onRetry(int count) {
            if(onTaskStateListener != null){
                onTaskStateListener.onRetry(count);
            }
        }

        @Override
        public void onCompleted() {
            if(onTaskStateListener != null){
                onTaskStateListener.onCompleted();
            }
        }
    }

}
