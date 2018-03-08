package com.bradyxiao.network_tool.common;

/**
 * Created by bradyxiao on 2017/12/22.
 */

public class HttpMethod {
    public static final String GET ="GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String PATCH ="PATCH";
    public static final String HEAD = "HEAD";
    public static final String OPTIONS = "OPTIONS";
    public static final String DELETE = "DELETE";

    public static boolean isSupportMethod(String httpMethod){
        return GET.equalsIgnoreCase(httpMethod)
                || POST.equalsIgnoreCase(httpMethod)
                || PUT.equalsIgnoreCase(httpMethod)
                || PATCH.equalsIgnoreCase(httpMethod)
                || HEAD.equalsIgnoreCase(httpMethod)
                || OPTIONS.equalsIgnoreCase(httpMethod)
                || DELETE.equalsIgnoreCase(httpMethod);

    }
}
