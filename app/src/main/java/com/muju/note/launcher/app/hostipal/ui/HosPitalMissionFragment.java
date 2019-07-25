package com.muju.note.launcher.app.hostipal.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.hostipal.adapter.MissionAdapter;
import com.muju.note.launcher.app.hostipal.contract.HospitalMissionContract;
import com.muju.note.launcher.app.hostipal.db.MissionInfoDao;
import com.muju.note.launcher.app.hostipal.presenter.HospitalMissionPresenter;
import com.muju.note.launcher.app.hostipal.service.MissionService;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.base.LauncherApplication;
import com.muju.note.launcher.util.sdcard.SdcardConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;

    private List<MissionInfoDao> missionInfoDaos;
    private MissionAdapter missionAdapter;

    @Override
    public int getLayout() {
        return R.layout.fragment_his_mission;
    }

    @Override
    public void initData() {
        tvTitle.setText("医院宣教");
        llBack.setOnClickListener(this);
        tvNull.setOnClickListener(this);

        missionInfoDaos = new ArrayList<>();
        missionAdapter = new MissionAdapter(R.layout.rv_item_his_mission, missionInfoDaos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LauncherApplication
                .getContext(), 3);
        gridLayoutManager.offsetChildrenHorizontal(20);
        rvHisMission.setLayoutManager(gridLayoutManager);
        rvHisMission.setAdapter(missionAdapter);

        mPresenter.queryMiss();

        missionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MissionInfoDao bean = missionAdapter.getData().get(position);
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
        missionAdapter.notifyDataSetChanged();
    }

    @Override
    public void getMissNull() {
        llNull.setVisibility(View.VISIBLE);
    }
}
