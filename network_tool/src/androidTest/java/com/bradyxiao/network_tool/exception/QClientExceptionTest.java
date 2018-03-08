package com.bradyxiao.network_tool.exception;

import android.test.AndroidTestCase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2017/12/22.
 */
public class QClientExceptionTest extends AndroidTestCase {
    QClientException qClientException;
    @Test
    public void testGetCode() throws Exception {
        int code = 100;
        String message = "http error";
        qClientException = new QClientException(100, message);
        assertEquals(code, qClientException.getCode());
        assertEquals(message, qClientException.getMessage());
    }

    @Test
    public void testGetExtendedInfo() throws Exception {
        int code = 100;
        String message = "http error";
        UserError userError = new UserError();
        userError.errorCode = "101";
        userError.errorMessage = "passwd error";
        qClientException = new QClientException(100, message, userError);
        assertEquals(code, qClientException.getCode());
        assertEquals(message, qClientException.getMessage());
        assertEquals(userError.errorCode, ((UserError)qClientException.getExtendedInfo()).errorCode);
        assertEquals(userError.errorMessage, ((UserError)qClientException.getExtendedInfo()).errorMessage);
    }

    class UserError{
        public String errorCode;
        public String errorMessage;
    }

}