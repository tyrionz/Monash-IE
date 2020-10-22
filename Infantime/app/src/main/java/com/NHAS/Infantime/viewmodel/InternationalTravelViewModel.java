package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.repository.InternationalTravelRepository;

import java.util.List;

public class InternationalTravelViewModel extends ViewModel {

    private InternationalTravelRepository repository;

    public InternationalTravelViewModel(){
    }

    public void initializeVars(Application application){
        repository = new InternationalTravelRepository(application);
    }

    public LiveData<List<InternationalTravelWithMedicinesTips>> getAllTrips(){
        return repository.getAllInternationalTrips();
    }



    public void insertTrip(InternationalTravelWithMedicinesTips trip){
        repository.insert(trip);
    }

    public void updateTrip(InternationalTrip trip){
        repository.updateTrip(trip);
    }
}
