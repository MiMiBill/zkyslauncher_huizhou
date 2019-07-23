package com.muju.note.launcher.app.timetask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.home.bean.CrontabBean;
import com.muju.note.launcher.app.home.db.CrontabDao;
import com.muju.note.launcher.app.home.event.CrontabEvent;
import com.muju.note.launcher.app.home.ui.HomeFragment;
import com.muju.note.launcher.app.video.util.DbHelper;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.broadcast.CrontabReceiver;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.Calendar;
import java.util.List;

//定时任务
public class CrontabService {
    private static final String TAG = "CrontabService";

    public static CrontabService crontabService = null;

    public static CrontabService getInstance() {
        if (crontabService == null) {
            crontabService = new CrontabService();
        }
        return crontabService;
    }


    public void start() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getTimeTask();
    }


    //查询定时任务
    private void getTimeTask() {
        if (HomeFragment.entity != null) {
            OkGo.<String>post(UrlUtil.getBedList()).tag(UrlUtil.getBedList())
                    .params("bedId", HomeFragment.entity.getBedId())
                    .params("hospId", ActiveUtils.getPadActiveInfo().getHospitalId())
                    .params("deptId", ActiveUtils.getPadActiveInfo().getDeptId())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                if (jsonObject.optInt("code") == 200) {
                                    Gson gson = new Gson();
                                    CrontabBean bean = gson.fromJson(response.body(), CrontabBean
                                            .class);
                                    List<CrontabBean.DataBean> crontabBean = bean.getData();
                                    LitePalDb.setZkysDb();
                                    LitePal.deleteAll(CrontabDao.class);
                                    DbHelper.insertTaskListDb(crontabBean);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }


    //开启定时事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CrontabEvent event) {
        startTask();
    }


    private void startTask() {
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
//        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        LitePalDb.setZkysDb();
        LitePal.findAllAsync(CrontabDao.class).listen(new FindMultiCallback<CrontabDao>() {
            @Override
            public void onFinish(List<CrontabDao> list) {
                long systemTime = System.currentTimeMillis();
                for (CrontabDao dao : list) {
//                    LogFactory.l().i("dao.getAddType()==="+dao.getAddType());
                    if (dao.getAddType() == 0) {  //单次
                        long stopTime = getStopTime(dao).getTimeInMillis();
                        Calendar mCalendar = getStartTime(dao);
                        long startTime = mCalendar.getTimeInMillis();
//                        LogFactory.l().i("startTime==="+startTime);
                        if (systemTime > stopTime) { //当前时间>停止时间
                            return;
                        }
                        addAlarmForOnce(LauncherApplication.getInstance(), startTime, dao
                                .getCrontabid() + "", dao.getFileType(), dao.getFileAddr());
//                        addAlarmForOnce(CrontabService.this,startTime,"47",dao.getFileType(),
// dao.getFileAddr());
                    } else if (dao.getAddType() == 1) {    //每天
                        long stopTime = getStopTime(dao).getTimeInMillis();
                        if (systemTime > stopTime) { //当前时间>停止时间
                            return;
                        }
                        Calendar mCalendar = getStartTime(dao);
                        long startTime = mCalendar.getTimeInMillis();
                        if (systemTime > startTime) { //当前时间大于执行时间从第二天开始
                            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        addRepeatAlarm(LauncherApplication.getInstance(), dao.getCrontabid() +
                                        "", startTime,
                                (long) (60 * 60 * 24 * 1000), "1", "", dao.getFileType(), dao
                                        .getFileAddr());

//                        addRepeatAlarm(CrontabService.this,"58",startTime,
//                                (long)(60*60*24*1000),"1","",dao.getFileType(),dao.getFileAddr());
                    }
                    if (dao.getAddType() == 2) {  //自定义
                        long stopTime = getStopTime(dao).getTimeInMillis();
                        if (systemTime > stopTime) { //当前时间>停止时间
                            return;
                        }
                        Calendar mCalendar = getStartTime(dao);
                        long startTime = mCalendar.getTimeInMillis();
                        if (systemTime > startTime) { //当前时间大于执行时间从第二天开始
                            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
                        }
                        addRepeatAlarm(LauncherApplication.getInstance(), dao.getCrontabid() +
                                "", startTime, (long) (60 * 60 * 24 * 1000), "2", dao.getWeeks(),
                                dao.getFileType(), dao.getFileAddr());
//                        addRepeatAlarm(CrontabService.this,"57",
//                                startTime,(long)(60*60*24*1000),"2",dao.getWeeks(),dao
// .getFileType(),dao.getFileAddr());
                    }
                }
            }
        });
    }


    //获取开始时间
    private Calendar getStartTime(CrontabDao dao) {
        Calendar mCalendar = Calendar.getInstance();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, Integer.valueOf(dao.getStartDate().substring(0, 4)));
        mCalendar.set(Calendar.MONTH, Integer.valueOf(dao.getStartDate().substring(5, 7)) - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dao.getStartDate().substring(8, 10)));
       /* mCalendar.set(Calendar.DAY_OF_MONTH,18);   //测试用
//        mCalendar.set(Calendar.HOUR_OF_DAY,13);
        if(dao.getAddType()==0){
            mCalendar.set(Calendar.MINUTE,32);
        }else if(dao.getAddType()==1) {
            mCalendar.set(Calendar.MINUTE,33);
        }else if(dao.getAddType()==2){
            mCalendar.set(Calendar.MINUTE,34);
        }*/
        mCalendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(dao.getExecTime().substring(0, 2)));
        mCalendar.set(Calendar.MINUTE, Integer.valueOf(dao.getExecTime().substring(3, 5)));
        mCalendar.set(Calendar.SECOND, 0);
        return mCalendar;
    }


    //获取开始时间
    private Calendar getStopTime(CrontabDao dao) {
        Calendar mCalendar = Calendar.getInstance();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.YEAR, Integer.valueOf(dao.getStopDate().substring(0, 4)));
        mCalendar.set(Calendar.MONTH, Integer.valueOf(dao.getStopDate().substring(5, 7)) - 1);
        mCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dao.getStopDate().substring(8, 10)));
        return mCalendar;
    }


    //单次事件
    private void addAlarmForOnce(Context context, long timeString, String id, String fileType,
                                 String fileAddr) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(Constants.CRONTAB_ACTION_SINGLE);
        intent.putExtra("Mode", "0"); //单次事件
        intent.putExtra("id", id);
        intent.putExtra("fileType", fileType);
        intent.putExtra("fileAddr", fileAddr);
        intent.putExtra("remind_number", "");
        intent.setClass(context, CrontabReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
//        LogFactory.l().i("addAlarmForOnce===");
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeString, pendingIntent);
    }


    //重复事件
    public void addRepeatAlarm(Context context, String id, long timeString, Long duration,
                               String Mode, String remind_number, String fileType, String
                                       fileAddr) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setClass(context, CrontabReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("Mode", Mode);
        intent.putExtra("fileType", fileType);
        intent.putExtra("fileAddr", fileAddr);
        intent.putExtra("remind_number", remind_number);
        if (Mode.equals("1")) {
            intent.setAction(Constants.CRONTAB_ACTION_DAY);
        } else if (Mode.equals("2")) {
            intent.setAction(Constants.CRONTAB_ACTION_WEEK);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        LogFactory.l().i("addRepeatAlarm===");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeString, duration, pendingIntent);
    }


    public void cancleAlarm(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent i = new Intent(context, CrontabReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pi);
    }

}
