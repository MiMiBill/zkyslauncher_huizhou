package com.muju.note.launcher.url;

public class UrlUtil {
//        public static final String HOST_DEFAULT = "http://pad.zgzkys.com";
    public static final String HOST_DEFAULT = "http://test.pad.zgzkys.com";
//    public static final String HOST_DEFAULT = "http://192.168.1.200:8086";

    public static String getHost() {
        return HOST_DEFAULT;
    }

    /**
     * 获取平板配置信息
     *
     * @return
     */
    public static String getPadConfigsNew() {
        return getHost() + "/padConfig/getConfig";
    }

    /**
     * 医院风采
     *
     * @return
     */
    public static String getHospitalInfo() {
        return getHost() + "/hospitalMien/list/%s";
    }

    /**
     * 获取视频本地化信息
     *
     * @return
     */
    public static String getVideoDownLoadUrl() {
        return getHost() + "/video/downloadUrl";
    }

    /**
     * 获取每日影视更新列表
     *
     * @return
     */
    public static String getVideoUpdate() {
        return getHost() + "/video/getLately";
    }

    /**
     * 获取影视类型及所有标签
     *
     * @return
     */
    public static String getVideoColumnsTags() {
        return getHost() + "/video/columnTags";
    }

    /**
     * 根据条件查询视频
     *
     * @return
     */
    public static String getSerchVideo() {
        return getHost() + "/video/search";
    }

    //医疗百科获取数据库压缩包
    public static String getDb() {
        return getHost() + "/me/downloadUrl";
    }

    /**
     * 平板激活状态
     *
     * @return
     */
    public static String getQueryPadActiveState() {
        return getHost() + "/pad/newList";
    }


    //心跳
    public static String putHeartbeat() {
        return getHost() + "/padPalpitate/palpitate";
    }

    //检查更新
    public static String getVersionCheckUpdate() {
        return getHost() + "/appVersion/check";
    }

    //获取获取更新的科室和病资料
    public static String getLately() {
        return getHost() + "/me/getLately";
    }

    /**
     * 查询影视支付数据
     *
     * @return
     */
    public static String getGetDeviceStatus() {
        return getHost() + "/device/getDeviceStatus";
    }

    /**
     * 根据集合获取广告列表
     *
     * @return
     */
    public static String getAdvertsByCodes() {
        return getHost() + "/advert/getAdvertsByCodes";
    }

    /**
     * 医院宣教标记已读
     *
     * @return
     */
    public static String updateReadFlag() {
        return getHost() + "/pushTabb/%s/updateReadFlag";
    }

    /**
     * 广告浏览次数增加
     *
     * @return
     */
    public static String getAdvertShowCount() {
        return getHost() + "/advert/updateShowCount";
    }

    /**
     * 上传广告统计数据
     *
     * @return
     */
    public static String getUpCountDb() {
        return getHost() + "/advert/statistics";
    }

    /**
     * 获取七牛云token
     *
     * @return
     */
    public static String getQnToken() {
        return getHost() + "/qn/cloud/getToken?bucketName=%s";
    }

    /**
     * 开机心跳率上传
     *
     * @return
     */
    public static String padLogInsert() {
        return getHost() + "/padLog/insert";
    }

    /**
     * 上传影视统计数据
     *
     * @return
     */
    public static String getUpVideoCountDb() {
        return getHost() + "/video/statistics";
    }

    //获取病人信息
    public static String getGetPaitentInfo() {
        return getHost() + "/hospitalBedTabb/list";
    }

    /**
     * 微信登录二维码链接
     *
     * @return
     */
    public static String getWxLogin() {
        return getHost() + "/login/wxLogin/";
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserInfo() {
        return getHost() + "/login/getUserInfo/";
    }

    //公众号平台页面验证验证码
    public static String verCode() {
        return getHost() + "/advert/verifyCode";
    }

    /**
     * 医院宣教
     *
     * @return
     */
    public static String getHospitalMission() {
        return getHost() + "/hospitalPublicize/list";
    }

    //抽奖
    public static String startLuck() {
        return getHost() + "/lottery/start/%s";
    }

    //抽奖
    public static String checkSignStatus() {
        return getHost() + "/userSing/isSign/%s";
    }

    public static String checkSign() {
        return getHost() + "/userSing/sign";
    }

    //意见反馈
    public static String getFeedbackContent() {
        return getHost() + "/padFeedBack/insert";
    }

    //提交满意度
    public static String getGetCommitSurveyData() {
        return getHost() + "/survey/saveAnswer";
    }

    /**
     * 医院宣教
     *
     * @return
     */
    public static String getGetSurveyData() {
        return getHost() + "/survey/%s/data";
    }


    //获取任务列表
    public static String getTaskList() {
        return getHost() + "/point/record/taskList";
    }

    //做任务
    public static String doTask() {
        return getHost() + "/point/record/doTask";
    }

    //获取奖品列表
    public static String getPointList() {
        return getHost() + "/point/record/pointList";
    }

    //兑奖
    public static String useReward() {
        return getHost() + "/point/record/useReward";
    }


    //屏安柜
    public static String getCabnetOrder() {
        return getHost() + "/cabinet/order?deviceCode=%s";
    }

    //屏开锁
    public static String unLock() {
        return getHost() + "/cabinet/unlock";
    }


    //屏还床
    public static String returnBed() {
        return getHost() + "/cabinet/returnBed";
    }

    //查询锁状态
    public static String findByDId() {
        return getHost() + "/cabinet/findByDid";
    }
}
