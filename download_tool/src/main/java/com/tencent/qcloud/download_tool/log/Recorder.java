package com.tencent.qcloud.download_tool.log;

import com.tencent.qcloud.download_tool.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by bradyxiao on 2018/3/9.
 */

public class Recoder {

    private static final int JSON_INDENT_SPACES = 2;
    private static final String XML_INDENT_SPACES = "2";
    private final ThreadLocal<String> threadLocalTag = new ThreadLocal<>();
    private QLog logImpl;

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int level, Throwable throwable, String msg, Object... args) {
        String tag = getTag();
        String message = null;
        if(msg != null){
            message = (args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        if(throwable != null && message != null){
            message += " : " + Utils.getStackTraceString(throwable);
        }
        if(throwable != null && msg == null){
            message = Utils.getStackTraceString(throwable);
        }
        if(Utils.isEmpty(message)){
            message = "Empty/NULL log message";
        }
        logImpl.log(level, tag, message);
    }

    private String getTag(){
        return null;
    }

    @Override
    public void setLogImpl(QLog logImpl) {
        this.logImpl = logImpl;
    }

    @Override
    public void v(String message, Object... args) {
        log(QLog.VERBOSE, null, message, args);
    }

    @Override
    public void d(String message, Object... args) {
        log(QLog.DEBUG, null, message, args);
    }

    @Override
    public void i(String message, Object... args) {
        log(QLog.INFO, null, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        log(QLog.WARN, null, message, args);
    }

    @Override
    public void e(String message, Object... args) {
        log(QLog.ERROR, null, message, args);
    }

    @Override
    public void e(Throwable throwable, String message, Object... args) {
        log(QLog.ERROR, throwable, message, args);
    }

    @Override
    public void json(String json) {
        if (Utils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT_SPACES);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT_SPACES);
                d(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e) {
            e("Invalid Json");
        }
    }

    @Override
    public void xml(String xml) {
        if (Utils.isEmpty(xml)) {
            d("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", XML_INDENT_SPACES);
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e("Invalid xml");
        }
    }
}
