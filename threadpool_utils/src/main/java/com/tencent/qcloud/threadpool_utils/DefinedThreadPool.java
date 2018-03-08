package com.tencent.qcloud.threadpool_utils;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bradyxiao on 2018/1/8.
 */

public class DefinedThreadPool extends ThreadPoolExecutor {

    private static final String TAG = "DefinedThreadPool";

    public DefinedThreadPool(int corePoolSize, int maximumPoolSize,
                             long keepAliveTime, TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        /**
         * 添加开始执行的策略
         */
        Log.d(TAG, "tid =" + t.getId() + " starting at " + System.currentTimeMillis()/ 1000);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        /**
         * 添加执行之后的策略
         */

        Log.d(TAG, " over at " + System.currentTimeMillis()/ 1000);
        super.afterExecute(r, t);
    }

    @Override
    protected void terminated() {
        /**
         * 添加执行被终止的策略
         */

        super.terminated();
    }
}
