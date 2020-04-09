package com.muju.note.launcher.url;


public class UrlUtil {

    public static enum Enum_VERSION{
        Production,  //生产环境
        Development, //开发环境
        Test        //测试环境
    }

    public static Enum_VERSION currentVersion = Enum_VERSION.Test;

        public static final String HOST_DEFAULT = "http://pad.zgzkys.com";
//    public static final String HOST_DEFAULT = "http://pad.test.zgzkys.com";
//    public static fina l String HOSTDEFAULT = "http://192.168.1.200:8086";

    //获取公众号任务广告 单独的接口地址
    private static final String HOST_AD = "https://advert-api.battcn.com";//广告正式地址

//    private static final String VIDEO_HOST = "http://test.advert.zgzkys.com";//视频资源测试服务地址
    private static final String VIDEO_HOST = "http://new.cloud.zgzkys.com";//视频资源正式服务地址
    //获取影视资源的host
    private static final String getVideoSourceHost()
    {
        return VIDEO_HOST;
    }

    public static String getHost() {
        return HOST_DEFAULT;
    }


    public static String getADHost()
    {
        return HOST_AD;
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
    public static String getVideoDownLoadUrlNew() {
        return getVideoSourceHost() + "/device/pad_videos/total_quantity";
    }

    /**
     * 获取视频本地化信息
     *
     * @return
     * @deprecated 之前是下载db文件，现在是下载json文件
     */
    @Deprecated
    public static String getVideoDownLoadUrl() {
        return getHost() + "/video/downloadUrl";
    }

    /**
     * 获取每日影视更新列表
     *
     * @return
     */
    @Deprecated
    public static String getVideoUpdate() {
        return getHost() + "/video/getLately";
    }

    /**
     * 获取每日影视更新列表
     *
     * @return
     */
    public static String getVideoUpdateNew(String timestamp) {
        return getVideoSourceHost() + "/device/pad_videos/" + timestamp +"/lately";
    }

    /**
     * 获取增量更新内容的长度
     *
     * @return
     */
    public static String getVideoUpdateCountNew(String timestamp) {
        return getVideoSourceHost() + "/device/pad_videos/" + timestamp +"/count_lately";
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


    //查询套餐
    public static String getComboList() {
        return getHost() + "/hospital/combo/list";
    }

    //创建订单
    public static String orderCreate() {
        return getHost() + "/video/order/create";
    }


    //查询公众号类型
    public static String getWxType() {
        return getHost() + "/advert/getWxType";
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
     * 获取公众号任务列表
     * 新接口2019.11.23日加上 微信登录后每个人的任务不一样
     * @return
     */
    public static String getWeiXinTasks(String hosp_id,String dept_id)
    {
        return getADHost() + "/advert/task_info_receives/" + hosp_id + "/"+ dept_id;
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

    /**
     *  上传模块统计数据
     * @return
     */
    public static String getUpModelCountDb(){
        return getHost()+"/model/statistics/save";
    }

    /**
     *  上传宣教模块统计数据
     * @return
     */
    public static String getUpMissionCountDb(){
        return  getHost()+"/mission/statistics/save";
    }

    //获取病人信息
    public static String getGetPaitentInfo() {
//        return getHost() + "/hospitalBedTabb/list";
        return getHost() + "/hospitalBedTabb/findByBedId";
    }

    /**
     * 微信登录二维码链接
     *
     * @return
     */
//    public static String getWxLogin() {
//        return "http://advert.battcn.com/auth/users/pad/wx_scan/";
//    }
    public static String getWxLogin() {
        return HOST_AD + "/auth/users/pad/wx_scan/";
    }

//    @Deprecated
//    public static String getWxLogin() {
//        return getHost() + "/login/wxLogin/";
//    }



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

    /**
     *  获取首页模块
     * @return
     */
    public static String getHomeMenu(){
        return getHost()+"/menu/config";
    }

    //定时任务列表
    public static String getBedList(){
        return getHost()+"/crontab/bedList";
    }


    //点餐列表
    public static String commodityAll(){
        return getHost()+"/commodity/listAll";
    }

    //提交订单
    public static String orderTableCreate(){
        return getHost()+"/order/table/create";
    }

    //查询订单
    public static String orderTableStatus(){
        return getHost()+"/order/table/status";
    }

    //取消订单
    public static String cancleTable(){
        return getHost()+"/order/table/cancel";
    }

    //查询订单
    public static String tableList(){
        return getHost()+"/order/table/list";
    }

    /**
     *  修改宣教已到达状态
     * @return
     */
    public static String getUpdateArriveFlag(){
        return getHost()+"/pushTabb/updateArriveFlag/";
    }

    /**
     *  修改宣教已读状态
     * @return
     */
    public static String getUpdateReadFlag(){
        return  getHost()+"/pushTabb/updateReadFlag/";
    }


    /**
     *  平板操作日志
     * @return
     */
    public static String recordLog(){
        return  getHost()+"/padOperationLog/recordLog";
    }

    /**
     *  获取所有医院科室的名字列表
     * @return
     */
    public static String getHospitalDepartmentList(int hospitalId){
        return  getHost()+"/dp/publicize/" + hospitalId;
    }






}
