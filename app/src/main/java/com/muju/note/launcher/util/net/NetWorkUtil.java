package com.muju.note.launcher.util.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.muju.note.launcher.util.log.LogFactory;

import java.text.DecimalFormat;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class NetWorkUtil {

    private static final String TAG = NetWorkUtil.class.getSimpleName();

    public static final String NETWORK_NONE = "无网络连接"; // 没有网络连接
    public static final String NETWORK_WIFI = "WIFI"; // wifi连接
    public static final String NETWORK_2G = "2G"; // 2G
    public static final String NETWORK_3G = "3G"; // 3G
    public static final String NETWORK_4G = "4G"; // 4G
    public static final String NETWORK_MOBILE = "未知"; // 手机流量
    public static int NETWORK_TYPE = 0; // 0.4G  1.WIFI 2.无网络连接
    public static String NETWORK_LEVEN = "";
    public static int Itedbm = 0;

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static String getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            NETWORK_TYPE = 2;
            return NETWORK_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();

        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            NETWORK_TYPE = 2;
            return NETWORK_NONE;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    NETWORK_TYPE = 1;
                    return NETWORK_WIFI;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        int networkType = telephonyManager.getNetworkType();
        int networkType = activeNetInfo.getSubtype();
        switch (networkType) {
    /*
       GPRS : 2G(2.5) General Packet Radia Service 114kbps
       EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
       UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
       CDMA : 2G 电信 Code Division Multiple Access 码分多址
       EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps -
       2.4mbps 属于3G
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
                NETWORK_TYPE = 0;
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
                NETWORK_TYPE = 0;
                return NETWORK_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                NETWORK_TYPE = 0;
                return NETWORK_4G;
            default:
                NETWORK_TYPE = 0;
                return NETWORK_MOBILE;
        }
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

    /**
     * 获取实时网速
     */
    private static long rxtxTotal = 0;

    public static String getNetWorkLine() {
        long tempSum = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum - rxtxTotal;
        double totalSpeed = rxtxLast * 1000 / 2000d;
        rxtxTotal = tempSum;
        return showSpeed(totalSpeed);
    }

    /**
     * 网络格式转换
     *
     * @param speed
     * @return
     */
    public static String showSpeed(double speed) {
        DecimalFormat showFloatFormat = new DecimalFormat("0.00");
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        }
        return speedString;
    }


    //获取wifi信号强度
    public static int getWifiLevel(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int wifi = mWifiInfo.getRssi();//获取wifi信号强度
        return wifi;
    }


    //移动网络下信号
    public static int getCurrentNetDBM(final Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        PhoneStateListener mylistener = new PhoneStateListener() {
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
//                super.onSignalStrengthsChanged(signalStrength);
                String signalInfo = signalStrength.toString();
                String[] params = signalInfo.split(" ");
                int level = signalStrength.getLevel();
                if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络 最佳范围   >-90dBm 越大越好
                    Itedbm = Integer.parseInt(params[9]);
                    String bin;
                    if (Itedbm > -95) {
                        bin = "网络很好";
                    } else if (Itedbm > -105) {
                        bin = "网络不错";
                    } else if (Itedbm > -115) {
                        bin = "网络还行";
                    } else if (Itedbm > -120) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }

                    NETWORK_LEVEN = "dbm:" + Itedbm + "\n" + bin + "\n level" + level;
                } else if (tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    //在这个范围的已经确定是3G，但不同运营商的3G有不同的获取方法，故在此需做判断 判断运营商与网络类型的工具类在最下方
//                    Itedbm = signalStrength.getCdmaDbm();
                    try {
                        Itedbm= (int) signalStrength.getClass().getMethod("getDbm").invoke(signalStrength);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String bin;
                    if (Itedbm > -75) {
                        bin = "网络很好";
                    } else if (Itedbm > -85) {
                        bin = "网络不错";
                    } else if (Itedbm > -95) {
                        bin = "网络还行";
                    } else if (Itedbm > -100) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }
                    NETWORK_LEVEN = "dbm:" + Itedbm + "\n" + bin + "\n level" +
                            level;
                } else {
                    //2G网络最佳范围>-90dBm 越大越好
                    int asu = signalStrength.getGsmSignalStrength();
                    Itedbm = -113 + 2 * asu;
                    NETWORK_LEVEN = "dbm:" + Itedbm + "\n" + "没有4G信号,网络很差" + "\n " +
                            "level" + level + "\nasu:" + asu;
                }
//                LogFactory.l().i("信号强度==="+Itedbm);
            }
        };
        //开始监听
        tm.listen(mylistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        tm.listen(mylistener,PhoneStateListener.LISTEN_NONE);
        return Itedbm;
    }
}
