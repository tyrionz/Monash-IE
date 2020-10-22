package com.NHAS.Infantime.data.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity
public class Trip implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tid")
    private long tripID;

    @ColumnInfo(name = "trip_name")
    private String tripName;

    @ColumnInfo(name = "destination")
    private String destination;


    public Trip(String tripName, String destination) {
        this.tripName = tripName;
        this.destination = destination;
    }



    public long getTripID() {
        return tripID;
    }

    public void setTripID(long tripID) {
        this.tripID = tripID;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

}
