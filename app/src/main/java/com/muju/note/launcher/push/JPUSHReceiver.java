package com.muju.note.launcher.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.event.PatientEvent;
import com.muju.note.launcher.app.video.bean.PayEntity;
import com.muju.note.launcher.app.video.bean.PayEvent;
import com.muju.note.launcher.app.video.bean.VideoEvent;
import com.muju.note.launcher.app.video.util.PayUtils;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.service.CustomMessageService;
import com.muju.note.launcher.topics.FileTopics;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.file.FileUtils;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;
import com.muju.note.launcher.util.system.SystemUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPUSHReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-JPUSHReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtil.d("[JPUSHReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtil.d("[JPUSHReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtil.d("[JPUSHReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                LogUtil.d("[JPUSHReceiver] 接收到推送下来的附加字段: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//                if (!ActiveUtils.hadActived(LauncherApplication.getInstance()))
//                    return;//如果没激活就不要弹出推送
                JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                switch (jsonObject.optInt("alertType")) {
                    case 11:
                        int volumeRate = Integer.parseInt(bundle.getString(JPushInterface.EXTRA_MESSAGE));
                        LogUtil.d("系统音量改变:%s ",volumeRate );
                        //声音改变
                        SPUtil.putLong(SpTopics.PAD_CONFIG_VOLUME_RATE, volumeRate);
                        SystemUtils.setVolume(LauncherApplication.getInstance(),volumeRate);
                        break;
                    case 201:
                        FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
                        FileUtils.deleteFile(FileTopics.FILE_VIP_VIDEO);
                        EventBus.getDefault().post(new PayEvent(PayEntity.ORDER_TYPE_VIDEO_PREMIUM));
                        EventBus.getDefault().post(new VideoEvent(VideoEvent.PREMIUM));
                        break;
                    case 101:
                    case 102:
                        LogUtil.d("data:%s", bundle.getString(JPushInterface.EXTRA_MESSAGE));
                        String json = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                        JSONObject extraMsgObj = new JSONObject(json);
                        String expire_time = extraMsgObj.getString("expire_time");
                        boolean isValid = DateUtil.isValid(expire_time);
                        if (isValid && !PayUtils.isValid(PayEntity.ORDER_TYPE_VIDEO)) {
                            PayEntity payEntity = new PayEntity(PayEntity.ORDER_TYPE_VIDEO, expire_time);
                            PayUtils.setPaied(payEntity);
                            EventBus.getDefault().post(new VideoEvent(VideoEvent.RESUME));
                            EventBus.getDefault().post(new PayEvent(PayEntity.ORDER_TYPE_VIDEO));
                        }

                        break;
                    case 1:
//                        if (!LauncherApplication.getInstance().getPatient().getDisabled()) {
//                            return;
//                        }
                        FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
                        Intent serviceIntent = new Intent(context, CustomMessageService.class);
                        serviceIntent.putExtra(CustomMessageService.PUSH_CUSTOM_CONTENT, bundle.getString(JPushInterface.EXTRA_MESSAGE));
                        serviceIntent.putExtra(CustomMessageService.MESSAGE_TYPE, 1);
                        context.startService(serviceIntent);
                        break;
                    //自定义消息
                    case 7:
//                        if (!LauncherApplication.getInstance().getPatient().getDisabled()) {
//                            return;
//                        }
                        FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
                        Intent serviceIntent2 = new Intent(context, CustomMessageService.class);
                        serviceIntent2.putExtra(CustomMessageService.PUSH_CUSTOM_CONTENT, bundle.getString(JPushInterface.EXTRA_MESSAGE));
                        serviceIntent2.putExtra(CustomMessageService.MESSAGE_TYPE, 7);
                        context.startService(serviceIntent2);
                        break;
                    case 8://入院
                        FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
                        EventBus.getDefault().post(new PatientEvent(PatientEvent.BIND));
                        break;
                    case 9://出院
                        FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
                        EventBus.getDefault().post(new PatientEvent(PatientEvent.UN_BIND));
                        break;
                    case 10:
////                        if (LauncherApplication.getInstance().getPatient().getDisabled()) {//患者已入院
//                            FileUtils.playReplay(LauncherApplication.getInstance().getApplicationContext(), R.raw.messagetips);
//                            Intent msgIntent = new Intent(context, SatisfactionSurveyActivity.class);
//                            msgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            msgIntent.putExtra(Constants.PAD_SURVEY_ID, bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                            context.startActivity(msgIntent);
////                        }
                        break;
                }
                //processCustomMessage(context, bundle);

               /* Intent msgIntent = new Intent(context, WebActivity.class);
                msgIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                msgIntent.putExtra(WebActivity.WEB_URL, bundle.getString(JPushInterface.EXTRA_MESSAGE));
                msgIntent.putExtra(WebActivity.WEB_TITLE, "测试测试");
                context.startActivity(msgIntent);*/

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtil.d("[JPUSHReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtil.d("[JPUSHReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtil.d("[JPUSHReceiver] 用户点击打开了通知");

                //打开自定义的Activity
				/*Intent i = new Intent(context, TestActivity.class);
				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(i);*/

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtil.d("[JPUSHReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtil.w("[JPUSHReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                LogUtil.d("[JPUSHReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            LogUtil.e(e, e.getMessage());
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtil.i("This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtil.e("Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
/*	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}*/
}