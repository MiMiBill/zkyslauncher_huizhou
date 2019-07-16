package com.muju.note.launcher.app.hostipal.bean;

/**
 * Created by Administrator on 2018/6/14.
 * 对于表格信息
 */

public class CostInfo {
    /**
     * entryName.setText(mList.get(position).getEnteyName());
     * number.setText(mList.get(position).getNumber());
     * unit.setText(mList.get(position).getUnit());
     * unitPrice.setText(mList.get(position).getUnitPrice());
     * totlePrice.setText(mList.get(position).getTotlePrice());
     * dateTime.setText(mList.get(position).getDateTime());
     */

    private String entryName;
    private String number;
    private String unit;
    private String unitPrice;
    private String totlePrice;
    private String dateTime;

    public CostInfo(String entryName, String number, String unit, String unitPrice, String totlePrice, String dateTime) {
        this.entryName = entryName;
        this.number = number;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.totlePrice = totlePrice;
        this.dateTime = dateTime;
    }


    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotlePrice() {
        return totlePrice;
    }

    public void setTotlePrice(String totlePrice) {
        this.totlePrice = totlePrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
