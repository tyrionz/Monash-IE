package com.NHAS.Infantime.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class DomesticTip extends Tip{

    @ForeignKey(
            entity = DomesticTrip.class,
            parentColumns = "tid",
            childColumns = "tip_id",
            onDelete = CASCADE
    )
    private long fk_aid;


    public DomesticTip(String tipName, String tipDescription, String purpose, String when) {
        super(tipName, tipDescription, purpose, when);
    }

    public long getFk_aid() {
        return fk_aid;
    }

    public void setFk_aid(long fk_aid) {
        this.fk_aid = fk_aid;
    }
}
