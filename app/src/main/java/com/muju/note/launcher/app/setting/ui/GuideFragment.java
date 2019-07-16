package com.muju.note.launcher.app.setting.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.video.ui.VideoContentFragment;
import com.muju.note.launcher.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.youngkaaa.yviewpager.YFragmentPagerAdapter;
import cn.youngkaaa.yviewpager.YViewPager;

public class GuideFragment extends BaseFragment {

    public static final String STATUS = "status";

    @BindView(R.id.yvp_guide)
    YViewPager yvpGuide;
    Unbinder unbinder;
    @BindView(R.id.img_close)
    ImageView imgClose;
    private List<FragmentInner> mFragments = new ArrayList<>();

    public static GuideFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt(STATUS, status);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_guide;
    }

    @Override
    public void initData() {
//        setData();

    }

    private void setData() {
        mFragments = new ArrayList<>();
        FragmentInner fragmentInner1 = FragmentInner.newInstance("fragment1", R.mipmap
                .img_novice_01);
        FragmentInner fragmentInner2 = FragmentInner.newInstance("fragment2", R.mipmap
                .img_novice_02);
        FragmentInner fragmentInner3 = FragmentInner.newInstance("fragment3", R.mipmap
                .img_novice_03);
        FragmentInner fragmentInner4 = FragmentInner.newInstance("fragment4", R.mipmap
                .img_novice_04);
        FragmentInner fragmentInner5 = FragmentInner.newInstance("fragment5", R.mipmap
                .img_novice_05);
        FragmentInner fragmentInner6 = FragmentInner.newInstance("fragment6", R.mipmap
                .img_novice_06);
        FragmentInner fragmentInner7 = FragmentInner.newInstance("fragment7", R.mipmap
                .img_novice_07);
        mFragments.add(fragmentInner1);
        mFragments.add(fragmentInner2);
        mFragments.add(fragmentInner3);
        mFragments.add(fragmentInner4);
        mFragments.add(fragmentInner5);
        mFragments.add(fragmentInner6);
        mFragments.add(fragmentInner7);

        yvpGuide.setAdapter(new FragmentAdapter(getChildFragmentManager()));

        if (getArguments().getInt(STATUS) == 1) {
            imgClose.setVisibility(View.VISIBLE);
        } else {
            imgClose.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        setData();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        pop();
    }


    class FragmentAdapter extends YFragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
