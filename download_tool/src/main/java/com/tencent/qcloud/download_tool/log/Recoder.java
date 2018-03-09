package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public interface Recoder {

    void setLogImpl(QLog logImpl);

    void v(String message, Object... args);

    void d(String message, Object... args);

    void i(String message, Object... args);

    void w(String message, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable,String message, Object... args);

    void json(String json);

    void xml(String xml);
}
