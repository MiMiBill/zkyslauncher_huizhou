package com.muju.note.launcher.app.video.bean;

import com.muju.note.launcher.okgo.BaseBean;

public class UserBean extends BaseBean {


    /**
     * createTime : 1557286757
     * openid : oEOjE00fioUeGPKPzifyF7xbCsDo
     * avater : http://thirdwx.qlogo.cn/mmopen/vi_32/ZRff7icxW9dXhPv0US9ayib8ibjhFPM1TCUkKfodJbSI3AWZpRaYgjibNngC5Y9HVQQKKQ0ibGJw6WBrYJ0JDvgWQkw/132
     * mobile :
     * nickname : 阳大大呀！
     * updateTime : 0
     * id :
     * status : 1
     */

//    private int createTime;
//    private String openid;
//    private String avater;
//    private String mobile;
//    private String nickname;
//    private int updateTime;
//    private int id;
//    private int status;
//    private String sex;
//    private String address;
//    private int integral;

    private String image;
    private String nickName;
    private int userId;
    private String sex;
    private String mobile;
    private String address;
    private int integral;
    private String access_token;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }



    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

//      if (!TextUtils.isEmpty(UserUtil.getUserBean().getNickname())) {
//        tvName.setText(UserUtil.getUserBean().getNickname());
//    }
//            if (!TextUtils.isEmpty(UserUtil.getUserBean().getSex())) {
//        tvSex.setText(UserUtil.getUserBean().getSex());
//    }
//            if (!TextUtils.isEmpty(UserUtil.getUserBean().getMobile())) {
//        tvPhone.setText(UserUtil.getUserBean().getMobile());
//    }
//            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAddress())) {
//        tvAddress.setText(UserUtil.getUserBean().getAddress());
//    }
//            if (!TextUtils.isEmpty(UserUtil.getUserBean().getAvater())) {
//        Glide.with(getActivity()).load(UserUtil.getUserBean().getAvater()).apply
//                (RequestOptions.bitmapTransform(new CircleCrop())).into(ivHead);
//    }

//    "accountNonExpired": true,
//            "accountNonLocked": true,
//            "credentialsNonExpired": true,
//            "enabled": true,
//            "": "http://thirdwx.qlogo.cn/mmopen/vi_32/HLDjKFe3k6ExFufiaNACYKfcWgWlcABLwIaggv6hBcIKNg9Y3phD3RuNZ6yIw1LXa4c00yohG9XIUNw2EPXE2gQ/132",
//            "": "唐亚峰 | battcn",
//            "parentId": 0,
//            "password": "{bcrypt}$2a$10$hZDKlWfNiSoryt0K36xc7uGxs7Ad0SbYsTLSE8CaTsjsBOlCVdkP2",
//            "permissions": [],
//            "roles": [],
//            "": 121,
//            "userNo": "devUN20191123000005",
//            "userType": 20,
//            "username": "oEOjE0w246neIYfyALIJsbcYYPPE"



}
