package com.muju.note.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.activity.WebActivity;
import com.muju.note.launcher.app.home.event.PatientEvent;
import com.muju.note.launcher.app.msg.db.CustomMessageDao;
import com.muju.note.launcher.app.msg.dialog.CustomMsgDialog;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;


/**
 * 医院宣教推送处理
 */
public class CustomMessageService extends Service {

    private final String TAG="CustomMessageService";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String PUSH_CUSTOM_CONTENT = "push_content";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = -1;
        if (intent != null)
            type = intent.getIntExtra(MESSAGE_TYPE, -1);
        LogUtil.e(TAG,"type == " + type);
        switch (type) {
            //宣教
            case 1:
                Gson gson = new Gson();
                CustomMessageDao entity=new CustomMessageDao();
                try {
                    entity = gson.fromJson(intent.getStringExtra(PUSH_CUSTOM_CONTENT), CustomMessageDao.class);
                } catch (Exception e) {
                    entity = null;
                    LogUtil.e(PUSH_CUSTOM_CONTENT, "宣教解析异常");
                }

                if (entity != null) {
                    entity.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
                    entity.setCreateTime(System.currentTimeMillis());
                    LitePalDb.setZkysDb();
                    entity.save();
                    EventBus.getDefault().post(new PatientEvent(PatientEvent.MISSION_ADD));
                    WebActivity.launchXJ(CustomMessageService.this, entity.getUrl(), entity.getTitle(), entity.getXjId());
                }
                break;
            //自定义消息
            case 7:
                CustomMsgDialog dialog = new CustomMsgDialog(this, intent.getStringExtra(PUSH_CUSTOM_CONTENT))
                        .setCustView(R.layout.dialog_custom_msg_layout);
                dialog.show();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
