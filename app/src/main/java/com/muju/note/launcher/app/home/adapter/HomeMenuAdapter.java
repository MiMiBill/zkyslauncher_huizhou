package com.muju.note.launcher.app.home.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.muju.note.launcher.R;
import com.muju.note.launcher.app.home.db.HomeMenuDao;
import com.muju.note.launcher.util.gilde.GlideUtil;

import java.util.List;

public class HomeMenuAdapter extends BaseQuickAdapter<HomeMenuDao, BaseViewHolder> {

    public HomeMenuAdapter(int layoutResId, @Nullable List<HomeMenuDao> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, HomeMenuDao dao) {
        ImageView img=baseViewHolder.getView(R.id.iv_menu);
        switch (dao.getTab()){
            case "风采":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_hositpal);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"医院风采");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "宣教":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_his_mission);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"医院宣教");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "百科":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_his_ency);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"医疗百科");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "柜子":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_lock);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"屏安柜");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "影视":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_video);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"海量影视");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "用户":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_setting);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"个人中心");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "游戏":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.icon_home_game);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"游戏娱乐");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "直播":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_video_line);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"电视直播");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "购物":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_shop);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"严选好物");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "金融":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_finance);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"金融财富");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "保险":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_insureance);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"保险服务");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "点餐":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_order);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"点餐服务");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "通知":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_msg);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"通知中心");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "新手":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.home_guide);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"新手引导");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "设置":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.hos_setting);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"系统设置");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "儿童":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_child);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"儿童专栏");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;

            case "健康":
                if(TextUtils.isEmpty(dao.getIcon())){
                    img.setImageResource(R.mipmap.ic_home_item_healthy);
                }else {
                    GlideUtil.loadImg(dao.getIcon(),img,R.mipmap.ic_video_load_default);
                }
                if(TextUtils.isEmpty(dao.getName())){
                    baseViewHolder.setText(R.id.tv_menu,"健康咨询");
                }else {
                    baseViewHolder.setText(R.id.tv_menu,dao.getName());
                }
                break;
        }
    }
}
