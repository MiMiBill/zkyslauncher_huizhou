package com.muju.note.launcher.app.hostipal.bean;

/**
 * Created by Administrator on 2018/6/19.
 */

public class ChemotherapayItemInfo {

    private String projectNmae;//项目名称
    private String result;//结果
    private String unit;//单位
    private String tip;//提示
    private String reRange;//参考范围

    public ChemotherapayItemInfo(String projectNmae, String result, String unit, String tip, String reRange) {
        this.projectNmae = projectNmae;
        this.result = result;
        this.unit = unit;
        this.tip = tip;
        this.reRange = reRange;
    }

    public String getProjectNmae() {
        return projectNmae;
    }

    public void setProjectNmae(String projectNmae) {
        this.projectNmae = projectNmae;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getReRange() {
        return reRange;
    }

    public void setReRange(String reRange) {
        this.reRange = reRange;
    }

    @Override
    public String toString() {
        return "ChemotherapayItemInfo{" +
                "projectNmae='" + projectNmae + '\'' +
                ", result='" + result + '\'' +
                ", unit='" + unit + '\'' +
                ", tip='" + tip + '\'' +
                ", reRange='" + reRange + '\'' +
                '}';
    }
}
