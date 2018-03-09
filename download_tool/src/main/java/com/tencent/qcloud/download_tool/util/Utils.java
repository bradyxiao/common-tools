package com.tencent.qcloud.download_tool.util;

import com.tencent.qcloud.download_tool.exception.ClientException;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Utils {

    public static int getCoreNums(){
        return 3;
    }

    public static void close(Closeable closeable) throws ClientException {
        if(closeable != null){
            try {
                closeable.close();
                closeable = null;
            } catch (IOException e) {
                throw new ClientException(e);
            }
        }
    }
}
