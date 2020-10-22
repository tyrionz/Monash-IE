package com.nhas.NoBabyHosp.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task implements java.io.Serializable {

    @PrimaryKey
    @ColumnInfo(name = "tid")
    private int taskID;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "frequency")
    private int frequency;

    @ColumnInfo(name = "suggested_frequency")
    private int suggestedFrequency;

    @ColumnInfo(name = "purpose")
    private String purpose;

    @ColumnInfo(name = "assigned_date")
    private Date assignedDate;


    public Task(int taskID, String taskDescription, int suggestedFrequency, String purpose) {
        this.taskID = taskID;
        this.taskDescription = taskDescription;
        this.suggestedFrequency = suggestedFrequency;
        this.frequency = suggestedFrequency;
        this.purpose = purpose;
        // By default, date set to 253402261199000L = 9999/12/31
        this.assignedDate = new Date(253402261199000L);
    }

    public int getSuggestedFrequency() {
        return suggestedFrequency;
    }

    public String getPurpose() {
        return purpose;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getFrequency() {
        return frequency;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
