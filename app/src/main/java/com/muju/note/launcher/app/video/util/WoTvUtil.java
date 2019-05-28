package com.muju.note.launcher.app.video.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.devbrackets.android.component.app.LifecycleObserverManager;
import com.devbrackets.android.component.utils.ProcessUtil;
import com.devbrackets.android.media.BasicVideoView;
import com.muju.note.launcher.app.video.util.wotv.BaseMVPHandler;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.gilde.GlideApp;
import com.muju.note.launcher.util.log.LogUtil;
import com.unicom.common.ModuleSDKConfig;
import com.unicom.common.VideoSdkConfig;
import com.unicom.common.base.LoginType;
import com.unicom.common.base.OnLogoutListener;
import com.unicom.common.base.VideoSDKOpenAPI;
import com.unicom.common.base.video.VideoUIConfig;
import com.unicom.common.base.video.expand.ExpandVideoListener;
import com.unicom.common.base.video.expand.ExpandVideoView;
import com.unicom.common.helper.InitConfig;
import com.unicom.common.network.HttpUtils;
import com.unicom.common.network.OnNetworkConfigCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class WoTvUtil {

    private static final String TAG="WoTvUtil";

    public static WoTvUtil woTvUtil=null;
    public static WoTvUtil getInstance(){
        if(woTvUtil==null){
            woTvUtil=new WoTvUtil();
        }
        return woTvUtil;
    }

    public void initSDK(Application application) {

        /**
         * “沃视频”sdk的相关配置。
         *  需要申请了sdk使用权限的app，方可使用sdk
         *  debug 模式会打印sdk初始化的信息
         *  账号密码不对，会无法使用sdk，账号密码配套了applicationId
         */
        VideoSDKOpenAPI.getInstance()
                .setDebug(true)
                .initGlide4(GlideApp.class) // 除了proguard-rules.pro中的Glide配置，这个GlideApp类需要配置进混淆文件中, 如-keep class {你的APP包名}.GlideApp{*;}
                .initSDK(application,
                        Constants.WOTV_KEY,//向APPserver同事申请账号,已经绑定了当前app的 applicationID(packageName)
                        Constants.WOTV_VALUE,//向APPserver同事申请账号,已经绑定了当前app的 applicationID(packageName)
                        new OnNetworkConfigCallback() {
                            @Override
                            public void onSuccess() {
                                LogUtil.d(TAG,"初始化sdk完成, 可正常调用其他API");
                                if (LauncherApplication.getContext().getPackageName().equals(ModuleSDKConfig.getConfig().getPackageName())) {
                                    LogUtil.d(TAG,"配置文件的包名与此app相同，可放心使用");
                                    login();
                                } else {
                                    LogUtil.e(TAG,"配置文件的包名不一致，请注意检查是否哪里配置错误");
                                }
                            }

                            @Override
                            public void onFail(String message) {
                                LogUtil.e(TAG,"初始化sdk失败,原因：" + message);
                            }
                        });

        if(ProcessUtil.isMainProcess(application)){
            //用于配置不同Module的Lifecycle配置
            String thisModuleName = this.getClass().getPackage().getName();
            LifecycleObserverManager
                    .getInstance()
                    .build(application)
                    .setDefualtObserverTag(thisModuleName)
                    .setBaseViewAgency(thisModuleName, BaseMVPHandler.class)
                    .setHttpAgency(thisModuleName, HttpUtils.class);
            /**
             * app账号退出回调
             */
            VideoSDKOpenAPI.getInstance().setOnLogoutListener(new OnLogoutListener() {
                @Override
                public void onLogout(String message) {
                    LogUtil.d("账号被踢掉：" + message);
                    //支持自动重新登录
                    login();
                }
            }, false);//boolean值决定是否需要关闭当前进程
        }
    }

    /**
     *  登出
     */
    public  void loginOut() {
        VideoSdkConfig.getInstance().getUser().logout();
        VideoSDKOpenAPI.getInstance().logoutSDK();
    }

    /**
     * 登录VIP
     */
    public void login() {
        loginOut();
        loginWo();
    }

    @SuppressLint("WrongConstant")
    private void loginWo() {
        VideoSDKOpenAPI.getInstance().logoutSDK();
        String woPhoneNum = getWoPhoneNum();
        LogUtil.d("TRACE_LOGIN %s", "phone num : " + woPhoneNum);
        if (VideoSdkConfig.getInstance().getUser().getUserPhone().equals(getWoPhoneNum())) {
            //登录沃视频，如果已经登录的手机号跟再次登录的手机号相同就不登陆
            return;
        }

        if (VideoSdkConfig.getInstance().getUser().isLogined()) {
            VideoSDKOpenAPI.getInstance().logoutSDK();
        }
        if (!TextUtils.isEmpty(woPhoneNum)) {
            VideoSDKOpenAPI.getInstance().loginSDK(LoginType.LOGIN_TYPE_UID, woPhoneNum, new InitConfig.OnLoginListener() {
                @Override
                public void onSuccess() {
                    LogUtil.i(TAG,"沃tv登录成功");
                }

                @Override
                public void onFail(String s) {
                    LogUtil.e(TAG,"沃tv登录失败："+s);
                }
            });
        }
    }

    /**
     * 获取沃视频登录手机号
     *
     * @return
     */
    public String getWoPhoneNum() {
        return ActiveUtils.getPhoneNumber();
    }

    /**
     * 静默播放
     *
     * @param videoView       播放View
     * @param cid             内容id
     * @param videoType       内容类型
     * @param url             内容url
     * @param title           内容标题
     * @param controlListener 播放器回调函数
     */
    public void switchContent(ExpandVideoView videoView, String cid, String videoType, String url, String title, ExpandVideoListener controlListener) {
        LogUtil.e("播放", "cid=" + cid + ",type=" + videoType);
        if (!VideoSdkConfig.getInstance().getUser().isLogined()) {//如果到这里还没有登录，就再次登录一下
            login();
        } else {
            switchContent(videoView, true, cid, videoType, url, title, controlListener);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cid", cid);
            jsonObject.put("name", title);
            jsonObject.put("type", videoType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置播放参数，开启播放
     *
     * @param videoView       播放View
     * @param isShowUI        是否要播放器控制菜单
     * @param cid             内容id
     * @param videoType       内容类型
     * @param url             内容url
     * @param title           内容标题
     * @param controlListener 播放器回调函数
     */
    public void switchContent(ExpandVideoView videoView, boolean isShowUI, String cid, String videoType, String url, String title, ExpandVideoListener controlListener) {
//        videoView.setSilence(true);
        setPlayerUi(videoView);
        videoView.setExpandVideoListener(controlListener);
        LogUtil.e("updateVideo播放", "cid=" + cid + ",type=" + videoType);
        if (TextUtils.isEmpty(cid)) {
            videoView.setVideoTitle(title);
            videoView.updateVideoUrl(Uri.parse(url));
        } else {
            videoView.updateVideo(cid, videoType);
        }
    }

    private void setPlayerUi(ExpandVideoView videoView) {
        // Exo的升级版，使用硬解，兼容性好， 较稳定
        videoView.setPlayerType(BasicVideoView.PlayerType.EXO2);
        //是否设置为WIFI下自动播放视频
        videoView.setAutoLink(true);

//        VideoSdkConfig.getInstance().getWotvSdkConfig().setSupportShare(false);
        /**
         * 统一用于配置大小屏的样式，如果不设置播放器的样式，播放器采取与沃视频相同的配置
         *
         * 具体的配置语义，可结合属性来看
         */
        VideoUIConfig videoUIConfig = new VideoUIConfig();
        //切换其他视频时候，是否清空上一帧
        videoUIConfig.setAutoClearSurface(true);
        videoUIConfig.setShowPoster(true);
        //TODO 是否展示控制页面, 默认为true，可不设置
//        videoUIConfig.setUsedDefaultControl(true);
        //当且仅当播放器设置无控制ui才能设置监听
//		videoView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.e(TAG, "点击了屏幕");
//				//TODO 当且仅当，采用无ui页面时候，才可以设置videoview设置点击监听
//				Intent intent = new Intent(CAR_RADIO_START_ACTION);
//				intent.putExtra(BAIDU_CAR_RADIO_INTENT_KEY, BAIDU_CAR_RADIO_INTENT_PLAY);
//				VideoActivity.this.startActivity(intent);
//			}
//		});
        //TODO 是否发送可可家里的指令, 默认为true，可不设置
        videoUIConfig.setInitKeKeJLCommand(false);

        //小屏幕状态的控制
        VideoUIConfig.MinUIStyle minUIStyle = new VideoUIConfig.MinUIStyle();
        //大屏幕状态的控制
        VideoUIConfig.MaxUIStyle maxUIStyle = new VideoUIConfig.MaxUIStyle();


        //-----------------------------以下为举例具体根据实际需要------------------------------------
        //TODO 是否显示屏幕切换的icon 默认是展示的
        minUIStyle.setExpandButtonVisiable(false);
        maxUIStyle.setExpandButtonVisiable(false);

        //TODO 是否显示返回键的icon，默认是展示的
        minUIStyle.setBackButtonVisiable(false);
        maxUIStyle.setBackButtonVisiable(false);

        //TODO 是否具备锁定键的icon，默认是显示的
        minUIStyle.setLockButtonVisiable(true);
        maxUIStyle.setLockButtonVisiable(true);

        //TODO 是否具备清晰度的icon，默认是显示的
        minUIStyle.setDefinitionButtonVisiable(true);
        maxUIStyle.setDefinitionButtonVisiable(true);

//         minUIStyle.setScreenControlVisiable(false);
        minUIStyle.setUnicomLogoVisiable(true);
        maxUIStyle.setCollectionButtonVisiable(false);
        maxUIStyle.setShareButtonVisiable(false);
        maxUIStyle.setMoreScreenButtonVisiable(false);

        videoUIConfig.setMinUIStyle(minUIStyle);
        videoUIConfig.setMaxUIStyle(maxUIStyle);
        videoView.setVideoUIConfig(videoUIConfig);

        //----------------------------!!!针对某些横版APP，只需要全屏的状态!!!-----------------------
//        相当于初始化，直接调用切换大屏,以下配置虽然后执行，但优先级较高
        minUIStyle.setEnable(true);//使得小屏幕的所有配置不生效，包括默认配置，
        maxUIStyle.setEnable(true); //如果此时大屏幕的配置是生效的，将使用大屏幕的配置
        maxUIStyle.setBackFinish(true);
        //----------------------------!!!针对某些横版APP，只需要全屏的状态!!!-----------------------


    }


}
