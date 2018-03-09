package com.tencent.qcloud.download_tool.util;

import android.os.Build;

import com.tencent.qcloud.download_tool.exception.ClientException;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by bradyxiao on 2018/3/8.
 */

public class Utils {

    private Utils() {
        // Hidden constructor.
    }

    /**
     * get cpu cores
     */
    public static int getCoreNum(){
        if(Build.VERSION.SDK_INT >= 17){
            return Runtime.getRuntime().availableProcessors();
        }else {
            try {
                File cpuDir = new File("/sys/devices/system/cpu/");
                File[] cpuFiles = cpuDir.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if(Pattern.matches("cpu[0-9]+", pathname.getName())){
                            return true;
                        }
                        return false;
                    }
                });
                return cpuFiles.length;
            }catch (Exception e){
                return 1;
            }
        }
    }

    /**
     *  close stream
     * @param closeable
     * @throws ClientException
     */
    public static void close(Closeable closeable) throws ClientException {
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                throw new ClientException(e);
            }
        }
    }
    /**
     *
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     * @return Stack trace in form of String
    */
    public static String getStackTraceString(Throwable throwable){
        if(throwable == null){
            return "";
        }
        Throwable temp = throwable;
        while (temp != null) {
            if (temp instanceof UnknownHostException) {
                return "";
            }
            temp = temp.getCause();
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public static boolean isEmpty(CharSequence charSequence){
        return charSequence == null || charSequence.length() == 0;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param first
     * @param second
     * @return boolean
     */
    public static boolean isEqual(CharSequence first, CharSequence second){
        if(first == second){
            return true;
        }
        if(first != null && second != null){
            int len = first.length();
            if(len == second.length()){
                if(first instanceof String && second instanceof String){
                    return first.equals(second);
                }else {
                    for(int i = 0; i < len; i++){
                        if(first.charAt(i) != second.charAt(i)){
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * in general, toString() return  class"path @ hashcode;
     * for array, rentun [class"path@hashcode,
     * therefor, restructure this method for reading.
     * @param object
     * @return
     */
    public static String toString(Object object){
        if(object == null){
            return "null";
        }
        if(!object.getClass().isArray()){
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }

}
