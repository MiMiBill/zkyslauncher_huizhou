package com.muju.note.launcher.app.startUp;

public class CheckMsgEvent {

    private String msg;
    private boolean isAdd;
    private boolean isFinish;

    public CheckMsgEvent(String msg, boolean isAdd, boolean isFinish) {
        this.msg = msg;
        this.isAdd = isAdd;
        this.isFinish = isFinish;
    }

    public CheckMsgEvent(String msg, boolean isAdd) {
        this.msg = msg;
        this.isAdd = isAdd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
