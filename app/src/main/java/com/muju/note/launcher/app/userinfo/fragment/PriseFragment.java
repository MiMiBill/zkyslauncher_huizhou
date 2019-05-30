package com.muju.note.launcher.app.userinfo.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muju.note.launcher.R;
import com.muju.note.launcher.app.userinfo.bean.PriseItemBean;
import com.muju.note.launcher.base.BaseFragment;
import com.muju.note.launcher.util.DensityUtil;
import com.muju.note.launcher.util.VerticalLineDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PriseFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView myRecyclerview;
    Unbinder unbinder;


    @Override
    public int getLayout() {
        return R.layout.activity_prize;
    }

    @Override
    public void initData() {
        ArrayList<PriseItemBean> list=new ArrayList<>();
        PriseItemBean bean=new PriseItemBean();
        bean.setCreateTime("2019-05-14 11:08");
        bean.setTitle("创意水杯");
        bean.setPriseNum(2);
        bean.setImgRul("http://qiniuimage.zgzkys.com/0fatCat632×519.png");
        bean.setStatus(0);

        PriseItemBean priseItemBean=new PriseItemBean();
        priseItemBean.setCreateTime("2019-05-14 18:08");
        priseItemBean.setTitle("小懒猫真是胖,压垮炕");
        priseItemBean.setPriseNum(1);
        priseItemBean.setImgRul("http://qiniuimage.zgzkys.com/1水墨1128×501.png");
        priseItemBean.setStatus(1);

        list.add(bean);
        list.add(priseItemBean);
//        PriseAdapter adapter=new PriseAdapter(R.layout.item_prise,list,getActivity());
//        myRecyclerview.setAdapter(adapter);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        myRecyclerview.setLayoutManager(lin);
        myRecyclerview.setHasFixedSize(true);
        myRecyclerview.setFadingEdgeLength(0);
        myRecyclerview.addItemDecoration(new VerticalLineDecoration(DensityUtil.dip2px(getActivity(), 10),
                true));
        myRecyclerview.setFocusable(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showError(String msg) {

    }
}
