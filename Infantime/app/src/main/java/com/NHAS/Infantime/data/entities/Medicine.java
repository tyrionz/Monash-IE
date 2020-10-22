package com.NHAS.Infantime.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Medicine implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "med_id")
    private int medID;

    @NonNull
    @ColumnInfo(name = "medicine_description")
    private String medDescription;

    @ColumnInfo(name = "stock_needed")
    private int stockNeeded;

    @ColumnInfo(name = "dosage")
    private double dosage;

    @ForeignKey
            (entity = Trip.class,
                    parentColumns = "tid",
                    childColumns = "med_id",
                    onDelete = CASCADE
            )
    private long fk_tid;

    @ColumnInfo(name = "got")
    private boolean got;


    public Medicine(@NonNull String medDescription, int stockNeeded, double dosage) {
        this.medDescription = medDescription;
        this.stockNeeded = stockNeeded;
        this.dosage = dosage;
        this.got = false;
    }

    public boolean isGot() {
        return got;
    }

    public void setGot(boolean got) {
        this.got = got;
    }

    public int getMedID() {
        return medID;
    }

    public void setMedID(int medID) {
        this.medID = medID;
    }

    public int getStockNeeded() {
        return stockNeeded;
    }

    public void setMedDescription(String medDescription) {
        this.medDescription = medDescription;
    }

    public String getMedDescription() {
        return medDescription;
    }

    public double getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public void setStockNeeded(int stockNeeded) {
        this.stockNeeded = stockNeeded;
    }

    public long getFk_tid() {
        return fk_tid;
    }

    public void setFk_tid(long fk_tid) {
        this.fk_tid = fk_tid;
    }
}
