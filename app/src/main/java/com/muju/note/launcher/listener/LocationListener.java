package com.muju.note.launcher.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogUtil;

public class LocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        //获取纬度信息
        double latitude = location.getLatitude();
        //获取经度信息
        double longitude = location.getLongitude();
        //获取定位精度，默认值为0.0f
        float radius = location.getRadius();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        String coorType = location.getCoorType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        int errorCode = location.getLocType();
        String addr = location.getAddrStr();
        LauncherApplication.getInstance().latitude = "" + latitude;
        LauncherApplication.getInstance().longitude = "" + longitude;
        LauncherApplication.getInstance().address = addr;
        LogUtil.d("location: 位置信息：%s   %s   %s", latitude, longitude, addr);
    }
}
