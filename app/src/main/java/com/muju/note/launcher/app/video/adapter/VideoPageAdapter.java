package com.muju.note.launcher.app.video.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.muju.note.launcher.app.video.db.VideoColumnsDao;
import com.muju.note.launcher.app.video.ui.VideoContentFragment;

import java.util.List;

public class VideoPageAdapter extends FragmentPagerAdapter {

    private List<VideoColumnsDao> list;

    public VideoPageAdapter(FragmentManager fm,List<VideoColumnsDao> list) {
        super(fm);
        this.list=list;
    }


    @Override
    public Fragment getItem(int i) {
        return new VideoContentFragment().newInstance(list.get(i).getColumnsId(),list.get(i).getName());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }
}
