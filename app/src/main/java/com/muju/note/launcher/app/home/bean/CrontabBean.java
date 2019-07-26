package com.muju.note.launcher.app.home.bean;

import java.io.Serializable;
import java.util.List;

public class CrontabBean  {

    /**
     * code : 200
     * current : 1
     * data : [{"addType":1,"age":"29","bedId":113,"bedNumber":"006","deptName":"内科",
     * "execTime":"06:00","fileAddr":"http://qiniuhospital.zgzkys
     * .com/lvm0wjfhtJInDu64KBquo0U3EIEN","fileType":"frontCover","id":1,"sex":"1",
     * "startDate":"2019-07-20","stopDate":"2019-08-12","userName":"周瑜"},{"addType":0,"age":"29",
     * "bedId":113,"bedNumber":"006","deptName":"内科","execTime":"13:45",
     * "fileAddr":"http://qiniuhospital.zgzkys.com/ltQgba0bAyWksNdMb5EAR4wi8pGa",
     * "fileType":"frontCover","id":21,"sex":"1","startDate":"2019-07-20",
     * "stopDate":"2019-08-31","userName":"周瑜"},{"addType":2,"age":"29","bedId":113,
     * "bedNumber":"006","deptName":"内科","execTime":"20:00","fileAddr":"http://qiniuhospital
     * .zgzkys.com/lvm0wjfhtJInDu64KBquo0U3EIEN","fileType":"frontCover","id":2,"sex":"1",
     * "startDate":"2019-07-22","stopDate":"2019-08-12","userName":"周瑜","weeks":"1,2,3"},
     * {"addType":2,"age":"29","bedId":113,"bedNumber":"006","deptName":"内科","execTime":"03:30",
     * "fileAddr":"http://qiniuhospital.zgzkys.com/lvm0wjfhtJInDu64KBquo0U3EIEN",
     * "fileType":"frontCover","id":3,"sex":"1","startDate":"2019-07-23","stopDate":"2019-08-15",
     * "userName":"周瑜","weeks":"1,2"}]
     * msg : 操作成功！
     * pages : 1
     * size : 20
     * total : 4
     */

    private int code;
    private int current;
    private String msg;
    private int pages;
    private int size;
    private int total;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * addType : 1
         * age : 29
         * bedId : 113
         * bedNumber : 006
         * deptName : 内科
         * execTime : 06:00
         * fileAddr : http://qiniuhospital.zgzkys.com/lvm0wjfhtJInDu64KBquo0U3EIEN
         * fileType : frontCover
         * id : 1
         * sex : 1
         * startDate : 2019-07-20
         * stopDate : 2019-08-12
         * userName : 周瑜
         * weeks : 1,2,3
         */

        private int addType;
        private String age;
        private int bedId;
        private String bedNumber;
        private String deptName;
        private String execTime;
        private String fileAddr;
        private String fileType;
        private int id;
        private String sex;
        private String startDate;
        private String stopDate;
        private String userName;
        private String weeks;
        private int missionId;

        public int getMissionId() {
            return missionId;
        }

        public void setMissionId(int missionId) {
            this.missionId = missionId;
        }

        public int getAddType() {
            return addType;
        }

        public void setAddType(int addType) {
            this.addType = addType;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
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

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getExecTime() {
            return execTime;
        }

        public void setExecTime(String execTime) {
            this.execTime = execTime;
        }

        public String getFileAddr() {
            return fileAddr;
        }

        public void setFileAddr(String fileAddr) {
            this.fileAddr = fileAddr;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStopDate() {
            return stopDate;
        }

        public void setStopDate(String stopDate) {
            this.stopDate = stopDate;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getWeeks() {
            return weeks;
        }

        public void setWeeks(String weeks) {
            this.weeks = weeks;
        }
    }
}
