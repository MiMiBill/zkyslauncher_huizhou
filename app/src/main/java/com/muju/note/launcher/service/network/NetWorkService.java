package com.muju.note.launcher.service.network;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

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

    public void start(Activity activity){
        final TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener=new PhoneStateListener(){
            @Override
            //获取对应网络的ID，这个方法在这个程序中没什么用处
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {
                    int CID = ((GsmCellLocation) location).getCid();
                } else if (location instanceof CdmaCellLocation) {
                    int ID = ((CdmaCellLocation) location).getBaseStationId();
                }
            }

            //系统自带的服务监听器，实时监听网络状态
            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }
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
                    LogUtil.d(TAG,"网络情况:"+NETWORK_STR);
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
                    LogUtil.d(TAG,"网络情况:"+NETWORK_STR);
                    return;
                } else {
                    //这个dbm 是2G和3G信号的值
                    int asu = signalStrength.getGsmSignalStrength();
                    int dbm = -113 + 2 * asu;
                    NETWORK_STR = "dbm:" + dbm + "\n" + "没有4G信号,网络很差" + "\n level" + level + "\nasu:" + asu;
                    LogUtil.d(TAG,"网络情况:"+NETWORK_STR);
                }
                onSignalStrengthsChanged(signalStrength);
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }
}
