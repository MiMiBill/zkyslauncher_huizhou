package com.muju.note.launcher.app.Cabinet.bean;


import com.muju.note.launcher.util.DateUtil;

public class LockInfo {

    /**
     * csq : 75
     * charge : 0
     * hasBed : 0
     * lastRefresh : 1554357200000
     * latitude : 22.9617160
     * pageSize : 20
     * pageNum : 1
     * voltage : 424
     * lockId : 768901006040
     * lockStatus : 2
     * batteryStat : 100
     * id : 193
     * brand : 3
     * longitude : 113.9013800
     */

    private int csq;
    private int charge;
    private int hasBed;
    private long lastRefresh;
    private String latitude;
    private int pageSize;
    private int pageNum;
    private int voltage;
    private String lockId;
    private int lockStatus;
    private int batteryStat;
    private int id;
    private int brand;
    private String longitude;

    public int getCsq() {
        return csq;
    }

    public void setCsq(int csq) {
        this.csq = csq;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getHasBed() {
        return hasBed;
    }

    public void setHasBed(int hasBed) {
        this.hasBed = hasBed;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(long lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getBatteryStat() {
        return batteryStat;
    }

    public void setBatteryStat(int batteryStat) {
        this.batteryStat = batteryStat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        String lastRefreshTime = DateUtil.timeStamp2Date(lastRefresh / 1000 + "", null);
        return "锁当前状态：" + "\n" +
                "csq-信号指标：" + csq + "\n" +
                "charge-充电电压：" + charge + "\n" +
                "hasBed-是否还床(0为无 1为有)：" + hasBed + "\n" +
                "lastRefresh-最后上报时间：" + lastRefreshTime + "\n" +
                "voltage-电池电压：" + voltage + "\n" +
                "lockId-设备ID：" + lockId + "\n" +
                "latitude-纬度：" + latitude + "\n" +
                "longitude-经度：" + longitude + "\n" +
                "lockStatus-锁状态(1：关 2：开)：" + lockStatus + "\n" +
                "batteryStat-剩余电量：" + batteryStat + "\n" +
                "brand-设备锁厂商品牌(1：连旅、2：物联网、3：物网通)：" + brand;
    }
}
