package com.muju.note.launcher.app.hostipal.bean;

public class ChemotherapyBean {

    private String name;
    private String result;
    private String example;
    private String company;
    private String nameSub;
    private String resultSub;
    private String exampleSub;
    private String companySub;

    public ChemotherapyBean() {
    }

    public ChemotherapyBean(String name, String result, String example, String company, String nameSub, String resultSub, String exampleSub, String companySub) {
        this.name = name;
        this.result = result;
        this.example = example;
        this.company = company;
        this.nameSub = nameSub;
        this.resultSub = resultSub;
        this.exampleSub = exampleSub;
        this.companySub = companySub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNameSub() {
        return nameSub;
    }

    public void setNameSub(String nameSub) {
        this.nameSub = nameSub;
    }

    public String getResultSub() {
        return resultSub;
    }

    public void setResultSub(String resultSub) {
        this.resultSub = resultSub;
    }

    public String getExampleSub() {
        return exampleSub;
    }

    public void setExampleSub(String exampleSub) {
        this.exampleSub = exampleSub;
    }

    public String getCompanySub() {
        return companySub;
    }

    public void setCompanySub(String companySub) {
        this.companySub = companySub;
    }
}
