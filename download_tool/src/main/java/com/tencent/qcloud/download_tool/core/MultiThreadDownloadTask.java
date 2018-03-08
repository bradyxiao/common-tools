package com.tencent.qcloud.download_tool.core;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.module.DownloadResult;

import okhttp3.OkHttpClient;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class MultiThreadDownloadTask extends DownloadTask {

    public MultiThreadDownloadTask(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    public DownloadResult syncDownload() throws ClientException, ServerException {
        return null;
    }

    @Override
    public void asyncDownload() {

    }
}
