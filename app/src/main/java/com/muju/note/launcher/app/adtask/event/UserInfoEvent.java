package com.muju.note.launcher.app.adtask.event;

import com.muju.note.launcher.app.video.bean.UserBean;

/**
 * 患者信息改变的事件类
 */
public class UserInfoEvent {
    public UserBean userBean;

    public UserInfoEvent(UserBean userBean) {
        this.userBean = userBean;
    }
}
