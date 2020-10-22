package com.nhas.NoBabyHosp.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Medicine implements java.io.Serializable {

    @PrimaryKey
//    @ColumnInfo(name = "medID")
//    private int medID;
    @NonNull
    @ColumnInfo(name = "medicine_description")
    private String medDescription;

    @ColumnInfo(name = "current_stock")
    private int currentStock;

    @ColumnInfo(name = "expiry_date")
    private Date expiryDate;


    public Medicine(String medDescription, int currentStock, Date expiryDate) {
 //       this.medID = medID;
        this.medDescription = medDescription;
        this.currentStock = currentStock;
        this.expiryDate = expiryDate;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setMedDescription(String medDescription) {
        this.medDescription = medDescription;
    }

    //public void setMedID(int medID) {
      //  this.medID = medID;
  //  }

    //public int getMedID() {
     //   return medID;
   // }

    public String getMedDescription() {
        return medDescription;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }
}
