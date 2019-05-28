package com.muju.note.launcher.app.video.bean;

/**
 * 支付完成的事件类
 */
public class PayEvent {
    int orderType;

    public PayEvent(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
