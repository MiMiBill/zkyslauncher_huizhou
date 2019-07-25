package com.muju.note.launcher.app.orderfood.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.muju.note.launcher.app.orderfood.db.CommodityDao;
import com.muju.note.launcher.app.orderfood.ui.FoodContentFragment;

import java.util.ArrayList;
import java.util.List;

public class FoodPageAdapter extends FragmentPagerAdapter {

    private List<CommodityDao> list = new ArrayList<>();

    public FoodPageAdapter(FragmentManager fm, List<CommodityDao> list) {
        super(fm);
        this.list = list;
    }


    @Override
    public Fragment getItem(int i) {
        return new FoodContentFragment().newInstance(list.get(i).getCommodid());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }
}
