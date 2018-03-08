package com.tencent.qcloud.download_tool.listener;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public interface OnProgressListener {
    void onProgress(long receivedLength, long totalLength);
}
