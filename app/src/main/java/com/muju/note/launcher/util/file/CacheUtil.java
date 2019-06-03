package com.muju.note.launcher.util.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muju.note.launcher.app.home.bean.AdvertsBean;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CacheUtil {

    private static List<AdvertsBean> datalist=new ArrayList<>();
    /**
     * 获取广告List
     * @param
     * @return
     */
    public static List<AdvertsBean> getDataList(final String code) {
        final String response = SPUtil.getString(Constants.ZKYS_ADVERTS);
        LogFactory.l().i("response==="+response);
        final Gson gson = new Gson();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.optInt("code")==200){
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj=data.getJSONObject(i);
                            if(obj.optString("code").equals(code)){
                                String adverts=obj.optString("adverts");
                                if(!adverts.equals("[]")){
                                    datalist =gson.fromJson(adverts, new TypeToken<List<AdvertsBean>>() {}.getType());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LogFactory.l().e("e==="+e.getMessage());
                    e.printStackTrace();
                }
            }
        }).run();
        return datalist;
    }
}
