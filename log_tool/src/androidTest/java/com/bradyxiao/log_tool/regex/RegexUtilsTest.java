package com.bradyxiao.log_tool.regex;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/5/9.
 */
@RunWith(AndroidJUnit4.class)
public class RegexUtilsTest {


    @Test
    public void test() throws Exception{
        //测试 匹配开始一行文本
        String regex = "^this is a test$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("this is a test");
        Log.e("XIAO", String.valueOf(matcher.find()));

        matcher = pattern.matcher("this is a test java");
        Log.e("XIAO", String.valueOf(matcher.find()));


        matcher = pattern.matcher("java this is a test");
        Log.e("XIAO", String.valueOf(matcher.find()));

        regex = "(\\D*)(\\d+)(.*)";
        pattern = Pattern.compile(regex);

        matcher = pattern.matcher("this is a test 56 ?");

        if(matcher.find()){
            int count = matcher.groupCount();
            Log.e("XIAO", String.valueOf(count));
            Log.e("XIAO", matcher.group(0));
            for(int i = 1; i <= count; i++){
                Log.e("XIAO", matcher.group(i));
            }
        }else {
            Log.e("XIAO", String.valueOf(matcher.find()));
        }
    }
}