package com.muju.note.launcher.util.user;


import com.muju.note.launcher.app.video.bean.UserBean;

public class UserUtil {

    public static UserBean userBean = null;

    public static void setUserBean(UserBean bean){
        userBean=bean;
    }

    public static UserBean getUserBean(){
        return userBean;
    }

}
