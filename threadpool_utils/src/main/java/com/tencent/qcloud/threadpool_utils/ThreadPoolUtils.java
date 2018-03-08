package com.tencent.qcloud.threadpool_utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by bradyxiao on 2018/1/8.
 */

public class ThreadPoolUtils {

    ExecutorService executorService;

    volatile boolean isStop = false;


    public ThreadPoolUtils(EXECUTOR_TYPE executorType, ExtendedParameter extendedParameter){
        isStop = false;
        int coreNums;
        int maxNums;
        switch (executorType){
            case CACHE:
                executorService = Executors.newCachedThreadPool();
                break;
            case FIXED:
                coreNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
                if(extendedParameter != null){
                    coreNums = extendedParameter.threadCoreNum;
                }
                executorService = Executors.newFixedThreadPool(coreNums);
                break;
            case SINGLE:
                executorService = Executors.newSingleThreadExecutor();
                break;
            case SCHEDULE:
                coreNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
                if(extendedParameter != null){
                    coreNums = extendedParameter.threadCoreNum;
                }
                executorService = Executors.newScheduledThreadPool(coreNums);
                break;
            case DEFINED:
            default:
                coreNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
                maxNums = coreNums * 2;
                if(extendedParameter != null){
                    coreNums = extendedParameter.threadCoreNum;
                    maxNums = extendedParameter.threadMaxNum;
                    maxNums = maxNums >= coreNums ? maxNums : coreNums;
                }
                executorService = new DefinedThreadPool(coreNums, maxNums, 60,
                        TimeUnit.SECONDS);
        }
    }

    public void addTask(Runnable runnable) throws ThreadPoolException {
        if(runnable != null && isNormal()){
            executorService.submit(runnable);
        }else {
            throw new ThreadPoolException("executor has been null or stop");
        }
    }

    public <T> Future<T> addTask(Callable<T> callable) throws ThreadPoolException {
        if(callable != null && isNormal()){
            return executorService.submit(callable);
        }else {
            throw new ThreadPoolException("executor has been null or stop");
        }
    }

    public void shutdown(){
        if(isNormal()){
            executorService.shutdown();
        }
    }

    public List<Runnable> shutdownNow() throws ThreadPoolException {
        if(isNormal()){
            return executorService.shutdownNow();
        }else {
            throw new ThreadPoolException("executor has been null or stopped");
        }
    }

    public boolean isShutdown(){
        isStop = true;
        return !isNormal() || executorService.isShutdown();
    }

    public boolean isTerminated(){
        isStop = true;
        return !isNormal() || executorService.isTerminated();
    }

    private boolean isNormal(){
        if(executorService == null || isStop){
            return false;
        }else {
            return true;
        }
    }


    public static enum EXECUTOR_TYPE{
        FIXED,
        SINGLE,
        SCHEDULE,
        CACHE,
        DEFINED
    }

    public static class ExtendedParameter {
        public int threadCoreNum;
        public int threadMaxNum;
    }
}
