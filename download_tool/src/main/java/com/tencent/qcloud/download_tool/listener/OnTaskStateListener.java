package com.tencent.qcloud.download_tool.listener;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public interface OnTaskStateListener {

    void onWaiting();
    void onRunning();
    void onRetry(int count);
    void onCompleted();
}
