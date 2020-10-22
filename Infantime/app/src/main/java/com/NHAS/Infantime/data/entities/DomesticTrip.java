package com.NHAS.Infantime.data.entities;


import androidx.room.Entity;

import java.io.Serializable;


@Entity
public class DomesticTrip extends Trip implements Serializable {

    public DomesticTrip(String tripName, String destination) {
        super(tripName, destination);
    }
}

