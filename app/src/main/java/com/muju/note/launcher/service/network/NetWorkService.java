package com.muju.note.launcher.service.network;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.DateUtil;
import com.muju.note.launcher.util.log.LogUtil;
import com.muju.note.launcher.util.net.NetWorkUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 *  判断信号强度
 */
public class NetWorkService {

    private static final String TAG=NetWorkService.class.getSimpleName();
    public static NetWorkService netWorkService=null;
    public static NetWorkService getInstance(){
        if(netWorkService==null){
            netWorkService=new NetWorkService();
        }
        return netWorkService;
    }

    public static String NETWORK_STR;

    public void start(){
        final TelephonyManager telephonyManager = (TelephonyManager) LauncherApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener=new PhoneStateListener(){
            //这个是我们的主角，就是获取对应网络信号强度
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                //这个ltedbm 是4G信号的值
                String signalinfo = signalStrength.toString();
                String[] parts = signalinfo.split(" ");
                int ltedbm = Integer.parseInt(parts[9]);
                int level = signalStrength.getLevel();
                if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    String bin;
                    if (ltedbm > -75) {
                        bin = "网络很好";
                    } else if (ltedbm > -85) {
                        bin = "网络不错";
                    } else if (ltedbm > -95) {
                        bin = "网络还行";
                    } else if (ltedbm > -100) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }
                    NETWORK_STR = "dbm:" + ltedbm + "\n" + bin + "\n level" + level;
                    return;
                } else if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    ltedbm = signalStrength.getCdmaDbm();
                    String bin;
                    if (ltedbm > -75) {
                        bin = "网络很好";
                    } else if (ltedbm > -85) {
                        bin = "网络不错";
                    } else if (ltedbm > -95) {
                        bin = "网络还行";
                    } else if (ltedbm > -100) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }
                    NETWORK_STR = "dbm:" + ltedbm + "\n" + bin + "\n level" + level;
                    return;
                } else {
                    //这个dbm 是2G和3G信号的值
                    int asu = signalStrength.getGsmSignalStrength();
                    int dbm = -113 + 2 * asu;
                    NETWORK_STR = "dbm:" + dbm + "\n" + "没有4G信号,网络很差" + "\n level" + level + "\nasu:" + asu;
                }
                onSignalStrengthsChanged(signalStrength);
                LogUtil.d(TAG,"网络情况:"+NETWORK_STR);
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void getNet(){
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.i(TAG,"信号："+NETWORK_STR);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
