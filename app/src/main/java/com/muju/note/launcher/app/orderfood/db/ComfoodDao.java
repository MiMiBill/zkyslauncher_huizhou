package com.muju.note.launcher.app.orderfood.db;

import org.litepal.crud.LitePalSupport;

public class ComfoodDao extends LitePalSupport {
    private int id;
    private double price;
    private String images;
    private String name;
    private int foodid;
    private int tabid;

    public int getTabid() {
        return tabid;
    }

    public void setTabid(int tabid) {
        this.tabid = tabid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFoodid() {
        return foodid;
    }

    public void setFoodid(int foodid) {
        this.foodid = foodid;
    }
}
