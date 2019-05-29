package com.muju.note.launcher.app.home.event;

/**
 * 患者信息改变的事件类
 */
public class PatientEvent {
    public static final int BIND = 1;//护士绑定入院
    public static final int UN_BIND = 11;//护士选择出院
    public static final int MISSION_ADD = 2;//收到新的宣教
    int type;

    public PatientEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
