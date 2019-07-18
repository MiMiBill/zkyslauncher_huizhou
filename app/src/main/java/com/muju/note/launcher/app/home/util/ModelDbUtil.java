package com.muju.note.launcher.app.home.util;

import com.muju.note.launcher.app.home.db.ModelCountDao;
import com.muju.note.launcher.app.home.db.ModelInfoDao;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.app.MobileInfoUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 模块数据统计
 */
public class ModelDbUtil {

    private static final String TAG = ModelDbUtil.class.getSimpleName();

    public static ModelDbUtil modelDbUtil = null;

    public static ModelDbUtil getInstance() {
        if (modelDbUtil == null) {
            modelDbUtil = new ModelDbUtil();
        }
        return modelDbUtil;
    }

    /**
     * 添加模块count数据
     *
     * @param className
     * @param startTime
     * @param endTime
     */
    public void modelCount(final String className, final long startTime, final long endTime) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                final String tagName = getTagName(className);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date(System.currentTimeMillis());
                final String date = format.format(d);
                LitePalDb.setZkysDb();
                LitePal.where("modelTag=? and date=? and modelName=?", tagName, date, className).findFirstAsync(ModelCountDao.class).listen(new FindCallback<ModelCountDao>() {
                    @Override
                    public void onFinish(ModelCountDao modelCountDao) {
                        if (modelCountDao == null) {
                            addCount(tagName, className, date, startTime, endTime);
                        } else {
                            updateCount(modelCountDao, startTime, endTime);
                        }
                    }
                });
            }
        });
    }

    private void addCount(String tagName, String className, String date, long startTime, long endTime) {
        ModelCountDao dao = new ModelCountDao();
        dao.setDate(date);
        dao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
        dao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
        dao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
        dao.setModelName(className);
        dao.setModelTag(tagName);
        dao.setShowCount(1);
        dao.setShowTime((endTime - startTime));
        LitePalDb.setZkysDb();
        dao.save();
    }

    private void updateCount(ModelCountDao dao, long startTime, long endTime) {
        dao.setShowCount(dao.getShowCount() + 1);
        dao.setShowTime(dao.getShowTime() + (endTime - startTime));
        LitePalDb.setZkysDb();
        dao.update(dao.getId());
    }

    /**
     * 添加模块详情数据
     *
     * @param className
     * @param startTime
     * @param endTime
     */
    public void modelInfo(final String className, final long startTime, final long endTime) {
        try {
            final String tagName = getTagName(className);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            Date d = new Date(System.currentTimeMillis());
            final String date = format.format(d);
            LitePalDb.setZkysDb();
            ModelInfoDao dao = new ModelInfoDao();
            dao.setDate(date);
            if(ActiveUtils.getPadActiveInfo()!=null){
                dao.setDepId(ActiveUtils.getPadActiveInfo().getDeptId());
                dao.setHosId(ActiveUtils.getPadActiveInfo().getHospitalId());
            }else {
                dao.setDepId(0);
                dao.setHosId(0);
            }

            dao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
            dao.setModelName(className);
            dao.setModelTag(tagName);
            dao.setStartTime(startTime);
            dao.setEndTime(endTime);
            dao.setTime((endTime - startTime));
            DbHelper.insertToModelData(LitePalDb.DBNAME_ZKYS_DATA, dao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTagName(String className) {
        switch (className) {
            case "HomeFragment":
                return "首页";

            case "HospitalMienFragment":
                return "风采";

            case "HosPitalMissionFragment":
            case "HospitalMissionVideoFragment":
            case "HospitalMissionPdfFragment":
                return "宣教";

            case "HospitalEncyFragment":
                return "百科";

            case "HospitalelServiceFragment":
            case "CostFragment":
            case "ChemotherFragment":
                return "医疗";

            case "VideoFragment":
            case "VideoContentFragment":
            case "WotvPlayFragment":
            case "VideoHelperFragment":
                return "影视";

            case "PublicNumFragment":
                return "任务广告";

            case "WoTvVideoLineFragment":
                return "直播";

            case "HealthyFragment":
                return "健康";

            case "UserSettingFragment":
            case "UserFragment":
            case "ToolFragment":
            case "FeedBackFragment":
            case "SignTaskFragment":
            case "LuckDrawFragment":
                return "用户";

            case "MsgFragment":
                return "通知";

            case "GuideFragment":
                return "新手";

            case "VoiceFragment":
                return "设置";

            case "BedSideCardFragment":
                return "床头卡";

            case "ClideFragment":
                return "儿童";

            case "OrderFoodFragment":
                return "点餐";

            case "InsureanceFragment":
                return "保险";

            case "FinanceFragment":
                return "金融";

            case "ShopFragment":
                return "购物";

            case "GameFragment":
                return "游戏";

            case "CabinetFragment":
            case "CabinetOrderFragment":
            case "UnlockFragment":
            case "ReturnBedFragment":
                return "柜子";

            case "WebViewFragment":
            case "AdvideoViewFragment":
            case "LargePicFragment":
                return "广告";

            case "SatisfactionSurveyFragment":
                return "问卷";

            case "ProtectionProcessFragment":
                return "锁屏";

            case "StartCheckNetWorkFragment":
            case "ActivationFragment":
            case "StartCheckDataFragment":
                return "激活";
        }
        return "首页";
    }

}
