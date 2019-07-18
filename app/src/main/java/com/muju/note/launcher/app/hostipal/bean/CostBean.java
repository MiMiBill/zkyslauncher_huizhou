package com.muju.note.launcher.app.hostipal.bean;

public class CostBean {

    private int pos;
    private String name;
    private String size;
    private int num;
    private String company;
    private String price;
    private String type;
    private String level;
    private String date;

    public CostBean() {
    }

    public CostBean(int pos, String name, String size, int num, String company, String price, String type, String level, String date) {
        this.pos = pos;
        this.name = name;
        this.size = size;
        this.num = num;
        this.company = company;
        this.price = price;
        this.type = type;
        this.level = level;
        this.date = date;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
