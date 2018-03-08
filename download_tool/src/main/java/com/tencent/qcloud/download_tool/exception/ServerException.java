package com.tencent.qcloud.download_tool.exception;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class ServerException extends Exception {

    public ServerException(String message){super(message);}

    public ServerException(Throwable throwable){super(throwable);}

    public ServerException(String message, Throwable throwable){super(message, throwable);}
}
