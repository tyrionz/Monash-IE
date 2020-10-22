package com.nhas.NoBabyHosp.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhas.NoBabyHosp.Entities.Disease;

import java.util.List;

public class DiseaseViewModel extends ViewModel {
    private MutableLiveData<List<Disease>> mutableDiseaseList;

    public DiseaseViewModel(){
        mutableDiseaseList = new MutableLiveData<>();
    }

    public void setDisease(List<Disease> diseaseList){
        mutableDiseaseList.setValue(diseaseList);
    }

    public LiveData<List<Disease>> getDiseaseList(){
        return mutableDiseaseList;
    }
}
