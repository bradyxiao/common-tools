package com.bradyxiao.network_tool.exception;

import com.bradyxiao.network_tool.common.ErrorCode;

/**
 * Created by bradyxiao on 2017/12/22.
 * <H1> client exception </H1>
 * <P>
 *     主要是本地客户端出现的异常信息，如参数设置有误，使用出错等
 *     <ul>
 *         <li>code</li>
 *         <li>message</li>
 *         <li> extended info </li>
 *     </ul>
 * </P>
 */

public class QClientException extends Exception {

    private int code;

    private Object extendedInfo;

    /**
     *  本地参数校验
     * @param code
     * @param message
     */
    public QClientException(int code, String message){
        super(message);
        this.code = code;
    }

    /**
     * 本地触发的异常信息
     * @param code
     * @param cause
     */
    public QClientException(int code, Throwable cause){
        super(cause);
        this.code = code;
    }

    /**
     * 业务扩展的信息
     * @param code
     * @param message
     * @param extendedInfo
     */
    public QClientException(int code, String message, Object extendedInfo){
        this(code, message);
        this.extendedInfo = extendedInfo;
    }

    public int getCode(){
        return code;
    }

    public Object getExtendedInfo() {
        return extendedInfo;
    }
}
