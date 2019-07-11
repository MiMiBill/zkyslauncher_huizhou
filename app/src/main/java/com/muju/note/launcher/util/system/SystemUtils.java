package com.muju.note.launcher.util.system;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.PowerManager;
import android.provider.Settings;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.broadcast.ScreenOffAdminReceiver;
import com.muju.note.launcher.topics.SpTopics;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.sp.SPUtil;

import java.lang.reflect.Method;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.POWER_SERVICE;

public class SystemUtils {

    /**
     * 获得当前屏幕亮度值  0--255
     */
    public static int getScreenBrightness(){
        int screenBrightness=255;
        try{
            screenBrightness = Settings.System.getInt(LauncherApplication.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Exception localException){
            LogUtil.e(localException, localException.getMessage());
        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值  0--255
     */
    public static  void saveScreenBrightness(int paramInt){
        try{
            Settings.System.putInt(LauncherApplication.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }

    /**
     * 设置系统音量,并保存
     * @param context
     * @param volumeRate
     */
    public static void setVolume(Context context, int volumeRate){
        AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        int volume = (int) (getMaxVolume(context) / 100D * volumeRate);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        int currentVolume = SystemUtils.getCurrentVolume(LauncherApplication.getContext());
        LogUtil.d("比率:%s 设置的音量:%s  当前系统音量:%s 设置音量有没有超过系统音量:%s", volumeRate, volume, currentVolume, currentVolume > volume);
    }

    public static int getMaxVolume(Context context){
        AudioManager manager=(AudioManager) context.getSystemService(AUDIO_SERVICE);
        return manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    public static int getCurrentVolume(Context context){
        AudioManager manager=(AudioManager) context.getSystemService(AUDIO_SERVICE);
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 关闭屏幕
     */
    public static void screenOff() {
        PowerManager mPowerManager = (PowerManager) LauncherApplication.getContext().getSystemService(POWER_SERVICE);
        if (!(mPowerManager.isInteractive() || mPowerManager.isScreenOn())) {
            return;
        }

        DevicePolicyManager policyManager = (DevicePolicyManager) LauncherApplication.getContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(LauncherApplication.getContext(), ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        LogUtil.i("admin:"+admin);
        if (admin) {
            // isScreenOn = false;
            policyManager.lockNow();
        } else {
            try {
                Method setActiveAdmin = policyManager.getClass().getDeclaredMethod("setActiveAdmin", ComponentName.class, boolean.class);
                setActiveAdmin.setAccessible(true);
                setActiveAdmin.invoke(policyManager, adminReceiver, true);
                policyManager.lockNow();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 开启屏幕
     */
    public static void turnOnScreen() {
        // turn on screen
        PowerManager mPowerManager = (PowerManager) LauncherApplication.getContext().getSystemService(POWER_SERVICE);
        if (mPowerManager.isInteractive() || mPowerManager.isScreenOn()) {
            return;
        }
        PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        mWakeLock.acquire();
        mWakeLock.release();
    }

    /**
     * 获取当前屏幕是开屏还是锁屏
     *
     * @return
     */
    public static boolean isLock() {
        PowerManager pm = (PowerManager) LauncherApplication.getContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        return isScreenOn;
    }


    /**
     * 设置通知静音
     *
     * @param context
     */
    public static void setVolumeNotification(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
//        manager.setStreamVolume(AudioManager.STREAM_SYSTEM,index,AudioManager.FLAG_SHOW_UI);
        manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
    }
}
