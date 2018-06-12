package com.bradyxiao.log_tool;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by bradyxiao on 2018/5/7.
 */

public class LogCatHandler extends Thread{

    private final String[] log = new String[]{"logcat", "-d"};
    private final String[] clear = new String[]{"logcat", "-c"};
    private  BufferedWriter bufferedWriter;
    private  BufferedReader bufferedReader;
    private final String logPathDir = Environment.getExternalStorageDirectory().getPath() + File.separator
            + "QLog";

    @Override
    public void run() {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getPath(), true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (!Thread.interrupted()){
            try {
                Process process = Runtime.getRuntime().exec(log); //抓取当前的缓存日志
                Runtime.getRuntime().exec(clear); //清除日志
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = bufferedReader.readLine();
                while (line != null){
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                    line = bufferedReader.readLine();
                }
                Runtime.getRuntime().exec(clear); //清除日志
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(){
        File file = new File(logPathDir);
        if(!file.exists()){
            file.mkdirs();
        }
        return logPathDir + File.separator + "system.txt";
    }

    public void release(){
        interrupt(); // I/O
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bufferedWriter != null) {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
