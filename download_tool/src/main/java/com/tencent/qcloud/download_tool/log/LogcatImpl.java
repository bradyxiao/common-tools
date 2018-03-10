package com.tencent.qcloud.download_tool.log;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public class LogcatImpl extends QLogAdapter {

    @SuppressLint("WrongConstant")
    @Override
    public void log(int level, String tag, String message) {
        setLevel(level);
        setTag(tag);
        Log.println(defaultLevel, defaultTag, message);
    }


}
