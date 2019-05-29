package com.muju.note.launcher.service.location;

import android.provider.Settings;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogUtil;

public class LocationService {

    private static final String TAG="LocationService";

    public static LocationService locationService = null;

    public static LocationService getInstance() {
        if (locationService == null) {
            locationService = new LocationService();
        }
        return locationService;
    }

    private LocationClient locationClient;
    private MyLocationListener locationListener=new MyLocationListener();
    public String latitude,longitude,address;

    /**
     *  开启定位
     */
    public void start() {
        LogUtil.d(TAG,"开始定位");
        locationClient=new LocationClient(LauncherApplication.getContext());
        locationClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        //可选，设置定位模式，默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setCoorType("bd09ll");
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(1000);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);
        //设置需要地址信息
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取纬度信息
            latitude = location.getLatitude()+"";
            //获取经度信息
            longitude = location.getLongitude()+"";
            //获取定位精度，默认值为0.0f
            address = location.getAddrStr();
            LogUtil.d(TAG,"location: 位置信息：%s   %s   %s", latitude, longitude, address);
            locationClient.stop();
        }
    }

}
