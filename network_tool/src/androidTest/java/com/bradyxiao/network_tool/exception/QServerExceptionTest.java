package com.bradyxiao.network_tool.exception;

import android.test.AndroidTestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2017/12/22.
 */
public class QServerExceptionTest extends AndroidTestCase {
    QServerException qServerException;
    @Test
    public void testGetCode() throws Exception {
        int code = 100;
        String message = "http error";
        qServerException = new QServerException(100, message);
        assertEquals(code, qServerException.getCode());
        assertEquals(message, qServerException.getMessage());
    }

    @Test
    public void testGetExtendedInfo() throws Exception {
        int code = 100;
        String message = "http error";
        UserError userError = new UserError();
        userError.errorCode = "101";
        userError.errorMessage = "passwd error";
        qServerException = new QServerException(100, message, userError);
        assertEquals(code, qServerException.getCode());
        assertEquals(message, qServerException.getMessage());
        assertEquals(userError.errorCode, ((UserError)qServerException.getExtendedInfo()).errorCode);
        assertEquals(userError.errorMessage, ((UserError)qServerException.getExtendedInfo()).errorMessage);

    }

    class UserError{
        public String errorCode;
        public String errorMessage;
    }

}