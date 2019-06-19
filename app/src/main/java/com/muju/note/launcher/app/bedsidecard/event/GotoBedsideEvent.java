package com.muju.note.launcher.app.bedsidecard.event;

import com.muju.note.launcher.app.home.bean.PatientResponse;

/**
 * 跳转抽奖
 */
public class GotoBedsideEvent {
    public PatientResponse.DataBean info;

    public GotoBedsideEvent(PatientResponse.DataBean info) {
        this.info = info;
    }
}
