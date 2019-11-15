package com.muju.note.launcher.callkey.event;

public class CallKeyDownEvent {

    //返回键被按下
    public boolean isTurnOn;
    public String name;

    public CallKeyDownEvent(boolean isTurnOn,String name) {
        this.isTurnOn = isTurnOn;
        this.name = name;
    }
}
