package com.muju.note.launcher.app.home.bean;

import java.io.Serializable;
import java.util.List;

public class AdverNewBean implements Serializable {
    private List<AdvertsBean> adverts;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AdvertsBean> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<AdvertsBean> adverts) {
        this.adverts = adverts;
    }
}
