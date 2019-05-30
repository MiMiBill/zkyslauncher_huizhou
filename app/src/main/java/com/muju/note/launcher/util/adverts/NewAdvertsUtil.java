package com.muju.note.launcher.util.adverts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activity.AdVideoViewActivity;
import com.muju.note.launcher.app.activity.LargePicActivity;
import com.muju.note.launcher.app.activity.WebActivity;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.home.bean.AdvertShowBean;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.app.home.db.AdvertsCountDao;
import com.muju.note.launcher.app.home.db.AdvertsInfoDao;
import com.muju.note.launcher.app.video.dialog.OnAdDialogDismissListener;
import com.muju.note.launcher.app.video.dialog.VideoOrImageDialog;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.view.banana.Banner;
import com.muju.note.launcher.view.banana.BannerPage;
import com.muju.note.launcher.view.banana.OnBannerListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NewAdvertsUtil {

    public static NewAdvertsUtil advertsUtil = null;

    public static NewAdvertsUtil getInstance() {
        if (advertsUtil == null) {
            advertsUtil = new NewAdvertsUtil();
        }
        return advertsUtil;
    }


    public static final int TAG_SHOWCOUNT = 0; // 展示次数
    public static final int TAG_CLICKCOUNT = 1; // 点击次数
    public static final int TAG_SHOWTIME = 2; // 展示时间
    public static final int TAG_BROWSETIME = 3; // 浏览时间

    public static final String UP_COUNT_DB = "up_count_db";


    public void queryAdverts(String code, Banner banner, AdvertsDialog dialog) throws Exception {

        queryCodeListAdverts(code, banner, null, dialog);
    }


    public void queryAdverts(String code, final ImageView imageView) throws Exception {
//        queryAdverts(code, null, imageView, null);
    }

    public void queryAdverts(String code, Context context) throws Exception {
        AdvertsDialog dialog = new AdvertsDialog(context, R.style.dialog);
//        queryNewAdverts(code, null, null, dialog);
    }


    //请求广告列表数据
    public void queryCodeListAdverts(final String code, final Banner banner, final ImageView
            imageView, final AdvertsDialog dialog) throws Exception {
        OkGo.<String>post(UrlUtil.getAdvertsByCodes())
                .params("codes", code)
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
//                .params("hospitalId", 142000)
//                .params("deptId", 25800)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheTime(-1)
                .cacheKey(code)
                .tag(code)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogFactory.l().i("queryNewAdverts_onCacheSuccess");
                        SPUtil.putString(Constants.ZKYS_ADVERTS,response.body());
                        try {
                            getNewAdvertsSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
                        LogFactory.l().i("queryNewAdverts_onCacheSuccess");
                        SPUtil.putString(Constants.ZKYS_ADVERTS,response.body());
                        try {
                            getNewAdvertsSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        try {
                           if(failLisinter!=null){
                               failLisinter.fail();
                           }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //展示广告
    private void getNewAdvertsSuccess() {
        if (lisinter != null) {
            lisinter.success();
        }
        if (dialogListener != null) {
            dialogListener.success();
        }
        if(showImgListener!=null){
            showImgListener.success();
        }
    }

    public void showByBanner(final List<AdvertsBean> list, final Banner banner) {
        banner.setVisibility(View.VISIBLE);
        List<BannerPage> pageList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getAdditionUrl()==null){
                list.get(i).setAdditionUrl("");
            }
            BannerPage page = new BannerPage(list.get(i).getResourceUrl(), 10000);
//            BannerPage page = new BannerPage(list.get(i).getResourceUrl(), list.get(i).getInterval()*1000); //轮播时长
            pageList.add(page);
        }
        banner.setSlide(true);
        banner.setDataPlay(pageList, 0);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e("zkpad", "click---position===" + position);
                AdvertsBean bean = list.get(position);
                try {
                    if (bean.getResourceUrl().endsWith("png") || bean.getResourceUrl().endsWith
                            ("jpg")) {
                        jump(bean);
                    }
                } catch (Exception e) {
                    LogFactory.l().i("e=="+e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        final int[] id = {0};
        final long[] startTime={System.currentTimeMillis()};
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
//                LogFactory.l().i("position:"+position);
                try {
                    if(id[0]!=0){
                        if(position==0){
                            return;
                        }
                        if(position==list.size()+1){
                            position=1;
                        }
                        if(id[0]==list.get(position-1).getId()){
                            return;
                        }
                        if(position==1){
                            addData(list.get(list.size()-1).getId(),TAG_SHOWTIME,System.currentTimeMillis()-startTime[0]);
                        }else{
                            addData(list.get(position-2).getId(),TAG_SHOWTIME,System.currentTimeMillis()-startTime[0]);
                        }
                        startTime[0]=System.currentTimeMillis();
                    }
                    if(id[0]!=list.get(position-1).getId()){
                        addData(list.get(position-1).getId(),TAG_SHOWCOUNT);
//                        LogFactory.l().i("TAG_SHOWCOUNT");
                    }
                    id[0] =list.get(position-1).getId();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int status) {
//                LogFactory.l().i("status==="+status);
            }
        });
    }


    //显示默认图片
    public void showDefaultBanner(Banner banner, int type) {  //区分是home还是锁屏
        LogFactory.l().e("showDefaultBanner");
        banner.setVisibility(View.VISIBLE);
        List<BannerPage> pageList = new ArrayList<>();
        if(type==0){
            BannerPage page = new BannerPage("00", 10000);
            BannerPage page1 = new BannerPage("11", 10000);
            pageList.add(page);
            pageList.add(page1);
        }else {
            BannerPage page = new BannerPage("22", 10000);
            BannerPage page1 = new BannerPage("33", 10000);
            pageList.add(page);
            pageList.add(page1);
        }
        banner.setSlide(true);
        banner.setDataPlay(pageList, -100);
    }


    //根据类型跳转不同页面
    private void jump(AdvertsBean bean) throws Exception {
        //linkType  1:url  2:游戏 3:视频  4:通道  5:图片
        updateOnClickCount(bean.getId()); //图片增加点击
        LogFactory.l().i(bean.getLinkType());
        LogFactory.l().i("跳转类型==="+bean.getLinkType());
        if(bean.getLinkType()==1){
            WebActivity.launch(LauncherApplication.getContext(), true, bean
                    .getLinkContent(), bean.getName(), bean.getId());
        }else if (bean.getLinkType()==5){
            String url=bean.getLinkContent();
            LargePicActivity.launch(LauncherApplication.getContext(),url,bean.getId());
        }else if(bean.getLinkType()==3){
            AdVideoViewActivity.launch(LauncherApplication.getContext(),bean.getLinkContent(),bean.getId());
        }else if(bean.getLinkType()==2){

        }else if(bean.getLinkType()==4){

        }
    }


    //展示图片
    public void showByImageView(final List<AdvertsBean> list, ImageView imageView) {
        final AdvertsBean bean = list.get(0);
        Glide.with(LauncherApplication.getContext()).load(bean.getResourceUrl()).into(imageView);
        addData(bean.getId(), TAG_SHOWCOUNT);
        LogFactory.l().i("TAG_SHOWCOUNT");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jump(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //图片击进入长图
    public void showByImageView(final Context context, final List<AdvertsBean> list, final ImageView imageView,
                                final ImageView img, final RelativeLayout relCorner){
        final AdvertsBean bean = list.get(0);
        Glide.with(LauncherApplication.getContext()).load(bean.getResourceUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                imageView.setImageDrawable(resource);
                if(showImgLoadListener!=null){
                    showImgLoadListener.success();
                }
                relCorner.setVisibility(View.VISIBLE);
            }
        });

        addData(bean.getId(), TAG_SHOWCOUNT);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jump(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        final long time = System.currentTimeMillis();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relCorner.setVisibility(View.GONE);
                addData(bean.getId(), TAG_SHOWTIME, System.currentTimeMillis() - time);
            }
        });
    }


    //dialog形式展示
    public void showByDialog(final List<AdvertsBean> list, final AdvertsDialog dialog) {
        final AdvertsBean bean = list.get(0);
        dialog.setOnImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jump(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        dialog.loadImg(bean.getResourceUrl(),bean.getCloseType(),bean.getSecond());
        addData(bean.getId(), TAG_SHOWCOUNT);
        dialog.setOnAdDialogDismissListener(new OnAdDialogDismissListener() {
            @Override
            public void OnAdDialogDismiss() {
                dialog.dismiss();
            }
        });
        final long time = System.currentTimeMillis();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(bean.getCloseType()==2){
                    dialog.removeHandler();
                }
                addData(bean.getId(), TAG_SHOWTIME, System.currentTimeMillis() - time);
            }
        });
    }


    public void showVideoDialog(final List<AdvertsBean> list, final VideoOrImageDialog dialog) throws
            Exception {
        final AdvertsBean bean = list.get(0);
        dialog.setOnImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jump(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        if (bean.getResourceUrl().endsWith("mp4")) {
            dialog.startVideo(bean.getResourceUrl());
        } else {
            dialog.loadImg(bean.getResourceUrl(),bean.getCloseType(),bean.getSecond());
        }
        dialog.setOnAdDialogDismissListener(new OnAdDialogDismissListener() {
            @Override
            public void OnAdDialogDismiss() {
                dialog.dismiss();
            }
        });
        addData(bean.getId(), TAG_SHOWCOUNT);
        final long time = System.currentTimeMillis();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                addData(bean.getId(), TAG_SHOWTIME, System.currentTimeMillis() - time);
                if(bean.getCloseType()==2){
                    dialog.removeHandler();
                }
            }
        });
    }


    private OnBannerSuccessLisinter lisinter;
    private OnBannerFailLisinter failLisinter;
    private OnDialogSuccessLisinter dialogListener;
    private ShowImageSuccessLisinter showImgListener;
    private ShowImageLoadLisinter showImgLoadListener;

    public void setOnImageLoadSuccessLisinter(ShowImageLoadLisinter lisinter) {
        this.showImgLoadListener = lisinter;
    }

    public interface ShowImageLoadLisinter {
        public void success();
    }

    public void setOnBannerSuccessLisinter(OnBannerSuccessLisinter lisinter) {
        this.lisinter = lisinter;
    }

    public interface OnBannerSuccessLisinter {
        public void success();
    }

    public void setOnBannerFailLisinter(OnBannerFailLisinter lisinter) {
        this.failLisinter = lisinter;
    }

    public interface OnBannerFailLisinter {
        public void fail();
    }


    public void setOnDialogSuccessLisinter(OnDialogSuccessLisinter dialogListener) {
        this.dialogListener = dialogListener;
    }

    public interface OnDialogSuccessLisinter {
        public void success();
    }

    public void showImageSuccessLisinter(ShowImageSuccessLisinter showImgListener) {
        this.showImgListener = showImgListener;
    }

    public interface ShowImageSuccessLisinter {
        public void success();
    }

    /**
     * 增加广告点击
     *
     * @param advertId
     */
    public void updateOnClickCount(int advertId) throws Exception {
//        OkGo.<String>post(Constant.getAdvertOnClickCount())
//                .params("advertId", advertId)
//                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
//                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
//                .tag(this)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//
//                    }
//                });
        addData(advertId, TAG_CLICKCOUNT);
    }

    /**
     * 检查是否需要上传浏览次数
     *
     * @param code
     */
    private void checkShowDate(String code) throws Exception {
//        String advertShowStr = SPUtil.getString("ADVERTSHOWCOUNTS");
//        List<AdvertShowBean> list = new ArrayList<>();
//        Calendar c = Calendar.getInstance();
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        boolean isadd = true;
//        list = new Gson().fromJson(advertShowStr, new TypeToken<List<AdvertShowBean>>() {
//        }.getType());
//        if (list == null || list.size() <= 0) {
//            list = new ArrayList<>();
//            LogUtil.i("本地保存的浏览信息为空");
//            AdvertShowBean advertShowBean = new AdvertShowBean(day, code);
//            list.add(advertShowBean);
//            updateShowAdvert(code, list);
//            return;
//        }
//        for (AdvertShowBean bean : list) {
//            if (bean.getCode().equals(code)) {
//                // 如果当天日期大于保存的日期，则增加浏览次数，并保存日期
//                LogUtil.i("当前日期：" + day);
//                LogUtil.i("保存的日期：" + bean.getDay());
//                isadd = false;
//                if (day != bean.getDay()) {
//                    updateShowAdvert(code, list);
//                    bean.setDay(day);
//                    bean.setCode(code);
//                    return;
//                }
//            }
//        }
//        if (isadd) {
//            LogUtil.i("新增code：" + code + "    新增day：" + day);
//            AdvertShowBean advertShowBean = new AdvertShowBean(day, code);
//            list.add(advertShowBean);
//            updateShowAdvert(code, list);
//        }
    }

    /**
     * 增加广告浏览次数
     *
     * @param code
     */
    private void updateShowAdvert(String code, final List<AdvertShowBean> list) throws Exception {
        OkGo.<String>post(UrlUtil.getAdvertShowCount())
                .params("positionCode", code)
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = new Gson().toJson(list);
                        SPUtil.putString("ADVERTSHOWCOUNTS", s);
                    }
                });
    }

    public void addData(int advertId, int tag) {
        addData(advertId, tag, 0);
    }

    private AdvertsCountDao getAdvertsCountByDb(int advertId,String date){
        LitePalDb.setZkysDb();
        return LitePal.where("advertId=? and date=?", advertId + "", date)
                .findFirst(AdvertsCountDao.class);
    }

    private void saveAdvertsCountByDb(AdvertsCountDao dao){
        LitePalDb.setZkysDb();
        dao.save();
    }

    /**
     * 处理数据库操作
     *
     * @param advertId
     * @param tag
     */
    public void addData(int advertId, int tag, long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(System.currentTimeMillis());
        String date = format.format(d);
        AdvertsCountDao dao = getAdvertsCountByDb(advertId,date);
        AdvertsCountDao countDao = null;
        if (dao == null) {
            countDao = new AdvertsCountDao();
            countDao.setAdvertId(advertId);
            countDao.setDate(date);
            countDao.setClickCount(0);
            countDao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
            countDao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
            countDao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        }
        switch (tag) {
            case TAG_SHOWCOUNT: // 展示次数
                if (dao == null) {
                    countDao.setShowCount(1);
                } else {
                    dao.setShowCount(dao.getShowCount() + 1);
                }
                break;
            case TAG_CLICKCOUNT: // 点击次数
                if (dao == null) {
                    countDao.setClickCount(1);
                } else {
                    dao.setClickCount(dao.getClickCount() + 1);
                }
                break;

            case TAG_SHOWTIME: // dialog展示时长
                if (dao == null) {
                    countDao.setShowTime(time);
                } else {
                    dao.setShowTime(dao.getShowTime() + time);
                }
                break;

            case TAG_BROWSETIME: // 浏览时间
                if (dao == null) {
                    countDao.setBrowseTime(time);
                } else {
                    dao.setBrowseTime(dao.getBrowseTime() + time);
                }
                break;
        }
        if (dao == null) {
            saveAdvertsCountByDb(countDao);
        } else {
            saveAdvertsCountByDb(dao);
        }
    }

    private void saveAdvertsInfoByDb(AdvertsInfoDao dao){
        LitePalDb.setZkysDb();
        dao.save();
    }


    /**
     *  添加广告详情数据
     * @param advertId
     * @param tag
     * @param startTime
     * @param endTime
     *
     */
    public void addDataInfo(int advertId,int tag,long startTime, long endTime){
        String date = getStringDate();
        AdvertsInfoDao dao=new AdvertsInfoDao();
        dao.setAdvertId(advertId);
        dao.setDate(date);
        dao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
        dao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
        dao.setType(tag);
        dao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        switch (tag){
            case TAG_SHOWCOUNT:
            case TAG_CLICKCOUNT:
                dao.setStartTime(System.currentTimeMillis());
                dao.setEndTime(System.currentTimeMillis());
                dao.setTime(0);
                break;

            case TAG_BROWSETIME:
            case TAG_SHOWTIME:
                dao.setStartTime(startTime);
                dao.setEndTime(endTime);
                dao.setTime((endTime-startTime));
                break;
        }
        saveAdvertsInfoByDb(dao);
    }

    private String getStringDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date d = new Date(System.currentTimeMillis());
        return format.format(d);
    }

    /**
     * 上传广告统计信息
     */
    public void checkCountDb() {
        int advertDay = SPUtil.getInt("UP_COUNT_DB");
        Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        if (day != advertDay) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 22 || hour <= 7) {
                // 上传统计信息
                if (isUpCountDb) {
                    return;
                }
                isUpCountDb = true;
                int num = new Random().nextInt(59) + 1;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        upCountDb(day);
                    }
                }, 1000 * 60 * num);
            }
        }
    }

    public static boolean isUpCountDb = false;

    public void upCountDb(final int day) {
        List<AdvertsCountDao> daoList = LitePal.findAll(AdvertsCountDao.class);
        OkGo.<BaseBean<Void>>post(UrlUtil.getUpCountDb())
                .params("data", new Gson().toJson(daoList))
                .execute(new JsonCallback<BaseBean<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<Void>> response) {
//                        LitePal.deleteAll(AdvertsCountDao.class);
                        LogFactory.l().i("response==="+response.body());
                        SPUtil.putInt(UP_COUNT_DB, day);
//                        getToken(Constant.ZKYS_PAD_DB);
                    }
                });
    }


    //获取七牛云服务器token
    public void getToken(String bucketName) {
//        Map<String, String> params = new HashMap();
//        params.put("bucketName", bucketName);
        OkGo.<String>get(String.format(UrlUtil.getQnToken(), bucketName))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogFactory.l().e(response.body());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                String token=jsonObject.optString("token");
                                boolean isMount=Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
                                if(isMount){
                                    String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"zkysdb/zkys-data.db";
//                                    String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"zkys/launcher24.apk";
                                    File file=new File(path);
                                    if(file.exists()){
                                        LogFactory.l().i("数据库文件"+file.exists());
                                    }
                                    String key=MobileInfoUtil.getIMEI(LauncherApplication.getContext())+"_"+getStringDate();
                                    upLoad(file,key,token);
                                }
                            }
                        } catch (Exception e) {
                            LogFactory.l().e("e==="+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }





    //上传数据七牛云
    public void upLoad(File data, String key, String token) {
        Configuration config = new Configuration.Builder().build();
        UploadManager uploadManager = new UploadManager(config);
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            LogFactory.l().e("上传七牛云成功");
                            LitePal.deleteDatabase(LitePalDb.zkysDataDb.getDbName());
                        } else {
                            LogFactory.l().e("上传七牛云失败");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        LogFactory.l().e(key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

}
