package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public abstract class QLog {
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    protected String defaultTag = "QLogger";
    protected int defaultLeve = DEBUG;

    public abstract void log(int level, String tag, String message);

    protected void setLevel(int level){
        switch (level){
            case VERBOSE:
            case INFO:
            case DEBUG:
            case WARN:
            case ERROR:
                defaultLeve = level;
                break;
            default:
                    defaultLeve = DEBUG;
        }
    }

    protected void setTag(String tag){
        if(tag != null)defaultTag = tag;
    }
}
