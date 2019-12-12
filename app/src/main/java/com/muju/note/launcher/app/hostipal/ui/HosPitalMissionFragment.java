package com.muju.note.launcher.app.hostipal.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.HostipalMienVideoAdapter;
import com.muju.note.launcher.app.hostipal.adapter.MissionAdapter;
import com.muju.note.launcher.app.hostipal.contract.HospitalMissionContract;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.hostipal.presenter.HospitalMissionPresenter;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.app.video.db.VideoHisDao;
import com.muju.note.launcher.app.video.db.VideoInfoDao;
import com.muju.note.launcher.app.video.ui.WotvPlayFragment;
import com.muju.note.launcher.app.video.util.WoTvUtil;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.ActiveUtils;
import com.muju.note.launcher.util.sdcard.SdcardConfig;
import com.unicom.common.VideoSdkConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import me.leefeng.promptlibrary.PromptDialog;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * 医院宣教界面
 */
public class HosPitalMissionFragment extends BaseFragment<HospitalMissionPresenter> implements
        View.OnClickListener, HospitalMissionContract.View {
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_null)
    TextView tvNull;
    @BindView(R.id.ll_null)
    LinearLayout llNull;
    @BindView(R.id.rv_his_mission)
    RecyclerView rvHisMission;
//    @BindView(R.id.rv_his_mission_video)
//    RecyclerView rvHisMissionVideo;
//    HostipalMienVideoAdapter hostipalMienVideoAdapter;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    private PromptDialog promptDialog;
    private int pageNum = 1;

    Unbinder unbinder;

    private List<MissionInfoDao> missionInfoDaos;
    private MissionAdapter missionAdapter;


//    private List<VideoInfoDao> videoInfoDaos;

    @Override
    public int getLayout() {
        return R.layout.fragment_his_mission;
    }

    @Override
    public void initData() {

        tvTitle.setText("医院宣教");
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

//        initHisMissionVideoRecyclerView();

        missionInfoDaos = new ArrayList<>();
        missionAdapter = new MissionAdapter(R.layout.rv_item_his_mission, missionInfoDaos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LauncherApplication
                .getContext(), 3);
        gridLayoutManager.offsetChildrenHorizontal(20);
        rvHisMission.setLayoutManager(gridLayoutManager);
        rvHisMission.setAdapter(missionAdapter);

        promptDialog = new PromptDialog(getActivity());
        promptDialog.showLoading("正在加载...");
        mPresenter.queryMiss();
        missionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MissionInfoDao bean = missionAdapter.getData().get(position);

                if (!TextUtils.isEmpty(bean.getCid()))
                {
                    toPlay(bean);
                    return;
                }


                if (TextUtils.isEmpty(bean.getVideo())) {
                    File file = new File(SdcardConfig.RESOURCE_FOLDER, bean.getFrontCover()
                            .hashCode() + ".pdf");
                    if (!file.exists()) {
                        showToast("正在下载中，请稍后重试");
                        MissionService.getInstance().downMission();
                        return;
                    }
                    start(HospitalMissionPdfFragment.newInstance(bean.getFrontCover(),bean.getMissionId(),bean.getTitle(),null));
                } else {
                    File file = new File(SdcardConfig.RESOURCE_FOLDER, bean.getVideo().hashCode()
                            + ".mp4");
                    if (!file.exists()) {
                        showToast("正在下载中，请稍后重试");
                        MissionService.getInstance().downMission();
                        return;
                    }
                    start(HospitalMissionVideoFragment.newInstance(bean.getVideo(),bean.getMissionId(),bean.getTitle(),null));
                }
            }
        });

    }

//    private void initHisMissionVideoRecyclerView() {
//
//        videoInfoDaos = new ArrayList<>();
//        hostipalMienVideoAdapter = new HostipalMienVideoAdapter(R.layout.rv_item_hospital_mien_video, videoInfoDaos);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(LauncherApplication.getContext(), 3);
//        gridLayoutManager.offsetChildrenHorizontal(20);
//        rvHisMissionVideo.setLayoutManager(gridLayoutManager);
//        rvHisMissionVideo.setAdapter(hostipalMienVideoAdapter);
//
//        hostipalMienVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                toPlay(videoInfoDaos.get(i));
//            }
//        });
//
////        mPresenter.getMissionVideo(  "" + ActiveUtils.getPadActiveInfo().getHospitalId() +"/医院风采",pageNum);
//    }

    /**
     * 跳转播放
     *
     * @param infoDao
     */
    private void toPlay(MissionInfoDao infoDao) {
        if (!VideoSdkConfig.getInstance().getUser().isLogined()) {
            WoTvUtil.getInstance().login();
            showToast("登入视频中，请稍后");
            return;
        }
        VideoHisDao hisDao = new VideoHisDao();
        hisDao.setCid(infoDao.getCid());
        hisDao.setCustomTag(infoDao.getCustomTag());
        hisDao.setDescription(infoDao.getDescription());
        hisDao.setImgUrl(infoDao.getImgUrl());
        hisDao.setName(infoDao.getName());
        hisDao.setVideoId(infoDao.getVideoId());
        hisDao.setVideoType(infoDao.getVideoType());
        hisDao.setScreenUrl(infoDao.getScreenUrl());
        WotvPlayFragment wotvPlayFragment = new WotvPlayFragment();
        wotvPlayFragment.setHisDao(hisDao);
        start(wotvPlayFragment, ISupportFragment.SINGLETASK);
    }


    @Override
    public void initPresenter() {
        mPresenter = new HospitalMissionPresenter();
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
            case R.id.tv_null:
                pop();
                break;
        }
    }

    @Override
    public void getMission(List<MissionInfoDao> list) {
        missionInfoDaos.addAll(list);
//        missionAdapter.notifyDataSetChanged();
        mPresenter.getMissionVideo( "" + ActiveUtils.getPadActiveInfo().getHospitalId() + "/" + ActiveUtils.getPadActiveInfo().getDeptId() + "/科室宣教",pageNum);
    }

    @Override
    public void getMissNull() {

        mPresenter.getMissionVideo( "" + ActiveUtils.getPadActiveInfo().getHospitalId() + "/" + ActiveUtils.getPadActiveInfo().getDeptId() + "/科室宣教",pageNum);
    }



    @Override
    public void getMissionVideoSuccess(List<VideoInfoDao> list) {
        promptDialog.dismiss();
//        smartRefresh.finishRefresh();
        missionAdapter.loadMoreComplete();
        for (VideoInfoDao videoInfoDao:list)
        {
            MissionInfoDao missionInfoDao = new MissionInfoDao();
            missionInfoDao.setCid(videoInfoDao.getCid());
            missionInfoDao.setCustomTag(videoInfoDao.getCustomTag());
            missionInfoDao.setDescription(videoInfoDao.getDescription());
            missionInfoDao.setImgUrl(videoInfoDao.getImgUrl());
            missionInfoDao.setName(videoInfoDao.getName());
            missionInfoDao.setVideoId(videoInfoDao.getVideoId());
            missionInfoDao.setVideoType(videoInfoDao.getVideoType());
            missionInfoDao.setScreenUrl(videoInfoDao.getScreenUrl());
            missionInfoDaos.add(missionInfoDao);
        }
        missionAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMissionVideoNull() {
        if (missionInfoDaos.size() <= 0)
        {
            llNull.setVisibility(View.VISIBLE);
        }
        promptDialog.dismiss();
    }
}
