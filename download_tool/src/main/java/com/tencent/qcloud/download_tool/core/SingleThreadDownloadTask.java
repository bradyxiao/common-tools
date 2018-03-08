package com.tencent.qcloud.download_tool.core;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
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
        Request request = createRequest(downloadRequest);
        call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return handleResponse(downloadRequest, response, listenerHandler);
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

    @Override
    public void asyncDownload(){
        try {
            Request request = createRequest(downloadRequest);
            call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } catch (ClientException e) {
            listenerHandler.onFailed(downloadRequest, e, null);
        }
    }
}
