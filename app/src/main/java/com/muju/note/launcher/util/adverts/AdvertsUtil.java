package com.muju.note.launcher.util.adverts;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.dialog.AdvertsDialog;
import com.muju.note.launcher.app.home.bean.AdverNewBean;
import com.muju.note.launcher.app.home.db.AdvertsCodeDao;
import com.muju.note.launcher.app.home.db.AdvertsCountDao;
import com.muju.note.launcher.app.home.db.AdvertsInfoDao;
import com.muju.note.launcher.app.home.event.DefaultVideoEvent;
import com.muju.note.launcher.app.home.event.DefaultVideoLiveEvent;
import com.muju.note.launcher.app.video.dialog.OnAdDialogDismissListener;
import com.muju.note.launcher.app.video.dialog.VideoOrImageDialog;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.entity.AdvertWebEntity;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.litepal.UpAdvertInfoDao;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.view.banana.Banner;
import com.muju.note.launcher.view.banana.BannerPage;
import com.muju.note.launcher.view.banana.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvertsUtil {

    public static AdvertsUtil advertsUtil = null;

    public static AdvertsUtil getInstance() {
        if (advertsUtil == null) {
            advertsUtil = new AdvertsUtil();
        }
        return advertsUtil;
    }

    public static final int TAG_SHOWCOUNT = 0; // 展示次数
    public static final int TAG_CLICKCOUNT = 1; // 点击次数
    public static final int TAG_SHOWTIME = 2; // 展示时间
    public static final int TAG_BROWSETIME = 3; // 浏览时间

    public void queryAdverts(String code, Banner banner, AdvertsDialog dialog) throws Exception {

        queryCodeListAdverts(code, banner, null, dialog);
    }

    //请求广告列表数据
    public void queryCodeListAdverts(final String code, final Banner banner, final ImageView
            imageView, final AdvertsDialog dialog) throws Exception {
        OkGo.<BaseBean<List<AdverNewBean>>>post(UrlUtil.getAdvertsByCodes())
                .params("codes", code)
                .params("hospitalId", ActiveUtils.getPadActiveInfo().getHospitalId())
                .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
//                .params("deptId", 10000)
//                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(-1)
                .cacheKey(code)
                .tag(code)
                .execute(new JsonCallback<BaseBean<List<AdverNewBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<List<AdverNewBean>>> response) {
                        LogFactory.l().i("queryNewAdverts_onSuccess");
//                        SPUtil.putString(Constants.ZKYS_ADVERTS, response.body());
                        try {
                            getNewAdvertsSuccess(response.body().getData());
                        } catch (Exception e) {
                            LogFactory.l().i("e");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<BaseBean<List<AdverNewBean>>> response) {
                        super.onCacheSuccess(response);
                        LogFactory.l().i("queryNewAdverts_onCacheSuccess");
//                        SPUtil.putString(Constants.ZKYS_ADVERTS, response.body());
                        try {
                            getNewAdvertsSuccess(response.body().getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<BaseBean<List<AdverNewBean>>> response) {
                        super.onError(response);
                        try {
                            if (failLisinter != null) {
                                failLisinter.fail();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //展示广告
    private void getNewAdvertsSuccess(List<AdverNewBean> dataList) {
        try {
            LitePal.deleteAll(AdvertsCodeDao.class);
            DbHelper.insertAdvertListDb(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lisinter != null) {
            lisinter.success();
        }
        if (dialogListener != null) {
            dialogListener.success();
        }
        if (showImgListener != null) {
            showImgListener.success();
        }
    }

    //数据库获取数据
    public void showByDbBanner(final List<AdvertsCodeDao> list, final Banner banner) {
        banner.setVisibility(View.VISIBLE);
        List<BannerPage> pageList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAdditionUrl() == null) {
                list.get(i).setAdditionUrl("");
            }
            BannerPage page = new BannerPage(list.get(i).getResourceUrl(), 15000);
//            BannerPage page = new BannerPage(list.get(i).getResourceUrl(), list.get(i)
// .getInterval()*1000); //轮播时长
            pageList.add(page);
        }
        banner.setSlide(true);
        banner.setDataPlay(pageList, 0);
        final int[] id = {0};
        final long[] startTime = {System.currentTimeMillis()};
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
//                LogFactory.l().i("position:"+position);
                try {
                    if (id[0] != 0) {
                        if (position == 0) {
                            return;
                        }
                        if (position == list.size() + 1) {
                            position = 1;
                        }
                        if (id[0] == list.get(position - 1).getAdid()) {
                            return;
                        }
                        if (position == 1) {
                            long currentTime = System.currentTimeMillis();
                            addData(list.get(list.size() - 1).getAdid(), TAG_SHOWTIME, currentTime
                                    - startTime[0]);
                            addDataInfo(list.get(list.size() - 1).getAdid(), TAG_SHOWTIME,
                                    startTime[0], currentTime);
                        } else {
                            long currentTime = System.currentTimeMillis();
                            addData(list.get(position - 2).getAdid(), TAG_SHOWTIME, currentTime -
                                    startTime[0]);
                            addDataInfo(list.get(position - 2).getAdid(), TAG_SHOWTIME,
                                    startTime[0], currentTime);
                        }
                        startTime[0] = System.currentTimeMillis();
                    }
                    if (id[0] != list.get(position - 1).getAdid()) {
                        addData(list.get(position - 1).getAdid(), TAG_SHOWCOUNT);
                        addDataInfo(list.get(position - 1).getAdid(), TAG_SHOWCOUNT);
                    }
                    id[0] = list.get(position - 1).getAdid();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int status) {
//                LogFactory.l().i("status==="+status);
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                AdvertsCodeDao dao = list.get(position);
                try {
                    if (dao.getResourceUrl().endsWith("png") || dao.getResourceUrl().endsWith
                            ("jpg")) {
                        jumpByDb(dao);
                    }
                } catch (Exception e) {
                    LogFactory.l().i("e==" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


    //根据类型跳转不同页面
    private void jumpByDb(AdvertsCodeDao dao) throws Exception {
        //linkType  1:url  2:游戏 3:视频  4:通道  5:图片
        updateOnClickCount(dao.getAdid()); //图片增加点击
        addDataInfo(dao.getAdid(), TAG_CLICKCOUNT);
        LogFactory.l().i("跳转类型===" + dao.getLinkType());
        if (dao.getLinkType() == 1) {
            EventBus.getDefault().post(new AdvertWebEntity(dao.getAdid(), dao.getName(), dao.getLinkContent(),1));
        } else if (dao.getLinkType() == 5) {
            EventBus.getDefault().post(new AdvertWebEntity(dao.getAdid(), dao.getName(), dao.getLinkContent(),5));
        } else if (dao.getLinkType() == 3) {
            EventBus.getDefault().post(new AdvertWebEntity(dao.getAdid(), dao.getName(), dao.getLinkContent(),3));
        } else if (dao.getLinkType() == 2) {
            EventBus.getDefault().post(new AdvertWebEntity(dao.getAdid(), dao.getName(), dao.getLinkContent(),2));
        } else if (dao.getLinkType() == 4) {
            EventBus.getDefault().post(new AdvertWebEntity(dao.getAdid(), dao.getName(), dao.getLinkContent(),4));
        }
    }



    //显示默认图片
    public void showDefaultBanner(Banner banner, int type) {  //区分是home还是锁屏
        LogFactory.l().e("showDefaultBanner");
        banner.setVisibility(View.VISIBLE);
        List<BannerPage> pageList = new ArrayList<>();
        if (type == 0) {
            BannerPage page = new BannerPage("00", 10000);
            BannerPage page1 = new BannerPage("11", 10000);
            pageList.add(page);
            pageList.add(page1);
        } else {
            //首页轮播
            BannerPage page = new BannerPage("22", 10000);
            BannerPage page1 = new BannerPage("33", 10000);
            pageList.add(page);
            pageList.add(page1);

            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if(position==0){
                        EventBus.getDefault().post(new DefaultVideoEvent());
                    }else {
                        EventBus.getDefault().post(new DefaultVideoLiveEvent());
                    }
                }
            });
        }
        banner.setSlide(true);
        banner.setDataPlay(pageList, -100);
    }





    //展示图片
    public void showByImageView(final List<AdvertsCodeDao> list, final ImageView imageView) {
        final AdvertsCodeDao dao = list.get(0);
        Glide.with(LauncherApplication.getContext()).load(dao.getResourceUrl()).into(imageView);
        addData(dao.getAdid(), TAG_SHOWCOUNT);
        addDataInfo(dao.getAdid(), TAG_SHOWCOUNT);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jumpByDb(dao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //图片击进入长图
    public void showByImageView(final Context context, final AdvertsCodeDao dao, final
    ImageView imageView, final ImageView img, final RelativeLayout relCorner) {
        Glide.with(LauncherApplication.getContext()).load(dao.getResourceUrl()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super
                    Drawable> transition) {
                imageView.setImageDrawable(resource);
                if (showImgLoadListener != null) {
                    showImgLoadListener.success();
                }
                relCorner.setVisibility(View.VISIBLE);
            }
        });
        final long time = System.currentTimeMillis();
        addData(dao.getAdid(), TAG_SHOWCOUNT);
        addDataInfo(dao.getAdid(), TAG_SHOWCOUNT);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jumpByDb(dao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                addData(dao.getAdid(), TAG_SHOWTIME, currentTime - time);
                addDataInfo(dao.getAdid(), TAG_SHOWTIME, time, currentTime);
                relCorner.setVisibility(View.GONE);
            }
        });
    }


    //dialog形式展示
    public void showByDialog(final List<AdvertsCodeDao> list, final AdvertsDialog dialog) {
        final AdvertsCodeDao dao = list.get(0);
        final long time = System.currentTimeMillis();
        dialog.setOnImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jumpByDb(dao);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        dialog.loadImg(dao.getResourceUrl(), dao.getCloseType(), dao.getSecond());
        addData(dao.getAdid(), TAG_SHOWCOUNT);
        addDataInfo(dao.getAdid(), TAG_SHOWCOUNT);
        dialog.setOnAdDialogDismissListener(new OnAdDialogDismissListener() {
            @Override
            public void OnAdDialogDismiss() {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                long currentTime = System.currentTimeMillis();
                if (dao.getCloseType() == 2) {
                    dialog.removeHandler();
                }
                addData(dao.getAdid(), TAG_SHOWTIME, currentTime - time);
                addDataInfo(dao.getAdid(), TAG_SHOWTIME, time, currentTime);
            }
        });
    }


    public void showVideoDialog(final AdvertsCodeDao dao, final VideoOrImageDialog dialog)
            throws Exception {
        final long time = System.currentTimeMillis();
        dialog.setOnImgClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jumpByDb(dao);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
        if (dao.getResourceUrl().endsWith("mp4")) {
            dialog.startVideo(dao.getResourceUrl());
        } else {
            dialog.loadImg(dao.getResourceUrl(), dao.getCloseType(), dao.getSecond());
        }
        dialog.setOnAdDialogDismissListener(new OnAdDialogDismissListener() {
            @Override
            public void OnAdDialogDismiss() {
                dialog.dismiss();
            }
        });
        addData(dao.getAdid(), TAG_SHOWCOUNT);
        addDataInfo(dao.getAdid(), TAG_SHOWCOUNT);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                long currentTime = System.currentTimeMillis();
                addData(dao.getAdid(), TAG_SHOWTIME, currentTime - time);
                addDataInfo(dao.getAdid(), TAG_SHOWTIME, time, currentTime);
                if (dao.getCloseType() == 2) {
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
        addData(advertId, TAG_CLICKCOUNT);
    }


    public void addData(int advertId, int tag) {
        addData(advertId, tag, 0);
    }

    private AdvertsCountDao getAdvertsCountByDb(int advertId, String date) {
        LitePalDb.setZkysDb();
        return LitePal.where("advertId=? and date=?", advertId + "", date)
                .findFirst(AdvertsCountDao.class);
    }

    private void saveAdvertsCountByDb(AdvertsCountDao dao) {
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
        AdvertsCountDao dao = getAdvertsCountByDb(advertId, date);
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

    private void saveAdvertsInfoByDb(AdvertsInfoDao dao) {
        LitePalDb.setZkysDb();
        dao.save();
    }


    public void addDataInfo(int advertId, int tag) {
        addDataInfo(advertId, tag, 0, 0);
    }

    /**
     * 添加广告详情数据
     *
     * @param advertId
     * @param tag
     * @param startTime
     * @param endTime
     */
    public void addDataInfo(int advertId, int tag, long startTime, long endTime) {
        try {
            String date = getStringDate();
            UpAdvertInfoDao dao = new UpAdvertInfoDao();
            dao.setAdvertId(advertId);
            dao.setDate(date);
            dao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
            dao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
            dao.setType(tag);
            dao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
            switch (tag) {
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
                    dao.setTime((endTime - startTime));
                    break;
            }
//        saveAdvertsInfoByDb(dao);
            DbHelper.insertToAdvertData(LitePalDb.DBNAME_ZKYS_DATA, dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStringDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
        Date d = new Date(System.currentTimeMillis());
        return format.format(d);
    }
}
