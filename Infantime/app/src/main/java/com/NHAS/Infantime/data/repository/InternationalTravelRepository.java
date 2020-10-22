package com.NHAS.Infantime.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.data.local.dao.Travel.InternationTravelDAO;
import com.NHAS.Infantime.data.local.database.TravelDatabase;

import java.util.List;

public class InternationalTravelRepository {

    private InternationTravelDAO dao;
    private LiveData<List<InternationalTravelWithMedicinesTips>> allTrips;

    public InternationalTravelRepository(Application application) {
        TravelDatabase database = TravelDatabase.getInstance(application);
        dao = database.internationTravelDAO();
    }

    // Get all trips in the database
    public LiveData<List<InternationalTravelWithMedicinesTips>> getAllInternationalTrips(){
        allTrips = dao.getAllTrips();
        return allTrips;
    }

    // Update trip
    public void updateTrip(final InternationalTrip trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateInternationalTrip(trip);
            }
        });
    }

    // Insert one trip
    public void insert(final InternationalTrip trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertInternationalTrip(trip);
            }
        });
    }

    // Delete trip
    public void deleteTrip(final InternationalTrip trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteInternationalTrip(trip);
            }
        });
    }

    // Insert trip with its list of medicines and tips
    public void insert(final InternationalTravelWithMedicinesTips trip){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long tripIdentifier = dao.insertInternationalTrip(trip.trip);

                for(Medicine medicine: trip.medicineList){
                    medicine.setFk_tid(tripIdentifier);
                }

                dao.insertMedicines(trip.medicineList);

                for(InternationalTip tip: trip.tipList){
                    tip.setFk_tid(tripIdentifier);
                }
                dao.insertInternationalTips(trip.tipList);
            }
        });
    }



}
