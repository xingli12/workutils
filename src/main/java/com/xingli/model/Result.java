package com.xingli.model;

import java.util.Date;

/**
 * Created by shuangwang4 on 2018/4/13.
 */
public class Result {
    private int code;
    private String message;

    private Object result;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        Date date = new Date();

        this.timestamp = date.getTime();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
