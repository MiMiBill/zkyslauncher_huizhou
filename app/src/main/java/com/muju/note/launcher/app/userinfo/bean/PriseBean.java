package com.muju.note.launcher.app.userinfo.bean;

import java.io.Serializable;
import java.util.List;

public class PriseBean implements Serializable {
    private List<PriseItemBean> adverts;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PriseItemBean> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<PriseItemBean> adverts) {
        this.adverts = adverts;
    }
}
