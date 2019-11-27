package com.muju.note.launcher.service.updatedata;

import com.muju.note.launcher.app.hostipal.service.MienService;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.app.video.service.VideoService;
import com.muju.note.launcher.service.encyclope.EncyclopeService;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  本地数据更新相关
 */
public class UpdateDataService {

    public static UpdateDataService updateService=null;

    public static UpdateDataService getInstance(){
        if(updateService==null){
            updateService=new UpdateDataService();
        }
        return updateService;
    }

    private boolean isUpdate=false;

    public void start(){
        int videoDay = SPUtil.getInt(SpTopics.SP_UPDATE_DATA_DAY);
        Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        if (day != videoDay) {
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 22 || hour < 7) {
                if (isUpdate) {
                    return;
                }
                isUpdate = true;
                // 一个小时内随机获取
                int num = new Random().nextInt(49) + 1;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 上传数据信息
                        LogUtil.d("夜里10到7点之间的线程执行");
                        update();
                        SPUtil.putInt(SpTopics.SP_UPDATE_DATA_DAY,day);
                    }
                }, 1000 * 60 * num);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 以防万一，12个小时候重置状态
                        isUpdate=false;
                    }
                },1000*60*60*12);
            }
        }
    }

    public void update(){

        // 更新影视列表信息
        VideoService.getInstance().getUpdateVideo();
        // 更新影视分类信息
        VideoService.getInstance().getVideoCloumns();
        // 更新影视首页信息
        VideoService.getInstance().getVideoTopInfo();

        // 更新科室宣教信息
        MissionService.getInstance().updateMission(1);

        // 更新医院风采信息
        MienService.getInstance().getMienInfo();

        //更新医疗百科信息
        EncyclopeService.getInstance().getLately();


    }

}
