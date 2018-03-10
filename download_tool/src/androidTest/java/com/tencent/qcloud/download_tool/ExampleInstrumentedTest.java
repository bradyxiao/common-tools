package com.tencent.qcloud.download_tool;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tencent.qcloud.download_tool.log.LogcatImpl;
import com.tencent.qcloud.download_tool.log.QLogger;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.tencent.qcloud.download_tool.test", appContext.getPackageName());
        QLogger.setLogFormat(null);
        QLogger.i("num =%d; string =%s",2, "test");
    }
}
