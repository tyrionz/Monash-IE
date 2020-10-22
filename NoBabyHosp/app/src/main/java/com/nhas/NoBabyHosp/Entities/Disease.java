package com.nhas.NoBabyHosp.Entities;

public class Disease implements java.io.Serializable {

    private int diseaseID;
    private String diseaseScienceName;
    private String diseaseCommonName;

    public Disease(int diseaseID, String diseaseScienceName, String diseaseCommonName) {
        this.diseaseID = diseaseID;
        this.diseaseScienceName = diseaseScienceName;
        this.diseaseCommonName = diseaseCommonName;
    }


    public int getDiseaseID() {
        return diseaseID;
    }

    public String getDiseaseScienceName() {
        return diseaseScienceName;
    }

    public String getDiseaseCommonName() {
        return diseaseCommonName;
    }
}
