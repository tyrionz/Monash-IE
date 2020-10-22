package com.NHAS.Infantime.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class InternationalTravelWithMedicinesTips {

    @Embedded
    public InternationalTrip trip;

    @Relation(
            parentColumn = "tid",
            entityColumn = "med_id"
    )
    public List<Medicine> medicineList;

    @Relation(
            parentColumn = "tid",
            entityColumn = "tip_id"
    )
    public List<InternationalTip> tipList;

    public InternationalTravelWithMedicinesTips(InternationalTrip trip, List<Medicine> medicineList, List<InternationalTip> tipList) {
        this.trip = trip;
        this.medicineList = medicineList;
        this.tipList = tipList;
    }
}
