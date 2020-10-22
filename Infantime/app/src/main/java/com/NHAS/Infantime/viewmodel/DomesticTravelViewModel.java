package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.DomesticTravelWithTips;
import com.NHAS.Infantime.data.repository.DomesticTravelRepository;

import java.util.List;

public class DomesticTravelViewModel extends ViewModel {

    private DomesticTravelRepository repository;

    public DomesticTravelViewModel() {
    }

    public void initializeVars(Application application){
        repository = new DomesticTravelRepository(application);
    }

    public LiveData<List<DomesticTravelWithTips>> getAllTrips(){
        return repository.getAllDomesticTrips();
    }

    public void insertTrip(DomesticTravelWithTips trip){
        repository.insert(trip);
    }
}
