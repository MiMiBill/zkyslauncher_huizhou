package com.muju.note.launcher.app.setting.ui;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.muju.note.launcher.R;
import com.muju.note.launcher.base.LauncherApplication;


public class FragmentInner extends Fragment {
    public static final String KEY_TITLE="KEY_TITLE";
    public static final String KEY_IMG="KEY_IMG";
    public ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_inner,container,false);
        mImageView = (ImageView) rootView.findViewById(R.id.img1);
        return rootView;
    }

    public static FragmentInner newInstance(String title, @DrawableRes int res){
        Bundle bundle=new Bundle();
        bundle.putString(KEY_TITLE,title);
//        LogFactory.l().i("title==="+title);
        bundle.putInt(KEY_IMG,res);
        FragmentInner fragment=new FragmentInner();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
//        mImageView.setImageResource(arguments.getInt(KEY_IMG));
        Glide.with(LauncherApplication.getContext()).load(arguments.getInt(KEY_IMG)).into(mImageView);
    }
}
