package com.muju.note.launcher.app.hostipal.db;

import org.litepal.crud.LitePalSupport;

public class InfomationDao extends LitePalSupport {
    private String title;
    private int id;
    private String author;
    private String source;
    private int columnid;
    private int clickCount;
    private String summary;
    private String tag;
    private String cause;
    private String check;
    private String diacrsis;
    private String dassification;
    private String clinicalManifestation;
    private String antidiastole;
    private String cure;
    private String prognosis;
    private String prophylaxis;
    private String complicatingDisease;
    private String dietCare;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDassification() {
        return dassification;
    }

    public void setDassification(String dassification) {
        this.dassification = dassification;
    }

    public String getClinicalManifestation() {
        return clinicalManifestation;
    }

    public void setClinicalManifestation(String clinicalManifestation) {
        this.clinicalManifestation = clinicalManifestation;
    }

    public String getDietCare() {
        return dietCare;
    }

    public void setDietCare(String dietCare) {
        this.dietCare = dietCare;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getColumnid() {
        return columnid;
    }

    public void setColumnid(int columnid) {
        this.columnid = columnid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getDiacrsis() {
        return diacrsis;
    }

    public void setDiacrsis(String diacrsis) {
        this.diacrsis = diacrsis;
    }

    public String getAntidiastole() {
        return antidiastole;
    }

    public void setAntidiastole(String antidiastole) {
        this.antidiastole = antidiastole;
    }

    public String getCure() {
        return cure;
    }

    public void setCure(String cure) {
        this.cure = cure;
    }

    public String getPrognosis() {
        return prognosis;
    }

    public void setPrognosis(String prognosis) {
        this.prognosis = prognosis;
    }

    public String getProphylaxis() {
        return prophylaxis;
    }

    public void setProphylaxis(String prophylaxis) {
        this.prophylaxis = prophylaxis;
    }

    public String getComplicatingDisease() {
        return complicatingDisease;
    }

    public void setComplicatingDisease(String complicatingDisease) {
        this.complicatingDisease = complicatingDisease;
    }
}
