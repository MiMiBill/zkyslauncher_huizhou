package com.muju.note.launcher.service.location;

import android.provider.Settings;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.log.LogUtil;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

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
        initWeatherSDK();
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

    private void initWeatherSDK()
    {

        HeConfig.init("HE2003051739101419","33230a6c317b4a06a7317dfd09ddccdc");
        HeConfig.switchToFreeServerNode();//切换到免费版本


    }

    private void getWeather(String location)
    {

        /**
         * 实况天气
         * 实况天气即为当前时间点的天气状况以及温湿风压等气象指数，具体包含的数据：体感温度、
         * 实测温度、天气状况、风力、风速、风向、相对湿度、大气压强、降水量、能见度等。
         *
         * @param context  上下文
         * @param location 地址详解
         * @param lang     多语言，默认为简体中文，海外城市默认为英文
         * @param unit     单位选择，公制（m）或英制（i），默认为公制单位
         * @param listener 网络访问回调接口
         */
        //location格式： "31.305847,121.520267"
        HeWeather.getWeatherNow(LauncherApplication.getContext(), location, Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "Weather Now onError: ", e);
            }

            @Override
            public void onSuccess(Now dataObject) {
                LogUtil.d(TAG, " Weather Now onSuccess: " + new Gson().toJson(dataObject));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(dataObject.getStatus()) ){
                    //此时返回数据
                    NowBase now = dataObject.getNow();
                    LogUtil.d("当下气温：" +  now.getTmp() );
                } else {
                    //在此查看返回数据失败的原因
                    String status = dataObject.getStatus();
                    Code code = Code.toEnum(status);
                    LogUtil.i(TAG, "failed code: " + code);
                }
            }
        });

        /**
         * 实况天气
         * 实况天气即为当前时间点的天气状况以及温湿风压等气象指数，具体包含的数据：体感温度、
         * 实测温度、天气状况、风力、风速、风向、相对湿度、大气压强、降水量、能见度等。
         *
         * @param context  上下文
         * @param location 地址详解
         * @param lang     多语言，默认为简体中文，海外城市默认为英文
         * @param unit     单位选择，公制（m）或英制（i），默认为公制单位
         * @param listener 网络访问回调接口
         */
        HeWeather.getWeatherForecast(LauncherApplication.getContext(), location, Lang.CHINESE_SIMPLIFIED , Unit.METRIC , new HeWeather.OnResultWeatherForecastBeanListener() {

            @Override
            public void onError(Throwable throwable) {
                LogUtil.e(TAG, "Weather ForecastBase onError: ", throwable);
            }

            @Override
            public void onSuccess(Forecast forecast) {
                LogUtil.d(TAG, " Weather ForecastBase onSuccess: " + new Gson().toJson(forecast));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if ( Code.OK.getCode().equalsIgnoreCase(forecast.getStatus()) ){
                    //此时返回数据
                    List<ForecastBase> forecastBaseList = forecast.getDaily_forecast();
                    if (forecastBaseList != null && forecastBaseList.size() > 0)
                    {
                        ForecastBase forecastBase = forecastBaseList.get(0);
                        String tmpMax = forecastBase.getTmp_max();//最高温度
                        String tmpMin = forecastBase.getTmp_min();//最低温度
                        String condTxtd = forecastBase.getCond_txt_d();//整天气温的概括
                        LogUtil.d(TAG,"最高温度：" + tmpMax,"最低温度" + tmpMin, "描述：" + condTxtd);
                    }

                } else {
                    //在此查看返回数据失败的原因
                    String status = forecast.getStatus();
                    Code code = Code.toEnum(status);
                    LogUtil.i(TAG, "failed code: " + code);
                }
            }

        });


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
//            LogUtil.d(TAG,"location: 位置信息：%s   %s   %s", latitude, longitude, address);
//            LogUtil.d(TAG,"location: 位置信息：%s   %s   %s",  location.getAddress().city,  location.getAddress().street, location.getAddress().address);
            LogUtil.d(TAG,"location: 位置信息：%s   %s   %s", location.getAddress().province, location.getAddress().district);
            locationClient.stop();
            //获取天气
            getWeather(latitude + "," + longitude);

        }
    }




}
