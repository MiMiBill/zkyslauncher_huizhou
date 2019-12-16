package com.muju.note.launcher.callkey.bean;

public class CallKeyInfo {

    /**
     * true:白天模式  false：夜间模式
     */
    private boolean isDaytimeMode = true;
    private boolean isCallKeyAlreadyDown = false;
    private boolean enableSwitchScreen = false;

    public boolean isEnableSwitchScreen() {
        return enableSwitchScreen;
    }

    public void setEnableSwitchScreen(boolean enableSwitchScreen) {
        this.enableSwitchScreen = enableSwitchScreen;
    }

    private static CallKeyInfo sInstance = new CallKeyInfo();

    public static CallKeyInfo getsInstance()
    {
        return  sInstance;
    }

    public boolean isDaytimeMode() {
        return isDaytimeMode;
    }

    public void setDaytimeMode(boolean daytimeMode) {
        isDaytimeMode = daytimeMode;
    }

    public boolean isCallKeyAlreadyDown() {
        return isCallKeyAlreadyDown;
    }

    public void setCallKeyAlreadyDown(boolean callKeyAlreadyDown) {
        isCallKeyAlreadyDown = callKeyAlreadyDown;
    }
}
