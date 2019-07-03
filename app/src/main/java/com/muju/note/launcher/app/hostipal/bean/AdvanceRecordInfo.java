package com.muju.note.launcher.app.hostipal.bean;

/**
 * Created by Administrator on 2018/6/15.
 */

public class AdvanceRecordInfo {

    private String dateTime;//时间
    private String amount;//金额(小写)
    private String amountb;//金额(大写)
    private String payMethod;//支付方式
    private String cashier;//收款员

    public AdvanceRecordInfo(String dateTime, String amount, String amountb, String payMethod, String cashier) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.amountb = amountb;
        this.payMethod = payMethod;
        this.cashier = cashier;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountb() {
        return amountb;
    }

    public void setAmountb(String amountb) {
        this.amountb = amountb;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    @Override
    public String toString() {
        return "AdvanceRecordInfo{" +
                "dateTime='" + dateTime + '\'' +
                ", amount='" + amount + '\'' +
                ", amountb='" + amountb + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", cashier='" + cashier + '\'' +
                '}';
    }
}
