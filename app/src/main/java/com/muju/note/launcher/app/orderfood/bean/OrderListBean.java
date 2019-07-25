package com.muju.note.launcher.app.orderfood.bean;

import java.io.Serializable;
import java.util.List;

public class OrderListBean implements Serializable {

    /**
     * code : 200
     * current : 1
     * data : [{"deptName":"内科","bedId":119,"mobile":"13636655421","sellerName":"积香小厨",
     * "hospitalName":"东莞大朗医院","totalAmount":0.01,"payType":3,"sellerId":1,"name":"张三","id":438,
     * "bedNumber":"699","items":[{"commodity":{"images":"http://qiniuimage.zgzkys
     * .com/1563874659804.jpg","stockpileCount":0,"price":0.01,"workOffCount":0,"name":"奶茶",
     * "id":10},"orderId":438,"count":1,"commodityId":10,"id":114}],"status":1}]
     * msg : 操作成功！
     * pages : 1
     * size : 100
     * total : 1
     */

    /**
     * deptName : 内科
     * bedId : 119
     * mobile : 13636655421
     * sellerName : 积香小厨
     * hospitalName : 东莞大朗医院
     * totalAmount : 0.01
     * payType : 3
     * sellerId : 1
     * name : 张三
     * id : 438
     * bedNumber : 699
     * items : [{"commodity":{"images":"http://qiniuimage.zgzkys.com/1563874659804.jpg",
     * "stockpileCount":0,"price":0.01,"workOffCount":0,"name":"奶茶","id":10},"orderId":438,
     * "count":1,"commodityId":10,"id":114}]
     * status : 1
     */

    private String deptName;
    private int bedId;
    private String mobile;
    private String sellerName;
    private String hospitalName;
    private double totalAmount;
    private int payType;
    private int sellerId;
    private String name;
    private int id;
    private String bedNumber;
    private int status;
    private List<ItemsBean> items;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Serializable{
        /**
         * commodity : {"images":"http://qiniuimage.zgzkys.com/1563874659804.jpg",
         * "stockpileCount":0,"price":0.01,"workOffCount":0,"name":"奶茶","id":10}
         * orderId : 438
         * count : 1
         * commodityId : 10
         * id : 114
         */

        private CommodityBean commodity;
        private int orderId;
        private int count;
        private int commodityId;
        private int id;

        public CommodityBean getCommodity() {
            return commodity;
        }

        public void setCommodity(CommodityBean commodity) {
            this.commodity = commodity;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class CommodityBean {
            /**
             * images : http://qiniuimage.zgzkys.com/1563874659804.jpg
             * stockpileCount : 0
             * price : 0.01
             * workOffCount : 0
             * name : 奶茶
             * id : 10
             */

            private String images;
            private int stockpileCount;
            private double price;
            private int workOffCount;
            private String name;
            private int id;

            public String getImages() {
                return images;
            }

            public void setImages(String images) {
                this.images = images;
            }

            public int getStockpileCount() {
                return stockpileCount;
            }

            public void setStockpileCount(int stockpileCount) {
                this.stockpileCount = stockpileCount;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getWorkOffCount() {
                return workOffCount;
            }

            public void setWorkOffCount(int workOffCount) {
                this.workOffCount = workOffCount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }

}
