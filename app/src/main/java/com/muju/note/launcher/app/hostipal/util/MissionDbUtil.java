package com.muju.note.launcher.app.hostipal.util;

import com.muju.note.launcher.app.home.db.ModelCountDao;
import com.muju.note.launcher.app.hostipal.db.MissionCountDao;
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
 *  宣教统计工具类
 */
public class MissionDbUtil {

    public static MissionDbUtil missionDbUtil=null;
    public static MissionDbUtil getInstance(){
        if(missionDbUtil==null){
            missionDbUtil=new MissionDbUtil();
        }
        return missionDbUtil;
    }

    public void addData(final int missionId, final String name, final long startTime, final long endTime){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date(System.currentTimeMillis());
                final String date = format.format(d);
                LitePalDb.setZkysDb();
                LitePal.where("missionId=? and date=? ", missionId+"", date).findFirstAsync(MissionCountDao.class).listen(new FindCallback<MissionCountDao>() {
                    @Override
                    public void onFinish(MissionCountDao missionCountDao) {
                        if (missionCountDao == null) {
                            addCount(date, startTime,endTime, name, missionId);
                        } else {
                            updateCount(missionCountDao, startTime, endTime);
                        }
                    }
                });
            }
        });
    }

    private void addCount(String date,long startTime,long endTime,String name,int missionId){
        try {
            MissionCountDao dao=new MissionCountDao();
            dao.setDate(date);
            dao.setDeptId(ActiveUtils.getPadActiveInfo().getDeptId());
            dao.setHospId(ActiveUtils.getPadActiveInfo().getHospitalId());
            dao.setImei(MobileInfoUtil.getIMEI(LauncherApplication.getContext()));
            dao.setShowCount(1);
            dao.setShowTime((endTime-startTime));
            dao.setMissionId(missionId);
            dao.setMissionName(name);
            LitePalDb.setZkysDb();
            dao.save();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateCount(MissionCountDao dao, long startTime,long endTime) {
        try {
            dao.setShowCount(dao.getShowCount() + 1);
            dao.setShowTime(dao.getShowTime() + (endTime - startTime));
            LitePalDb.setZkysDb();
            dao.update(dao.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
