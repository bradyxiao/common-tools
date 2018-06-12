package com.bradyxiao.log_tool.buffer;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/5/9.
 */
@RunWith(AndroidJUnit4.class)
public class BufferUtilsTest {

    @Test
    public void test() throws Exception{
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        Log.d("XIAO", byteBuffer.position() + "|" + byteBuffer.limit() + "|" + byteBuffer.capacity());
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 1);
        Log.d("XIAO", byteBuffer.position() + "|" + byteBuffer.limit() + "|" + byteBuffer.capacity());
        //byteBuffer.put((byte) 1); // java.nio.BufferOverflowException
        byteBuffer.position(0);
        byteBuffer.get();
        byteBuffer.get();
        byteBuffer.get();
        byteBuffer.get();
        byteBuffer.get();
        Log.d("XIAO", byteBuffer.position() + "|" + byteBuffer.limit() + "|" + byteBuffer.capacity());
       // byteBuffer.get(); //java.nio.BufferUnderflowException
        byteBuffer.flip();
        byteBuffer.clear();

    }
}