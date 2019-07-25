package com.muju.note.launcher.app.orderfood.bean;

public class PayInfoBean  {
    /**
     * code : 200
     * data : {"qrCode":"weixin://wxpay/bizpayurl?pr=JgZGP4i","orderId":418,
     * "prepay_id":"wx24175753208168132650eaef1735696600"}
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
         * qrCode : weixin://wxpay/bizpayurl?pr=JgZGP4i
         * orderId : 418
         * prepay_id : wx24175753208168132650eaef1735696600
         */

        private String qrCode;
        private int orderId;
        private String prepay_id;

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }
    }
    /**
         * qrCode : weixin://wxpay/bizpayurl?pr=7Ps82hm
         * orderId : 417
         * prepay_id : wx24164739576581b8a219f1341653885000
         */


}
