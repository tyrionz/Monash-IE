package com.NHAS.Infantime.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.NHAS.Infantime.util.helpers.DateConverter;

import java.util.Date;

@Entity
public class InternationalTrip extends Trip {

    @ColumnInfo(name = "start_date")
    @TypeConverters(DateConverter.class)
    private Date startDate;

    @ColumnInfo(name = "end_date")
    @TypeConverters(DateConverter.class)
    private Date endDate;


    public InternationalTrip(String tripName, String destination, Date startDate, Date endDate) {
        super(tripName, destination);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
