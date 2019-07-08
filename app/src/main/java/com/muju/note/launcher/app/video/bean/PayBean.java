package com.muju.note.launcher.app.video.bean;

import java.io.Serializable;

public class PayBean implements Serializable {

    /**
     * code : 200
     * data : {"qrCode":"weixin://wxpay/bizpayurl?pr=41DUbQD",
     * "prepay_id":"wx0815233207573361cc1657461655592200"}
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
         * qrCode : weixin://wxpay/bizpayurl?pr=41DUbQD
         * prepay_id : wx0815233207573361cc1657461655592200
         */

        private String qrCode;
        private String prepay_id;

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }
    }
}
