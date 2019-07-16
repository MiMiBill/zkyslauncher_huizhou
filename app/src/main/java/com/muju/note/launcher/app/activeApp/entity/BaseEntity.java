package com.muju.note.launcher.app.activeApp.entity;

import java.io.Serializable;

public class BaseEntity<T> implements Serializable {

    private int code;
    private String msg;
    public T data;

    public BaseEntity() {
    }

    public BaseEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        if (data == null) {
            return (T) new Object();
        }
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success() {
        return code == 0;
    }

}
