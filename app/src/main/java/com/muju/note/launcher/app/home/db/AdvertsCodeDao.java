package com.muju.note.launcher.app.home.db;

import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class AdvertsCodeDao extends LitePalSupport implements Serializable {

    private int pageSize;
    private int closeType;
    private int pageNum;
    private String linkContent;
    private int second;
    private String resourceUrl;
    private String name;
    private int linkType;
    private int adid;
    private String advertType; //广告类型 1:轮播  2:插屏 3:公众号推广  4:通道
    private String additionUrl; //画中画url
    private int order;
    private int status;
    private int interval;
    private String code;
    private String pubCode;   //公众号回复码
    private String taskUrl;
    private int taskType;
    private int wxType;

    /**
     * 新增数据
     *
     * @param dao
     */
    public void saveDb(AdvertsCodeDao dao) {
        LitePal.use(LitePalDb.zkysDb);
        dao.save();
    }

    public int getWxType() {
        return wxType;
    }

    public void setWxType(int wxType) {
        this.wxType = wxType;
    }

    public String getPubCode() {
        return pubCode;
    }

    public void setPubCode(String pubCode) {
        this.pubCode = pubCode;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
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


    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getAdid() {
        return adid;
    }

    public void setAdid(int adid) {
        this.adid = adid;
    }

    public String getAdvertType() {
        return advertType;
    }

    public void setAdvertType(String advertType) {
        this.advertType = advertType;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
