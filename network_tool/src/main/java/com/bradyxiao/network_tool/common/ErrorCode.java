package com.bradyxiao.network_tool.common;

/**
 * Created by bradyxiao on 2017/12/25.
 */

public enum ErrorCode {

    HTTP_METHOD_INVALID(3000, "http method is invalid"),
    HOST_IS_NULL(3001, "Host is null");

    private int code;
    private String message;
    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
