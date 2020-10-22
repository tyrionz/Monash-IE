package com.NHAS.Infantime.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DomesticTravelWithTips {

    @Embedded
    public DomesticTrip domesticTrip;

    @Relation(
            parentColumn = "tid",
            entityColumn = "tip_id"
    )
    public List<DomesticTip> tipList;

    public DomesticTravelWithTips(DomesticTrip domesticTrip, List<DomesticTip> tipList) {
        this.domesticTrip = domesticTrip;
        this.tipList = tipList;
    }
}
