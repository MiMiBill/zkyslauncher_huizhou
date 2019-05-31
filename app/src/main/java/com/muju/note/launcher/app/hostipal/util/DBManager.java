package com.muju.note.launcher.app.hostipal.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.muju.note.launcher.app.hostipal.db.InfoDao;
import com.muju.note.launcher.app.hostipal.db.InfomationDao;
import com.muju.note.launcher.listener.OnQuerySuccessListener;
import com.muju.note.launcher.util.log.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private String DB_NAME = "medical.db";
    private Context mContext;
    /*选择题的集合*/
    public ArrayList<InfomationDao> mBeanLists = new ArrayList<InfomationDao>();
    public ArrayList<InfoDao> infoLists = new ArrayList<InfoDao>();
    public InfomationDao infomationBean;
    public OnQuerySuccessListener listener;
    public DBManager(Context mContext) {
        this.mContext = mContext;
    }

    //把assets目录下的db文件复制到dbpath下
    public SQLiteDatabase DBManager() {
        String dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "zkysdb/" + DB_NAME;

        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }


    //查询第二张表
    public List<InfomationDao> query(final SQLiteDatabase sqliteDB, final int VALUE_ID) {
        try {
            final String table = "medical_encyclopedia";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = sqliteDB.rawQuery("select * from " + table + " where columnId=" +
                            VALUE_ID, null);
                    while (cursor.moveToNext()) {
                        InfomationDao bean = setInfomationBean(cursor);
                        mBeanLists.add(bean);
                    }
                    cursor.close();
                }
            }).run();

            return mBeanLists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //搜索数据
    public ArrayList<InfomationDao> querySearch(final SQLiteDatabase sqliteDB, final String
            VALUE) {
        try {
            final String table = "medical_encyclopedia";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = sqliteDB.rawQuery("select * from " + table + " where title " +
                            "like " + "\'" + "%" + VALUE + "%" + "\'" + " limit 40", null);
                    while (cursor.moveToNext()) {
                        InfomationDao bean = setInfomationBean(cursor);
                        mBeanLists.add(bean);
                    }
                    cursor.close();
                }
            }).run();
//            LogFactory.l().i(System.currentTimeMillis());
            return mBeanLists;
        } catch (Exception e) {
            LogUtil.i("e==" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //设置javabean
    private InfomationDao setInfomationBean(Cursor cursor) {
//        InfomationDao bean=new InfomationDao();
        String title = cursor.getString(cursor.getColumnIndex("title"));
        String source = cursor.getString(cursor.getColumnIndex("source"));
        String summary = cursor.getString(cursor.getColumnIndex("summary"));
        String cause = cursor.getString(cursor.getColumnIndex("cause"));
        String check = cursor.getString(cursor.getColumnIndex("check"));
        String diacrsis = cursor.getString(cursor.getColumnIndex("diacrisis"));
        String antidiastole = cursor.getString(cursor.getColumnIndex("antidiastole"));
        String cure = cursor.getString(cursor.getColumnIndex("cure"));
        String prognosis = cursor.getString(cursor.getColumnIndex("prognosis"));
        String prophylaxis = cursor.getString(cursor.getColumnIndex("prophylaxis"));
        String complicatingDisease = cursor.getString(cursor.getColumnIndex("complicatingDisease"));
        String author = cursor.getString(cursor.getColumnIndex("author"));
        int columnid = cursor.getInt(cursor.getColumnIndex("columnId"));
        String classification = cursor.getString(cursor.getColumnIndex("classification"));
        String clinicalManifestation = cursor.getString(cursor.getColumnIndex
                ("clinicalManifestation"));
        String dietCare = cursor.getString(cursor.getColumnIndex("dietCare"));
        String tag = cursor.getString(cursor.getColumnIndex("tag"));
        int clickCount = cursor.getInt(cursor.getColumnIndex("clickCount"));
        InfomationDao bean = new InfomationDao();
        if (null == title) {
            title = "";
        }
        if (null == source) {
            source = "";
        }
        if (null == summary) {
            summary = "";
        }
        if (null == cause) {
            cause = "";
        }
        if (null == check) {
            check = "";
        }
        if (null == diacrsis) {
            diacrsis = "";
        }
        if (null == antidiastole) {
            antidiastole = "";
        }
        if (null == cure) {
            cure = "";
        }
        if (null == prognosis) {
            prognosis = "";
        }
        if (null == prophylaxis) {
            prophylaxis = "";
        }
        if (null == complicatingDisease) {
            complicatingDisease = "";
        }
        if (null == author) {
            author = "";
        }
        if (null == classification) {
            classification = "";
        }
        if (null == clinicalManifestation) {
            clinicalManifestation = "";
        }
        if (null == dietCare) {
            dietCare = "";
        }
        if (null == tag) {
            tag = "";
        }
        bean.setTitle(title);
        bean.setSource(source);
        bean.setSummary(summary);
        bean.setCause(cause);
        bean.setCheck(check);
        bean.setDiacrsis(diacrsis);
        bean.setAntidiastole(antidiastole);
        bean.setCure(cure);
        bean.setPrognosis(prognosis);
        bean.setProphylaxis(prophylaxis);
        bean.setComplicatingDisease(complicatingDisease);
        bean.setAuthor(author);
        bean.setColumnid(columnid);
        bean.setClickCount(clickCount);
        bean.setTag(tag);
        bean.setDassification(classification);
        bean.setClinicalManifestation(clinicalManifestation);
        bean.setDietCare(dietCare);
        return bean;
    }


    //获取第一条数据
    public InfomationDao queryTop(final SQLiteDatabase sqliteDB) {
        try {
            final String table = "medical_encyclopedia";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = sqliteDB.rawQuery("select * from " + table + " limit 1", null);
                    while (cursor.moveToNext()) {
                        infomationBean = setInfomationBean(cursor);
                    }
                    cursor.close();
                    if(listener!=null){
                        listener.OnQuerySuccess();
                    }
                    LogUtil.i(""+System.currentTimeMillis());
                }
            }).run();
            return infomationBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据title获取数据
    public InfomationDao queryTitle(final SQLiteDatabase sqliteDB, final String title) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String table = "medical_encyclopedia";
                    Cursor cursor = sqliteDB.rawQuery("select * from " + table + " where title=" + "\'" +
                            title + "\'", null);
                    while (cursor.moveToNext()) {
                        infomationBean = setInfomationBean(cursor);
                    }
                    cursor.close();
                    LogUtil.i(""+System.currentTimeMillis());
                }
            }).run();
            return infomationBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //查询第一张表所有科室名称
    public List<InfoDao> queryNameList(final SQLiteDatabase sqliteDB, final String[] columns, final String selection, final String[] selectionArgs) {
        try {
            final String table = "medical_column";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Cursor cursor = sqliteDB.query(table, columns, selection, selectionArgs,
                            null, null,
                            null);
                    LogUtil.i(""+System.currentTimeMillis());
                    while (cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex("name"));
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        InfoDao bean = new InfoDao();
                        bean.setName(title);
                        bean.setId(id);
                        infoLists.add(bean);
                    }
                    cursor.close();
                }
            }).run();
            return infoLists;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据id改name
    public void updateNameList(SQLiteDatabase sqliteDB, InfoDao bean) {
        String table = "medical_column";
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("id", bean.getId());
        sqliteDB.update(table, values, "name = ? where id = ?", new String[]{bean.getName(), bean
                .getId() + ""});
    }
}
