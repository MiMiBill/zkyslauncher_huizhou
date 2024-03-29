package com.muju.note.launcher.view.banana;

public class                  BannerPage {
    public String url;//图片/视频地址
    public String videourl;//图片/视频地址
    public int imgDelyed = 10000;//图片轮播延时时间

    public BannerPage(){}

    /**
     * 构造函数
     * @param url String;//图片/视频地址
     * **/
    public BannerPage(String url)
    {
        this.url = url;
    }

    /**
     * 构造函数
     * @param url String;//图片/视频地址
     * @param imgDelyed int;//图片轮播延时时间
     * **/
    public BannerPage(String url, int imgDelyed)
    {
        this.url = url;
        this.imgDelyed = imgDelyed;
    }

    public BannerPage(String url,String videourl, int imgDelyed)
    {
        this.url = url;
        this.videourl=videourl;
        this.imgDelyed = imgDelyed;
    }
}
