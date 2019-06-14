package com.muju.note.launcher.app.sign.bean;

import java.util.List;

public class PriseBean {


    private List<GiftListBean> giftList;
    private List<PointListBean> pointList;

    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }

    public List<PointListBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointListBean> pointList) {
        this.pointList = pointList;
    }

    public static class GiftListBean {
        /**
         * code : JF1001
         * count : 20
         * name : 积分
         * pageSize : 20
         * id : 1
         * pageNum : 1
         * point : 1
         */

        private String code;
        private int count;
        private String name;
        private int pageSize;
        private int id;
        private int pageNum;
        private int point;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }
    }

    public static class PointListBean {
        /**
         * code : GY1001
         * count : 0
         * name : 观影券
         * pageSize : 20
         * id : 2
         * pageNum : 1
         * point : 50
         */

        private String code;
        private int count;
        private String name;
        private int pageSize;
        private int id;
        private int pageNum;
        private int point;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }
    }

}
