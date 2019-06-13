package com.muju.note.launcher.app.sign.bean;

import com.google.gson.annotations.SerializedName;
import com.muju.note.launcher.okgo.BaseBean;

import java.util.List;

public class TaskBean extends BaseBean {
        private List<AdvertsBean> adverts;
        private List<PointRecordsBean> pointRecords;

        public List<AdvertsBean> getAdverts() {
            return adverts;
        }

        public void setAdverts(List<AdvertsBean> adverts) {
            this.adverts = adverts;
        }

        public List<PointRecordsBean> getPointRecords() {
            return pointRecords;
        }

        public void setPointRecords(List<PointRecordsBean> pointRecords) {
            this.pointRecords = pointRecords;
        }

        public static class AdvertsBean {
            /**
             * advertOwner : 1
             * advertPositionId : 28
             * clickCount : 0
             * count : 1
             * createDate : 2019-06-09 13:50:46.0
             * endDate : 2019-07-31
             * id : 133
             * incomeType : 1
             * name : 看視頻的任務廣告
             * order : 1
             * pageNum : 1
             * pageSize : 20
             * period : 1
             * pointCode : GY1001
             * pointName : 观影券
             * pointType : 2
             * resourceUrl : http://qiniuimage.zgzkys.com/1560059290493.mp4
             * second : 20
             * showCount : 0
             * startDate : 2019-06-10
             * status : 1
             * taskType : 2
             * code : 1101
             */

            private String advertOwner;
            private int advertPositionId;
            private int clickCount;
            private int count;
            private String createDate;
            private String endDate;
            private int id;
            private int incomeType;
            private String name;
            private int order;
            private int pageNum;
            private int pageSize;
            private String period;
            private String pointCode;
            private String pointName;
            private int pointType;
            private String resourceUrl;
            private int second;
            private int showCount;
            private String startDate;
            private int status;
            private int taskType;
            @SerializedName("code")
            private String codeX;

            public String getAdvertOwner() {
                return advertOwner;
            }

            public void setAdvertOwner(String advertOwner) {
                this.advertOwner = advertOwner;
            }

            public int getAdvertPositionId() {
                return advertPositionId;
            }

            public void setAdvertPositionId(int advertPositionId) {
                this.advertPositionId = advertPositionId;
            }

            public int getClickCount() {
                return clickCount;
            }

            public void setClickCount(int clickCount) {
                this.clickCount = clickCount;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIncomeType() {
                return incomeType;
            }

            public void setIncomeType(int incomeType) {
                this.incomeType = incomeType;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
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

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public String getPointCode() {
                return pointCode;
            }

            public void setPointCode(String pointCode) {
                this.pointCode = pointCode;
            }

            public String getPointName() {
                return pointName;
            }

            public void setPointName(String pointName) {
                this.pointName = pointName;
            }

            public int getPointType() {
                return pointType;
            }

            public void setPointType(int pointType) {
                this.pointType = pointType;
            }

            public String getResourceUrl() {
                return resourceUrl;
            }

            public void setResourceUrl(String resourceUrl) {
                this.resourceUrl = resourceUrl;
            }

            public int getSecond() {
                return second;
            }

            public void setSecond(int second) {
                this.second = second;
            }

            public int getShowCount() {
                return showCount;
            }

            public void setShowCount(int showCount) {
                this.showCount = showCount;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getTaskType() {
                return taskType;
            }

            public void setTaskType(int taskType) {
                this.taskType = taskType;
            }

            public String getCodeX() {
                return codeX;
            }

            public void setCodeX(String codeX) {
                this.codeX = codeX;
            }
        }

        public static class PointRecordsBean {
            /**
             * code : JF1001
             * count : 10
             * id : 1
             * name : 积分
             * pageNum : 1
             * pageSize : 20
             * point : 1
             */

            @SerializedName("code")
            private String codeX;
            private int count;
            private int id;
            private String name;
            private int pageNum;
            private int pageSize;
            private int point;

            public String getCodeX() {
                return codeX;
            }

            public void setCodeX(String codeX) {
                this.codeX = codeX;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }
        }
}
