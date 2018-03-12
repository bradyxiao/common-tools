package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public final class QLogger {


    private static Recorder recorder = new Recorder();

    private QLogger(){

    }

    public static void setLogFormat(FormatStrategy logFormatStrategy){
        recorder.setLogFormat(logFormatStrategy);
    }

    public static void setTag(String tag){
        recorder.setTag(tag);
    }

    public static void v(String message, Object... args){
        recorder.v(message, args);
    }

    public static void d(String message, Object... args){
        recorder.d(message, args);
    }

    public static void i(String message, Object... args){
        recorder.i(message, args);
    }

    public static void w(String message, Object... args){
        recorder.w(message, args);
    }

    public static void e(String message, Object... args){
        recorder.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args){
        recorder.e(throwable, message, args);
    }

    public static void json(String json){
        recorder.json(json);
    }

    public static void xml(String xml){
        recorder.xml(xml);
    }

}
