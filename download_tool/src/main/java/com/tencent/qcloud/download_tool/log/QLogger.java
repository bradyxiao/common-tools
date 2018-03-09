package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public final class QLogger {


    private static Recoder recoder = new LogRecoder();

    private QLogger(){

    }

    public static void initLogImpl(QLog logImpl){
        recoder.setLogImpl(logImpl);
    }

    public static void v(String message, Object... args){
        recoder.v(message, args);
    }

    public static void d(String message, Object... args){
        recoder.d(message, args);
    }

    public static void i(String message, Object... args){
        recoder.i(message, args);
    }

    public static void w(String message, Object... args){
        recoder.w(message, args);
    }

    public static void e(String message, Object... args){
        recoder.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args){
        recoder.e(throwable, message, args);
    }

    public static void json(String json){
        recoder.json(json);
    }

    public static void xml(String xml){
        recoder.xml(xml);
    }

}
