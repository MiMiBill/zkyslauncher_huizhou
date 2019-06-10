package com.muju.note.launcher.service.encyclope;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.muju.note.launcher.app.hostipal.bean.EncyUpdateBean;
import com.muju.note.launcher.app.hostipal.bean.GetDownloadBean;
import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.okgo.BaseBean;
import com.muju.note.launcher.okgo.JsonCallback;
import com.muju.note.launcher.url.UrlUtil;
import com.muju.note.launcher.util.Constants;
import com.muju.note.launcher.util.log.LogFactory;
import com.muju.note.launcher.util.sp.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  医疗百科
 */
public class EncyclopeService {

    public static EncyclopeService encyService=null;

    public static EncyclopeService getInstance(){
        if(encyService==null){
            encyService=new EncyclopeService();
        }
        return encyService;
    }

    public void start(){
        LitePal.countAsync(InfomationDao.class).listen(new CountCallback() {
            @Override
            public void onFinish(int count) {
                if(count<=0){
//                    querEncy();
                }
            }
        });


    }

    //查询医疗百科本地信息
    private void querEncy() {
        OkGo.<BaseBean<GetDownloadBean>>get(UrlUtil.getDb()).tag(this).execute(new JsonCallback<BaseBean<GetDownloadBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetDownloadBean>> response) {
                try{
                    downEncyDb(response.body().getData().getPath(),response.body().getData().getTableName());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void downEncyDb(String path, String tableName) {

    }


    /**
     *   获取医院风采信息并保存到数据库
     */
    public void getLately(){
        HttpParams params=new HttpParams();
        params.put("timeStamp",SPUtil.getLong(Constants.SP_ENCY_UPDATE_TIME));
        OkGo.<String>post(UrlUtil.getLately())
                .params(params)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogFactory.l().e(response.body());
                        Gson gson=new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.optInt("code") == 200) {
                                ExecutorService service=Executors.newSingleThreadExecutor();
                                String data = jsonObject.optString("data");
                                final EncyUpdateBean encyUpdateBean = gson.fromJson(data, EncyUpdateBean.class);
                                //更新科室
                                if(encyUpdateBean.getColumns()!=null && encyUpdateBean.getColumns().size()>0){
                                    service.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            LitePalDb.setZkysDb();
                                             for (InfoDao infoBean : encyUpdateBean.getColumns()){
                                                 InfoDao dao=LitePal.where("id = ?",infoBean.getId()+"").findFirst(InfoDao.class);
                                                 if(dao==null){
                                                     dao.setId(dao.getId());
                                                     dao.save();
                                                 }else {
                                                     LitePal.delete(InfoDao.class,dao.getId());
                                                     dao.setId(dao.getId());
                                                     dao.save();
                                                 }
                                             }
                                        }
                                   });
                                }
                                //更新第二张表
                                if(encyUpdateBean.getMes()!=null && encyUpdateBean.getMes().size()>0){
                                    service.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            LitePalDb.setZkysDb();
                                            for (InfomationDao infoBean : encyUpdateBean.getMes()){
                                                InfomationDao dao=LitePal.where("id = ?",infoBean.getId()+"").findFirst(InfomationDao.class);
                                                if(dao==null){
                                                    dao.setId(dao.getId());
                                                    dao.save();
                                                }else {
                                                    LitePal.delete(InfomationDao.class,dao.getId());
                                                    dao.setId(dao.getId());
                                                    dao.save();
                                                }
                                            }
                                        }
                                    });
                                }
                                SPUtil.putLong(Constants.SP_ENCY_UPDATE_TIME,System.currentTimeMillis()/1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

}
