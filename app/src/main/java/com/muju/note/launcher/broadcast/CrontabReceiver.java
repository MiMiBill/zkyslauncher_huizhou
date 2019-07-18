package com.muju.note.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.muju.note.launcher.entity.PushCustomMessageEntity;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.log.LogFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

public class CrontabReceiver extends BroadcastReceiver {
    private String id = "";
    private String Mode = "0";
    private String remind_number = "";
    private String fileType = "";
    private String fileAddr = "";
    @Override
    public void onReceive(Context context, Intent intent) {
//        LogFactory.l().i("onReceive==="+intent.getAction());
        if (intent.getAction().equals(Constants.CRONTAB_ACTION_SINGLE)) {   //单次事件
            id = intent.getStringExtra("id");
            EventBus.getDefault().post(new PushCustomMessageEntity(id));
        }else if(intent.getAction().equals(Constants.CRONTAB_ACTION_DAY)){  //每天
            id = intent.getStringExtra("id");
            EventBus.getDefault().post(new PushCustomMessageEntity(id));
        }else if(intent.getAction().equals(Constants.CRONTAB_ACTION_WEEK)){ //自定义week
            id = intent.getStringExtra("id");
            remind_number = intent.getStringExtra("remind_number");
            LogFactory.l().i("remind_number==="+remind_number);
            String[] arrr = remind_number.split(",");
            Calendar cc = Calendar.getInstance();
            int weeks = cc.get(Calendar.DAY_OF_WEEK);
            String Tweeks = weeks + "";
            LogFactory.l().i("Tweeks==="+Tweeks);
            for (int i = 0; i < arrr.length; i++) {
                if (Integer.valueOf(arrr[i])==0){
                    arrr[i]="7";
                }
                if (arrr[i].equals(Tweeks)) {
                    EventBus.getDefault().post(new PushCustomMessageEntity(id));
                }
            }
        }
    }
}
