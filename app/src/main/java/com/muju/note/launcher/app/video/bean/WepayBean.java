package com.muju.note.launcher.app.video.bean;

import java.io.Serializable;

public class WepayBean implements Serializable {

    /**
     * code : 200
     * data : {"data":{"create_time":1562637067,"expire_time":1562723467,"lease_time":1562637067,
     * "status":2},"status":0}
     * msg : 操作成功！
     */

    private int code;
    private DataBeanX data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        /**
         * data : {"create_time":1562637067,"expire_time":1562723467,"lease_time":1562637067,
         * "status":2}
         * status : 0
         */

        private DataBean data;
        private int status;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class DataBean implements Serializable{
            /**
             * create_time : 1562637067
             * expire_time : 1562723467
             * lease_time : 1562637067
             * status : 2
             */

            private int create_time;
            private int expire_time;
            private int lease_time;
            private int status;

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(int expire_time) {
                this.expire_time = expire_time;
            }

            public int getLease_time() {
                return lease_time;
            }

            public void setLease_time(int lease_time) {
                this.lease_time = lease_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
