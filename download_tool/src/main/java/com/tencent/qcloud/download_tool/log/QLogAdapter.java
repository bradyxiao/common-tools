package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public abstract class QLogAdapter {
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

    public abstract void log(int level, String tag, String message);

    protected int setLevel(int level){
        int defaultLevel = DEBUG;
        switch (level){
            case VERBOSE:
            case INFO:
            case DEBUG:
            case WARN:
            case ERROR:
                defaultLevel = level;
                break;
            default:
                    defaultLevel = DEBUG;
        }
        return defaultLevel;
    }

    protected String setTag(String tag){
        String defaultTag = "QLogger";
        if(tag != null)defaultTag = tag;
        return defaultTag;
    }
}
