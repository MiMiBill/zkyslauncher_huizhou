package com.muju.note.launcher.util.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class NetWorkUtil {

    public static final String NETWORK_NONE = "NULL"; // 没有网络连接
    public static final String  NETWORK_WIFI = "WIFI"; // wifi连接
    public static final String NETWORK_2G = "002G"; // 2G
    public static final String NETWORK_3G = "003G"; // 3G
    public static final String NETWORK_4G = "004G"; // 4G
    public static final String NETWORK_MOBILE = "00LL"; // 手机流量
    public static String NETWORK_LEVEN="";

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return NETWORK_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
    /*
       GPRS : 2G(2.5) General Packet Radia Service 114kbps
       EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
       UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
       CDMA : 2G 电信 Code Division Multiple Access 码分多址
       EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
       EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
       1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
       HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
       HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
       HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
       IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
       EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
       LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
       EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
       HSPAP : 3G HSPAP 比 HSDPA 快些
       */
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_4G;
            default:
                return NETWORK_MOBILE;
        }
    }

    /**
     * 获取手机信号强度
     */
    @SuppressLint("CheckResult")
    public static void getSignalStrength(final Context context) {
        Observable.just(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        PhoneStateListener MyPhoneListener = new PhoneStateListener() {
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

                                    NETWORK_LEVEN = "dbm:" + ltedbm + "\n" + bin + "\n level" + level;
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
                                    NETWORK_LEVEN = "dbm:" + ltedbm + "\n" + bin + "\n level" + level;
                                    return;
                                } else {
                                    //这个dbm 是2G和3G信号的值
                                    int asu = signalStrength.getGsmSignalStrength();
                                    int dbm = -113 + 2 * asu;

                                    NETWORK_LEVEN = "dbm:" + dbm + "\n" + "没有4G信号,网络很差" + "\n level" + level + "\nasu:" + asu;
                                }
                                onSignalStrengthsChanged(signalStrength);
                            }
                        };
                        telephonyManager.listen(MyPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                    }
                });
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
