package com.tencent.qcloud.download_tool;

import com.tencent.qcloud.download_tool.util.Utils;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Config {

    public boolean isMultiThreadDownload = false;

    public int maxThreadNums = Utils.getCoreNums();

    public int retryNums = 3;
}
