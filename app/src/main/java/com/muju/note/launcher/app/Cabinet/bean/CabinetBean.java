package com.muju.note.launcher.app.Cabinet.bean;

import java.io.Serializable;

public class CabinetBean implements Serializable {

    /**
     * code : 2
     * data : {"agentId":25,"cabinetCode":"1000000178","channel":"木巨健康1","createTime":"2019-06-13
     * 16:10:15","deposit":1,"deptId":1,"deviceType":2,"expireFee":0,"expireTime":"2019-06-14
     * 16:10:15","hasLock":1,"hospitalId":4,"id":101776,"isConvention":1,"leaseTime":"2019-06-13
     * 16:10:15","mobile":"18439711214","nickname":"阿狸","noticeNum":0,"num":1,"payPrice":0.01,
     * "randomNumber":"1557884833728","returnTime":"1970-01-01 08:00:00","status":2,
     * "statusName":"使用中","termDesc":"测试套餐一","termId":265,"unit":2,"userId":10158}
     * msg : 存在订单
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

    public static class DataBean implements Serializable{
        /**
         * agentId : 25
         * cabinetCode : 1000000178
         * channel : 木巨健康1
         * createTime : 2019-06-13 16:10:15
         * deposit : 1
         * deptId : 1
         * deviceType : 2
         * expireFee : 0
         * expireTime : 2019-06-14 16:10:15
         * hasLock : 1
         * hospitalId : 4
         * id : 101776
         * isConvention : 1
         * leaseTime : 2019-06-13 16:10:15
         * mobile : 18439711214
         * nickname : 阿狸
         * noticeNum : 0
         * num : 1
         * payPrice : 0.01
         * randomNumber : 1557884833728
         * returnTime : 1970-01-01 08:00:00
         * status : 2
         * statusName : 使用中
         * termDesc : 测试套餐一
         * termId : 265
         * unit : 2
         * userId : 10158
         */

        private int agentId;
        private String cabinetCode;
        private String channel;
        private String createTime;
        private double deposit;
        private int deptId;
        private int deviceType;
        private int expireFee;
        private String expireTime;
        private int hasLock;
        private int hospitalId;
        private int id;
        private int isConvention;
        private String leaseTime;
        private String mobile;
        private String nickname;
        private int noticeNum;
        private int num;
        private double payPrice;
        private String randomNumber;
        private String returnTime;
        private int status;
        private String statusName;
        private String termDesc;
        private int termId;
        private int unit;
        private int userId;

        public int getAgentId() {
            return agentId;
        }

        public void setAgentId(int agentId) {
            this.agentId = agentId;
        }

        public String getCabinetCode() {
            return cabinetCode;
        }

        public void setCabinetCode(String cabinetCode) {
            this.cabinetCode = cabinetCode;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public double getDeposit() {
            return deposit;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public int getExpireFee() {
            return expireFee;
        }

        public void setExpireFee(int expireFee) {
            this.expireFee = expireFee;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getHasLock() {
            return hasLock;
        }

        public void setHasLock(int hasLock) {
            this.hasLock = hasLock;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsConvention() {
            return isConvention;
        }

        public void setIsConvention(int isConvention) {
            this.isConvention = isConvention;
        }

        public String getLeaseTime() {
            return leaseTime;
        }

        public void setLeaseTime(String leaseTime) {
            this.leaseTime = leaseTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getNoticeNum() {
            return noticeNum;
        }

        public void setNoticeNum(int noticeNum) {
            this.noticeNum = noticeNum;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(double payPrice) {
            this.payPrice = payPrice;
        }

        public String getRandomNumber() {
            return randomNumber;
        }

        public void setRandomNumber(String randomNumber) {
            this.randomNumber = randomNumber;
        }

        public String getReturnTime() {
            return returnTime;
        }

        public void setReturnTime(String returnTime) {
            this.returnTime = returnTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getTermDesc() {
            return termDesc;
        }

        public void setTermDesc(String termDesc) {
            this.termDesc = termDesc;
        }

        public int getTermId() {
            return termId;
        }

        public void setTermId(int termId) {
            this.termId = termId;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
