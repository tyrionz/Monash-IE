package com.NHAS.Infantime.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tip implements java.io.Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tip_id")
    private int tipID;

    @ColumnInfo(name = "tip_name")
    private String tipName;

    @ColumnInfo(name = "tip_description")
    private String tipDescription;

    @ColumnInfo(name = "purpose")
    private String purpose;

    @ColumnInfo(name = "done")
    private boolean done;

    @ColumnInfo(name = "expandable")
    private boolean expandable;

    @ColumnInfo(name = "when")
    private String when;



    public Tip(String tipName, String tipDescription, String purpose, String when) {
        this.tipName = tipName;
        this.tipDescription = tipDescription;
        this.purpose = purpose;
        this.done = false;
        this.expandable = false;
        this.when = when;
    }

    public void setTipID(int tipID) {
        this.tipID = tipID;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getTipName() {
        return tipName;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getPurpose() {
        return purpose;
    }

    public int getTipID() {
        return tipID;
    }

    public String getTipDescription() {
        return tipDescription;
    }


}
