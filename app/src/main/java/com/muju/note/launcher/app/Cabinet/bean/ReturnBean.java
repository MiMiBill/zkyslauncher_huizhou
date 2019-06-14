package com.muju.note.launcher.app.Cabinet.bean;

public class ReturnBean {

    /**
     * code : 200
     * data : {"expireDay":0,"isExpire":0,"pageNum":1,"pageSize":20}
     * msg : 操作成功！
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * expireDay : 0
         * isExpire : 0
         * pageNum : 1
         * pageSize : 20
         */

        private int expireDay;
        private int isExpire;
        private int pageNum;
        private int pageSize;

        public int getExpireDay() {
            return expireDay;
        }

        public void setExpireDay(int expireDay) {
            this.expireDay = expireDay;
        }

        public int getIsExpire() {
            return isExpire;
        }

        public void setIsExpire(int isExpire) {
            this.isExpire = isExpire;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
