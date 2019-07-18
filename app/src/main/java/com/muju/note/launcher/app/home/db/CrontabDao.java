package com.muju.note.launcher.app.home.db;

import com.muju.note.launcher.litepal.LitePalDb;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class CrontabDao extends LitePalSupport {
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
    private int crontabid;
    private int addType;
    private String fileAddr;
    private String fileType;
    private String startDate;
    private String stopDate;
    private String execTime;
    private String weeks;

    /**
     * 新增数据
     *
     * @param dao
     */
    public void saveDb(CrontabDao dao) {
        LitePal.use(LitePalDb.zkysDb);
        dao.save();
    }

    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
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


    public int getCrontabid() {
        return crontabid;
    }

    public void setCrontabid(int crontabid) {
        this.crontabid = crontabid;
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

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

}
