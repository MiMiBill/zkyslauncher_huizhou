package com.muju.note.launcher.app.home.event;

import com.muju.note.launcher.app.home.bean.PatientResponse;

/**
 * 患者信息改变的事件类
 */
public class PatientInfoEvent {
    public PatientResponse.DataBean info;

    public PatientInfoEvent(PatientResponse.DataBean info) {
        this.info = info;
    }
}
