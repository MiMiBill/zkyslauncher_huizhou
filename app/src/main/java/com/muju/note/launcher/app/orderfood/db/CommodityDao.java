package com.muju.note.launcher.app.orderfood.db;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;

public class CommodityDao extends LitePalSupport {
    private int id;
    private int sort;
    private String title;
    private int sellerId;
    private int commodid;
    private ArrayList<ComfoodDao> commodities;

    public int getCommodid() {
        return commodid;
    }

    public void setCommodid(int commodid) {
        this.commodid = commodid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public ArrayList<ComfoodDao> getCommodities() {
        return commodities;
    }

    public void setCommodities(ArrayList<ComfoodDao> commodities) {
        this.commodities = commodities;
    }
}
