package com.tencent.qcloud.download_tool.log;


import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.tencent.qcloud.download_tool.util.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public class LogFileImpl extends QLogAdapter {

    private final LogFileWriteHandler logFileWriteHandler;

    public LogFileImpl(Context context, String flag){
        String logDirName = context != null ? context.getExternalCacheDir().getAbsolutePath() : null;
        HandlerThread handlerThread = new HandlerThread("QLog_File_HandlerThread", Thread.MIN_PRIORITY);
        handlerThread.start();
        logFileWriteHandler = new LogFileWriteHandler(handlerThread.getLooper(), logDirName, flag);
        logFileWriteHandler.sendEmptyMessageDelayed(logFileWriteHandler.MSG_FULL, 1 * 1000);
    }

    @Override
    public void log(int level, String tag, String message) {
        super.setLevel(level);
        super.setTag(tag);
        LogFileMessageStruct logFileMessageStruct = new LogFileMessageStruct(setLevel(level), setTag(tag), message);
        logFileWriteHandler.messageComeIn(logFileMessageStruct);
    }


    private static class LogFileWriteHandler extends Handler {

        //格式：xx/num.flag.log
        private String logDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        private String flag = "qcloud";
        private final int maxSize = 500 * 1024; // 500KB
        private final int maxCount = 50;
        public final int MSG_FULL = 1;
        public final int MSG_IN = 2;
        private final SimpleDateFormat dateFormatForDir = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        private List<LogFileMessageStruct> logFileMessageStructList = new ArrayList<LogFileMessageStruct>();
        private volatile long bufferSize = 0;
        private final int maxBufferSize = 4 * 1024;
        private Object syncObject = new Object();

        private final FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                boolean isExit = fileName.endsWith("." + flag + ".log");
                if(!isExit){
                    return false;
                }
                return getIndexFromFile(pathname) != -1;
            }
        };

        public LogFileWriteHandler(Looper looper, String logDir, String flag){
            super(looper);
            if(!Utils.isEmpty(logDir)){
                this.logDir = logDir;
            }
            if(!Utils.isEmpty(flag)){
                this.flag = flag;
            }
        }

        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case MSG_IN:
                    write();
                    break;
                case MSG_FULL:
                    flush();
                    sendEmptyMessageDelayed(MSG_FULL, 10 * 1000);
                    break;
            }
        }

        private void flush(){
            if(bufferSize <= 0) return;
            writeLogToFile();
        }

        private void write(){
            if(bufferSize > maxBufferSize){
                flush();
            }
        }

        public void messageComeIn(LogFileMessageStruct logFileMessageStruct){
            synchronized (syncObject){
                logFileMessageStructList.add(logFileMessageStruct);
                bufferSize += logFileMessageStruct.getMessageStructLength();
            }
            Message message = this.obtainMessage();
            message.what = MSG_IN;
            sendMessage(message);

        }

        private void writeLogToFile(){
            String dirName = logDir + File.separator + dateFormatForDir.format(new Date(System.currentTimeMillis()));
            File logFile = getLogFile(dirName);
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(logFile, true);
                synchronized (syncObject){
                    for(LogFileMessageStruct logFileMessageStruct : logFileMessageStructList){ //非安全（同步）方法
                        fileWriter.append(logFileMessageStruct.buildMessage());
                    }
                    logFileMessageStructList.clear();
                    bufferSize = 0;
                }
                fileWriter.flush();
            } catch (IOException e) {

            }finally {
                try {
                    Utils.close(fileWriter);
                }catch (Exception e){
                    // 无法写入log 文件
                }
            }
        }

        private File getLogFile(String dirName){
            File folder = new File(dirName);
            if(!folder.exists()){
                folder.mkdirs();
                return new File(dirName, "1." + flag + ".log");
            }

            int fileCount = 0;
            File[] files = folder.listFiles(fileFilter);
            if(files == null || files.length == 0){
                return new File(dirName, "1." + flag + ".log");
            }
            //sort by number
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return getIndexFromFile(o1) - getIndexFromFile(o2);
                }
            });
            File newFile = files[files.length - 1];
            if(newFile.length() > maxSize){
                newFile = new File(dirName, (getIndexFromFile(newFile) + 1) + "."
                        + flag + ".log");
            }
            for(int i = 0; i < files.length - maxCount; i ++){
                files[i].delete();
            }
            return newFile;
        }

        //获取文件分片的索引
        private int getIndexFromFile(File file){
            try{
                String filename = file.getName();
                int point = filename.indexOf('.');
                filename = filename.substring(0,point);
                return Integer.valueOf(filename);
            }catch (Exception e){
                e.printStackTrace();
                return  -1;
            }
        }
    }

    private static class LogFileMessageStruct{
        private final SimpleDateFormat dateFormatForContent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        private int level;
        private String tag;
        private String message;
        private String threadName;
        private long threadId;
        private String time;

        public LogFileMessageStruct(int level, String tag, String message){
            this.level = level;
            this.tag = tag;
            this.message = message;
            this.threadName = Thread.currentThread().getName();
            this.threadId = Thread.currentThread().getId();
            this.time = dateFormatForContent.format(new Date(System.currentTimeMillis()));
        }

        public String buildMessage(){
            String levelMessage = null;
            switch (level){
                case VERBOSE:
                    levelMessage = "V";
                    break;
                case DEBUG:
                    levelMessage = "D";
                    break;
                case INFO:
                    levelMessage = "I";
                    break;
                case WARN:
                    levelMessage = "W";
                    break;
                case ERROR:
                    levelMessage = "E";
                    break;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(levelMessage).append("/")
                    .append(time)
                    .append("[").append(threadName).append(" ").append(threadId)
                    .append("]")
                    .append("[").append(tag).append("]")
                    .append(message)
                    .append("\n");
            return stringBuilder.toString();
        }

        public long getMessageStructLength(){
            return (message != null ? message.length() : 0) + 40;
        }

    }

}
