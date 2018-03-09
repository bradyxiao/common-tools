package com.tencent.qcloud.download_tool.core;

import android.util.Log;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class SingleThreadDownloadTask extends DownloadTask{

    public SingleThreadDownloadTask(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public DownloadResult syncDownload() throws ClientException, ServerException{
        listenerHandler.onWaiting();
        Request request = createRequest(downloadRequest);
        call = okHttpClient.newCall(request);
        try {
            listenerHandler.onRunning();
            Response response = call.execute();
            DownloadResult downloadResult = handleResponse(downloadRequest, response, listenerHandler);
            listenerHandler.onCompleted();
            return downloadResult;
        } catch (Exception e) {
            boolean isRetry = RetryHandler.retryRequest(currentRetryNum + 1 ,maxRetryNums,e);
            if(isRetry){
                listenerHandler.onRetry(currentRetryNum + 1);
                currentRetryNum = currentRetryNum + 1;
                return syncDownload();
            }
            listenerHandler.onCompleted();
            if(e instanceof ClientException){
                throw (ClientException)e;
            }
            if(e instanceof ServerException){
                throw (ServerException)e;
            }
            throw new ServerException(e);
        }
    }

    @Override
    public void asyncDownload(){
        try {
            listenerHandler.onWaiting();
            Request request = createRequest(downloadRequest);
            call = okHttpClient.newCall(request);
            listenerHandler.onRunning();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call innerCall, IOException e) {
                    listenerHandler.onCompleted();
                    if(call.isCanceled()){
                        listenerHandler.onFailed(downloadRequest, null, new ServerException("Canceled"));
                    }else {
                        boolean isRetry = RetryHandler.retryRequest(currentRetryNum + 1 ,maxRetryNums,e);
                        if(isRetry){
                            listenerHandler.onRetry(currentRetryNum + 1);
                            currentRetryNum = currentRetryNum + 1;
                            asyncDownload();
                        }else {
                            listenerHandler.onFailed(downloadRequest, null, new ServerException(e));
                        }
                    }
                }

                @Override
                public void onResponse(Call innerCall, Response response) throws IOException {
                    try {
                        DownloadResult downloadResult = handleResponse(downloadRequest, response, listenerHandler);
                        listenerHandler.onCompleted();
                        listenerHandler.onSuccess(downloadRequest, downloadResult);
                    }catch (Exception e){
                        boolean isRetry = RetryHandler.retryRequest(currentRetryNum + 1 ,maxRetryNums,e);
                        if(isRetry){
                            listenerHandler.onRetry(currentRetryNum + 1);
                            currentRetryNum = currentRetryNum + 1;
                            asyncDownload();
                        }else {
                            listenerHandler.onCompleted();
                            if(call.isCanceled()){
                                listenerHandler.onFailed(downloadRequest, null, new ServerException("Canceled"));
                            }else if( e instanceof ClientException){
                                listenerHandler.onFailed(downloadRequest, (ClientException) e, null);
                            }else if( e instanceof ServerException){
                                listenerHandler.onFailed(downloadRequest, null, (ServerException) e);
                            }else {
                                listenerHandler.onFailed(downloadRequest, null, new ServerException(e));
                            }
                        }
                    }
                }
            });
        } catch (ClientException e) {
            listenerHandler.onFailed(downloadRequest, e, null);
        }
    }
}
