package com.NHAS.Infantime.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.entities.DomesticTravelWithTips;
import com.NHAS.Infantime.data.entities.DomesticTrip;
import com.NHAS.Infantime.data.local.dao.Travel.DomesticTravelDAO;
import com.NHAS.Infantime.data.local.database.TravelDatabase;

import java.util.List;

public class DomesticTravelRepository {

    private DomesticTravelDAO dao;
    private LiveData<List<DomesticTravelWithTips>> allTrips;

    public DomesticTravelRepository(Application application) {
        TravelDatabase database = TravelDatabase.getInstance(application);
        dao = database.domesticTravelDAO();
    }

    // Get all trips in the database
    public LiveData<List<DomesticTravelWithTips>> getAllDomesticTrips(){
        allTrips = dao.getAllDomesticTravel();
        return allTrips;
    }

    // Update trip
    public void updateTrip(final DomesticTrip trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateDomesticTrip(trip);
            }
        });
    }

    // Insert trip with its list of medicines and tips
    public void insert(final DomesticTravelWithTips trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long tripIdentifier = dao.insertDomesticTrip(trip.domesticTrip);

                for(DomesticTip tip: trip.tipList){
                    tip.setFk_aid(tripIdentifier);
                }
                dao.insertDomesticTips(trip.tipList);
            }
        });
    }
}
