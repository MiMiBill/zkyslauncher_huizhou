package com.muju.note.launcher.app.home.db;

import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class AdvertsCodeDao extends LitePalSupport {
    private int clickCount;
    private String endDate;
    private int pageSize;
    private int closeType;
    private int pageNum;
    private String linkContent;
    private int second;
    private int showCount;
    private String resourceUrl;
    private String name;
    private int createUser;
    private int linkType;
    private int id;
    private String advertOwner;
    private String advertType; //广告类型 1:轮播  2:插屏 3:公众号推广  4:通道
    private int advertPositionId;
    private String accessModule;
    private String startDate;
    private String createDate;
    private String additionUrl; //画中画url
    private int order;
    private int status;
    private int interval;
    private int incomeType;
    private String code;

    /**
     *   新增数据
     * @param dao
     */
    public void saveDb(AdvertsCodeDao dao){
        LitePal.use(LitePalDb.zkysDb);
        dao.save();
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCloseType() {
        return closeType;
    }

    public void setCloseType(int closeType) {
        this.closeType = closeType;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getLinkContent() {
        return linkContent;
    }

    public void setLinkContent(String linkContent) {
        this.linkContent = linkContent;
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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvertOwner() {
        return advertOwner;
    }

    public void setAdvertOwner(String advertOwner) {
        this.advertOwner = advertOwner;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
    }

    public int getAdvertPositionId() {
        return advertPositionId;
    }

    public void setAdvertPositionId(int advertPositionId) {
        this.advertPositionId = advertPositionId;
    }

    public String getAccessModule() {
        return accessModule;
    }

    public void setAccessModule(String accessModule) {
        this.accessModule = accessModule;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAdditionUrl() {
        return additionUrl;
    }

    public void setAdditionUrl(String additionUrl) {
        this.additionUrl = additionUrl;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
