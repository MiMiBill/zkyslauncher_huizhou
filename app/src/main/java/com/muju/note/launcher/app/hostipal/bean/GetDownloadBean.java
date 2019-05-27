package com.muju.note.launcher.app.hostipal.bean;

import com.muju.note.launcher.okgo.BaseBean;

public class GetDownloadBean extends BaseBean {


        private String path;
        private String remark;
        private int id;
        private String createDate;
        private String tableName;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }


}
