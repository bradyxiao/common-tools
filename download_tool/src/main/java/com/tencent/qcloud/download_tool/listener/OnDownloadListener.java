package com.tencent.qcloud.download_tool.listener;

import com.tencent.qcloud.download_tool.exception.ClientException;
import com.tencent.qcloud.download_tool.exception.ServerException;
import com.tencent.qcloud.download_tool.module.DownloadRequest;
import com.tencent.qcloud.download_tool.module.DownloadResult;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public interface OnDownloadListener {
    void onSuccess(DownloadRequest downloadRequest, DownloadResult downloadResult);
    void onFailed(DownloadRequest downloadRequest, ClientException clientException,
                  ServerException serverException);
}
