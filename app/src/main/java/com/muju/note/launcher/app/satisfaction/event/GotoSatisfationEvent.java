package com.muju.note.launcher.app.satisfaction.event;

/**
 * 跳转抽奖
 */
public class GotoSatisfationEvent {
    public String padsurvey;

    public GotoSatisfationEvent(String padsurvey) {
        this.padsurvey = padsurvey;
    }
}
