package com.tencent.qcloud.download_tool.core;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class RetryHandler{

    /**
     * @see UnknownHostException { dns resolve error or network is not available}
     * @see SocketException { connect time out}
     * @see SocketTimeoutException {read or write time out}
     * @param currentRetryCount
     * @param maxRetryCount
     * @param e
     * @return (true : need to retry again or false: no)
     */
    public static boolean retryRequest(int currentRetryCount, int maxRetryCount,Exception e){
        boolean isRetry = true;
        if(currentRetryCount > maxRetryCount)return false;
        if( e instanceof SocketTimeoutException || e instanceof SocketException || e instanceof UnknownHostException){
            isRetry = true;
        }
        if(isRetry){
            try {
                Thread.sleep(1500);
            } catch (Exception e1) {
            }
        }
        return isRetry;
    }
}
