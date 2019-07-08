package com.muju.note.launcher.app.video.bean;

import java.io.Serializable;
import java.util.List;

public class PriceBean implements Serializable {

    /**
     * code : 200
     * data : [{"amount":1,"pageSize":20,"sydicId":25,"updateTime":1562318303000,"type":0,
     * "pageNum":1,"sortno":1,"unit":2,"createTime":1562318303000,"hospitalId":4,"price":3,
     * "disabled":1,"describe":"视频套餐","id":274,"foregift":1}]
     * msg : 操作成功！
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 1
         * pageSize : 20
         * sydicId : 25
         * updateTime : 1562318303000
         * type : 0
         * pageNum : 1
         * sortno : 1
         * unit : 2
         * createTime : 1562318303000
         * hospitalId : 4
         * price : 3
         * disabled : 1
         * describe : 视频套餐
         * id : 274
         * foregift : 1
         */

        private int amount;
        private int pageSize;
        private int sydicId;
        private long updateTime;
        private int type;
        private int pageNum;
        private int sortno;
        private int unit;
        private long createTime;
        private int hospitalId;
        private double price;
        private int disabled;
        private String describe;
        private int id;
        private int foregift;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSydicId() {
            return sydicId;
        }

        public void setSydicId(int sydicId) {
            this.sydicId = sydicId;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getSortno() {
            return sortno;
        }

        public void setSortno(int sortno) {
            this.sortno = sortno;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getForegift() {
            return foregift;
        }

        public void setForegift(int foregift) {
            this.foregift = foregift;
        }
    }
}
