package com.muju.note.launcher.app.home.util;

import com.muju.note.launcher.app.home.bean.PatientResponse;

public class PatientUtil {

    public static PatientUtil patientUtil = null;

    public static PatientUtil getInstance() {
        if (patientUtil == null) {
            patientUtil = new PatientUtil();
        }
        return patientUtil;
    }

    private PatientResponse.DataBean patientData;

    public void setPatientData(PatientResponse.DataBean patientData) {
        this.patientData = patientData;
    }

    public PatientResponse.DataBean getPatientData() {
        return patientData;
    }

}
