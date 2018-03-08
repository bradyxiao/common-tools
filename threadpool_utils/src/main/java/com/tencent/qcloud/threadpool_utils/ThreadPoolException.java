package com.tencent.qcloud.threadpool_utils;

/**
 * Created by bradyxiao on 2018/1/8.
 */

public class ThreadPoolException extends Exception {
    public ThreadPoolException(){
        super();
    }

    public ThreadPoolException(String message){
        super(message);
    }

    public ThreadPoolException(String message, Throwable t){
        super(message, t);
    }
}
