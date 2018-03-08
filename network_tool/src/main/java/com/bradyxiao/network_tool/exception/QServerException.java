package com.bradyxiao.network_tool.exception;

/**
 * Created by bradyxiao on 2017/12/22.
 * <H1> Server Exception </H1>
 * <P>
 *     服务端引发本地端的异常信息,可以根据业务扩展
 *    <ul>
 *        <li> code </li>
 *        <li> message </li>
 *        <li> extended info </li>
 *    </ul>
 * </P>
 */

public class QServerException extends Exception {
    private int code;
    private Object extendedInfo;

    public QServerException(int code, String message){
        super(message);
        this.code = code;
    }

    public QServerException(int code, Throwable cause){
        super(cause);
        this.code = code;
    }

    public QServerException(int code, String message, Object extendedInfo){
        this(code, message);
        this.extendedInfo = extendedInfo;
    }

    public int getCode(){
        return code;
    }

    public Object getExtendedInfo(){
        return extendedInfo;
    }
}
