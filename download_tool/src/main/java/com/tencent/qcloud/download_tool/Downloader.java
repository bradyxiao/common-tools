package com.tencent.qcloud.download_tool;

import com.tencent.qcloud.download_tool.core.DownloadManager;
import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.listener.OnDownloadListener;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Downloader {

    private DownloadManager downloadManager;

    public Downloader(Config config){

        DownloadManager.Builder builder = new DownloadManager.Builder()
                .setConfig(config);
        downloadManager = builder.build();
    }

    public Downloader(){
        this(new Config());
    }

    public DownloadResult download(DownloadRequest downloadRequest) throws ClientException, ServerException{
        return downloadManager.download(downloadRequest);
    }

    public void download(DownloadRequest downloadRequest, OnDownloadListener onDownloadListener){
        downloadManager.download(downloadRequest, onDownloadListener);
    }

    public void cancel(DownloadRequest downloadRequest){
        downloadManager.cancel(downloadRequest);
    }
}
