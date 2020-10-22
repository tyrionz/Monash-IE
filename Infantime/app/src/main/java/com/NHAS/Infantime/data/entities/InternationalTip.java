package com.NHAS.Infantime.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class InternationalTip extends Tip{

    @ForeignKey(
            entity = InternationalTrip.class,
            parentColumns = "tid",
            childColumns = "tip_id",
            onDelete = CASCADE
    )
    private long fk_tid;

    public InternationalTip(String tipName, String tipDescription, String purpose, String when) {
        super(tipName, tipDescription, purpose, when);
    }

    public long getFk_tid() {
        return fk_tid;
    }

    public void setFk_tid(long fk_tid) {
        this.fk_tid = fk_tid;
    }
}
