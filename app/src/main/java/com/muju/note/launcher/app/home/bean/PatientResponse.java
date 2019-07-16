package com.muju.note.launcher.app.home.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class PatientResponse implements Serializable {


    /**
     * code : 200
     * data : [{"deptName":"手足外科","sex":2,"deptId":1,"pageSize":20,"userName":"旺达吃","pageNum":1,"chargeNurse":"护士","number":"1005","createTabbDate":"2018-08-30","jpushRegistid":"1a0018970af3832b1cd","teamId":57,"chargeDoctor":"医生","dietCategory":"少渣饮食","padName":"Pad_1005","attention":"注意事项","padId":139,"disabled":1,"pushMobile":"13333333333","headNurse":"护士长","id":104,"nursingLevel":"一级护理","age":25,"tabbId":108},{"deptName":"手足外科","deptId":1,"pageSize":20,"pageNum":1,"number":"1005","jpushRegistid":"1a0018970af3832b1cd","teamId":57,"padName":"Pad_1005","padId":139,"disabled":1,"id":103,"tabbId":0}]
     * msg : 操作成功！
     * total : 2
     */

    private int code;
    private String msg;

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * deptName : 手足外科
         * sex : 2
         * deptId : 1
         * pageSize : 20
         * userName : 旺达吃
         * pageNum : 1
         * chargeNurse : 护士
         * number : 1005
         * createTabbDate : 2018-08-30
         * jpushRegistid : 1a0018970af3832b1cd
         * teamId : 57
         * chargeDoctor : 医生
         * dietCategory : 少渣饮食
         * padName : Pad_1005
         * attention : 注意事项
         * padId : 139
         * disabled : 1
         * pushMobile : 13333333333
         * headNurse : 护士长
         * id : 104
         * nursingLevel : 一级护理
         * age : 25
         * tabbId : 108
         */
        private int workerId;
        private int bedId;
        private String bedNumber;
        private String hospitalizationNumber;
        private String deptName;
        private int sex;
        private int deptId;
        private int pageSize;
        private String userName;
        private int pageNum;
        private String chargeNurse;
        private String number;
        //        private String createTabbDate;
        private String jpushRegistid;
        private int teamId;
        private String chargeDoctor;
        private String dietCategory;
        private String padName;
        private String attention;
        private int padId;
        private int disabled;
        private String pushMobile;
        private String headNurse;
        private int id;
        private String nursingLevel;
        private int age;
        private String createTime;


        public int getWorkerId() {
            return workerId;
        }

        public void setWorkerId(int workerId) {
            this.workerId = workerId;
        }

        public int getBedId() {
            return bedId;
        }

        public void setBedId(int bedId) {
            this.bedId = bedId;
        }

        public String getBedNumber() {
            return bedNumber;
        }

        public void setBedNumber(String bedNumber) {
            this.bedNumber = bedNumber;
        }

        public String getHospitalizationNumber() {
            return hospitalizationNumber;
        }

        public void setHospitalizationNumber(String hospitalizationNumber) {
            this.hospitalizationNumber = hospitalizationNumber;
        }

        public String getCreateTime() {
            return createTime == null ? "" : createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }


        //        private int tabbId;

        public DataBean(int disabled) {
            this.disabled = disabled;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public String getChargeNurse() {
            if (TextUtils.isEmpty(chargeNurse)) {
                return "";
            }
            return chargeNurse;
        }

        public void setChargeNurse(String chargeNurse) {
            this.chargeNurse = chargeNurse;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        /*public String getCreateTabbDate() {
            if (TextUtils.isEmpty(createTabbDate)) {
                return "";
            }
            return createTabbDate;
        }

        public void setCreateTabbDate(String createTabbDate) {
            this.createTabbDate = createTabbDate;
        }*/

        public String getJpushRegistid() {
            return jpushRegistid;
        }

        public void setJpushRegistid(String jpushRegistid) {
            this.jpushRegistid = jpushRegistid;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public String getChargeDoctor() {
            if (TextUtils.isEmpty(chargeDoctor)) {
                return "";
            }
            return chargeDoctor;
        }

        public void setChargeDoctor(String chargeDoctor) {
            this.chargeDoctor = chargeDoctor;
        }

        public String getDietCategory() {
            if (TextUtils.isEmpty(dietCategory)) {
                return "";
            }
            return dietCategory;
        }

        public void setDietCategory(String dietCategory) {
            this.dietCategory = dietCategory;
        }

        public String getPadName() {
            return padName;
        }

        public void setPadName(String padName) {
            this.padName = padName;
        }

        public String getAttention() {
            return attention;
        }

        public void setAttention(String attention) {
            this.attention = attention;
        }

        public int getPadId() {
            return padId;
        }

        public void setPadId(int padId) {
            this.padId = padId;
        }

        public boolean getDisabled() {
            return disabled == 1;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public String getPushMobile() {
            return pushMobile;
        }

        public void setPushMobile(String pushMobile) {
            this.pushMobile = pushMobile;
        }

        public String getHeadNurse() {
            return headNurse;
        }

        public void setHeadNurse(String headNurse) {
            this.headNurse = headNurse;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNursingLevel() {
            if (TextUtils.isEmpty(nursingLevel)) {
                return "";
            }
            return nursingLevel;
        }

        public void setNursingLevel(String nursingLevel) {
            this.nursingLevel = nursingLevel;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

       /* public int getTabbId() {
            return tabbId;
        }

        public void setTabbId(int tabbId) {
            this.tabbId = tabbId;
        }*/
    }
}
