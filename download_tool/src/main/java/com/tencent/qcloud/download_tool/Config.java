package com.tencent.qcloud.download_tool;

import com.tencent.qcloud.download_tool.util.Utils;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Config {

    public long connectTimeout = 45000;

    public long socketTimeout = 30000;

    public String hostnameVerifier ="*.myqcloud.com";

    public boolean isMultiThreadDownload = false;

    public int maxThreadNums = Utils.getCoreNum();

    public int retryNums = 3;

    public boolean debug = false;
}
