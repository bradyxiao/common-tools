package com.tencent.qcloud.download_tool.log;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/12.
 */
@RunWith(AndroidJUnit4.class)
public class QLoggerTest {

    @Test
    public void test() throws Exception{
        QLogger.i("Logger test");

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10){
                    QLogger.d("thread ->%s -- > %d"
                    , Thread.currentThread().getName(), i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10){
                    QLogger.d("thread ->%s -- > %d"
                            , Thread.currentThread().getName(), i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t2").start();
    }

    int over = 0;
    int over1 = 0;
    @Test
    public void test2() throws Exception{

        QLogger.i("Logger test");
        QLogger.setLogFormat(new FormatStrategy.Builder()
        .setQLogAdapter(new LogFileImpl(InstrumentationRegistry.getContext(), "COS"))
                .build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10){
                    QLogger.d("thread ->%s -- > %d"
                            , Thread.currentThread().getName(), i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    i++;
                }
                over = 1;
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 10){
                    QLogger.d("thread ->%s -- > %d"
                            , Thread.currentThread().getName(), i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    i++;
                }
                over1 = 1;
            }
        }, "t2").start();

        while (over != 1 && over1 != 1){
            Thread.sleep(1000);
        }
    }

    @Test
    public void test3(){
        QLogger.setTag("V1");
        QLogger.d("TAG %s -> V1test", Thread.currentThread().getName());

        new Thread(new Runnable() {
            @Override
            public void run() {
                QLogger.setTag("V2");
                QLogger.d("TAG %s -> V2test", Thread.currentThread().getName());
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                QLogger.setTag("V3");
                QLogger.d("TAG %s -> V3test", Thread.currentThread().getName());
            }
        }, "t2").start();
    }

}