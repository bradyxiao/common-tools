package com.tencent.qcloud.download_tool.log;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public class LogFormatStrategy {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 5;

    private final int showMethodCount;
    private final int showMethodOffset;
    private final boolean showThreadInfo;
    private final QLogAdapter logImpl;
    private final String tag;

    private LogFormatStrategy(Builder builder){
        this.showMethodCount = builder.showMethodCount;
        this.showMethodOffset = builder.showMethodOffset;
        this.showThreadInfo = builder.showThreadInfo;
        this.logImpl = builder.logImpl;
        this.tag = builder.tag;
    }

    public void log(int level, String tag, String message){

    }


    private void logHeaderContent(int level, String tag, int showMethodCount){
        // 得到当前线程栈
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        //打印线程信息
        if(showThreadInfo){
            String threadInfo = " Thread [" + Thread.currentThread().getName() + "]";
            logChunk(level, tag, threadInfo);
        }

        //打印方法信息
        int stackOffset = getStackOffset(trace) + showMethodOffset;
        if(showMethodCount + stackOffset > trace.length){
            showMethodCount = trace.length - stackOffset - 1;
        }
        for(int i = showMethodCount; i > 0; i--){
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            logChunk(level, tag, stringBuilder.toString());
        }
    }

    private void logBodyContent(int level, String tag, String message){
        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int len = bytes.length;
        if(len < CHUNK_SIZE){
            String[] lines = message.split(System.getProperty("line.separator"));
            for(String line : lines){
                logChunk(level, tag, line);
            }
            return;
        }
        for(int i = 0; i < len; i += CHUNK_SIZE){
            int count = Math.min(len - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            String[] lines = new String(bytes, i, count).split(System.getProperty("line.separator"));
            for(String line : lines){
                logChunk(level, tag, line);
            }
        }
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    //得到类栈
    private int getStackOffset(StackTraceElement[] stackTraceElements){
        for(int i = MIN_STACK_OFFSET; i < stackTraceElements.length; i ++){
            StackTraceElement stackTraceElement = stackTraceElements[i];
            String name = stackTraceElement.getClassName();
            if(!name.equals(QLogger.class.getName())){
                return --i;
            }
        }
        return -1;
    }

    private void logChunk(int level, String tag, String chunkContent){
        logImpl.log(level, tag, chunkContent);
    }


    public static class Builder{
        int showMethodCount = 2;
        int showMethodOffset = 0;
        boolean showThreadInfo = true;
        QLogAdapter logImpl = new LogcatImpl();
        String tag = "QLogger";

        public Builder(){}

        public Builder setShowMethodCount(int showMethodCount) {
            this.showMethodCount = showMethodCount;
            return this;
        }

        public Builder setShowMethodOffset(int showMethodOffset) {
            this.showMethodOffset = showMethodOffset;
            return this;
        }

        public Builder setShowThreadInfo(boolean showThreadInfo) {
            this.showThreadInfo = showThreadInfo;
            return this;
        }

        public Builder setLogImpl(QLogAdapter logImpl){
            if(logImpl != null){
                this.logImpl = logImpl;
            }
            return this;
        }

        public Builder setTag(String tag){
            if(tag != null){
                this.tag = tag;
            }
            return this;
        }

        public LogFormatStrategy build(){
            return new LogFormatStrategy(this);
        }
    }
}
