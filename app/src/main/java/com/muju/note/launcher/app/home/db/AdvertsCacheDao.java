package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

public class AdvertsCacheDao extends LitePalSupport {

    /**
     * imei
     */
    private String resourceUrl; //图片或者视频地址url

    private byte[] imgdata;

    /**
     * 广告ID
     */
    private int advertId;

    /*
     *  广告code
     * */

    private String adverCode;

    /*
     *  广告分类,视频还是图片
     * */

    public byte[] getImgdata() {
        return imgdata;
    }

    public void setImgdata(byte[] imgdata) {
        this.imgdata = imgdata;
    }

    private int adverType;

    public int getAdverType() {
        return adverType;
    }

    public void setAdverType(int adverType) {
        this.adverType = adverType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getAdvertId() {
        return advertId;
    }

    public void setAdvertId(int advertId) {
        this.advertId = advertId;
    }


    public String getAdverCode() {
        return adverCode;
    }

    public void setAdverCode(String adverCode) {
        this.adverCode = adverCode;
    }
}
