package com.tencent.qcloud.download_tool.exception;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class ClientException extends Exception {

    public ClientException(String message){super(message);}

    public ClientException(Throwable throwable){super(throwable);}

    public ClientException(String message, Throwable throwable){super(message, throwable);}
}
