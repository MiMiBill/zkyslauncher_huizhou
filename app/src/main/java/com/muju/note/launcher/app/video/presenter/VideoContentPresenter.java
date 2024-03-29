package com.muju.note.launcher.app.video.presenter;

import android.text.TextUtils;

import com.muju.note.launcher.app.video.contract.VideoContentContract;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.db.VideoInfoTopDao;
import com.muju.note.launcher.app.video.db.VideoTagSubDao;
import com.muju.note.launcher.app.video.db.VideoTagsDao;
import com.muju.note.launcher.base.BasePresenter;
import com.muju.note.launcher.litepal.LitePalDb;
import com.muju.note.launcher.util.log.LogUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

public class VideoContentPresenter extends BasePresenter<VideoContentContract.View> implements VideoContentContract.Presenter {

    /**
     * 查询影视列表
     *
     * @param columnId
     * @param name
     * @param pageNum
     */
    @Override
    public void queryVideo(int columnId, String name, int pageNum) {
        queryVideo(columnId, name, pageNum, "");
    }

    /**
     * 根据标签查询影视列表
     *
     * @param columnId
     * @param name
     * @param pageNum
     * @param mapFilter
     */
    @Override
    public void queryVideo(int columnId, String name, int pageNum, String mapFilter) {
        try {
            LitePalDb.setZkysDb();
            if (columnId == 1) {
                LitePal.findAllAsync(VideoInfoTopDao.class).listen(new FindMultiCallback<VideoInfoTopDao>() {
                    @Override
                    public void onFinish(List<VideoInfoTopDao> list) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        if (list == null || list.size() <= 0) {
                            mView.getVideoNull();
                            return;
                        }
                        List<VideoInfoDao> topDaos = new ArrayList<>();
                        for (VideoInfoTopDao dao : list) {
                            VideoInfoDao infoDao = new VideoInfoDao();
                            infoDao.setImgUrl(dao.getImgUrl());
                            infoDao.setColumnId(dao.getColumnId());
                            infoDao.setVideoType(dao.getVideoType());
                            infoDao.setScreenUrl(dao.getScreenUrl());
                            infoDao.setName(dao.getName());
                            infoDao.setDescription(dao.getDescription());
                            infoDao.setEditTime(dao.getEditTime());
                            infoDao.setCid(dao.getCid());
                            infoDao.setWatchCount(dao.getWatchCount());
                            topDaos.add(infoDao);
                        }
                        mView.getVideoSuccess(topDaos);
                        if (list.size() > 0 && list.size() < 30) {
                            mView.getVideoend();
                        }
                    }
                });
            } else {
                String sql;
                int limit = (pageNum * 30) - 30;
                if (TextUtils.isEmpty(mapFilter) || "".equals(mapFilter)) {
                    sql = "columnName like '%" + name + "%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
                } else {
                    sql = "columnName like '%" + name + "%' and status = 1 and customtag like '%" + mapFilter + "%' order by number desc,onwayTime desc,editTime desc,updateTime desc";
                }
                LitePal.where(sql).limit(30).offset(limit).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
                    @Override
                    public void onFinish(List<VideoInfoDao> list) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        if (list == null || list.size() <= 0) {
                            mView.getVideoNull();
                            return;
                        }
                        mView.getVideoSuccess(list);
                        if (list.size() > 0 && list.size() < 30) {
                            mView.getVideoend();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            LitePalDb.setZkysDb();
            if (columnId == 1) {
                LitePal.findAllAsync(VideoInfoTopDao.class).listen(new FindMultiCallback<VideoInfoTopDao>() {

                    @Override
                    public void onFinish(List<VideoInfoTopDao> list) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        if (list == null || list.size() <= 0) {
                            mView.getVideoNull();
                            return;
                        }
                        List<VideoInfoDao> topDaos = new ArrayList<>();
                        for (VideoInfoTopDao dao : list) {
                            VideoInfoDao infoDao = new VideoInfoDao();
                            infoDao.setImgUrl(dao.getImgUrl());
                            infoDao.setColumnId(dao.getColumnId());
                            infoDao.setVideoType(dao.getVideoType());
                            infoDao.setScreenUrl(dao.getScreenUrl());
                            infoDao.setName(dao.getName());
                            infoDao.setDescription(dao.getDescription());
                            infoDao.setEditTime(dao.getEditTime());
                            infoDao.setCid(dao.getCid());
                            infoDao.setWatchCount(dao.getWatchCount());
                            topDaos.add(infoDao);
                        }
                        mView.getVideoSuccess(topDaos);
                        if (list.size() > 0 && list.size() < 30) {
                            mView.getVideoend();
                        }
                    }
                });
            } else {
                String sql;
                int limit = (pageNum * 30) - 30;
                if (TextUtils.isEmpty(mapFilter) || "".equals(mapFilter)) {
                    sql = "columnName like '%" + name + "%' and status = 1 order by number desc,onwayTime desc,editTime desc,updateTime desc";
                } else {
                    sql = "columnName like '%" + name + "%' and status = 1 and customtag like '%" + mapFilter + "%' order by number desc,onwayTime desc,editTime desc,updateTime desc";
                }
                LitePalDb.setZkysDb();
                LitePal.where(sql).limit(30).offset(limit).findAsync(VideoInfoDao.class).listen(new FindMultiCallback<VideoInfoDao>() {
                    @Override
                    public void onFinish(List<VideoInfoDao> list) {
                        if (mView == null) {
                            LogUtil.e("mView为空");
                            return;
                        }
                        if (list == null || list.size() <= 0) {
                            mView.getVideoNull();
                            return;
                        }
                        mView.getVideoSuccess(list);
                        if (list.size() > 0 && list.size() < 30) {
                            mView.getVideoend();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void queryFilter(final int columnId) {
        LitePalDb.setZkysDb();
        LitePal.where("coulmnsId = ?", columnId + "").findAsync(VideoTagsDao.class, true).listen(new FindMultiCallback<VideoTagsDao>() {
            @Override
            public void onFinish(List<VideoTagsDao> list) {
                if (mView == null) {
                    LogUtil.e("mView为空");
                    return;
                }
                for (VideoTagsDao tagsDao : list) {
                    if (tagsDao.getList() != null) {
                        tagsDao.getList().add(0, new VideoTagSubDao("全部", true));
                    }
                }
                mView.getFilter(list);
            }
        });
    }
}
