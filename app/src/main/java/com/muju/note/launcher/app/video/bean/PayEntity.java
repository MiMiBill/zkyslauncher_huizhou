package com.muju.note.launcher.app.video.bean;

import java.io.Serializable;

/**
 * 支付的实体类，需要序列化到文件的
 */
public class PayEntity implements Serializable {
    public static final int ORDER_TYPE_VIDEO = 1;
    //视频已退费
    public static final int ORDER_TYPE_VIDEO_PREMIUM = 3;
    public static final int ORDER_TYPE_GAME = 2;
    public static final int PAY_TYPE_ZFB = 111;
    public static final int PAY_TYPE_WX = 222;
    String name;//订单名称
    String prepay_id;//订单号
    int days;//vip类型商品购买天数
    long paiedTime;//支付完成时间
    double total;//总金额
    int orderType;//订单类型
    int payType;//支付类型

    private int status;
    private String expireTime;


    public String getExpireTime() {
        return expireTime == null ? "" : expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public PayEntity(int orderType, String expireTime) {
        this.orderType = orderType;
        this.expireTime = expireTime;
    }

    public PayEntity(String name, String prepay_id, int days, long paiedTime, double total, int orderType, int payType) {
        this.name = name;
        this.prepay_id = prepay_id;
        this.days = days;
        this.paiedTime = paiedTime;
        this.total = total;
        this.orderType = orderType;
        this.payType = payType;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getPaiedTime() {
        return paiedTime;
    }

    public void setPaiedTime(long paiedTime) {
        this.paiedTime = paiedTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getTotal() {
        return total;
    }
}
