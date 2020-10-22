package com.NHAS.Infantime.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.Facility;

import java.util.List;

public class FacilityViewModel extends ViewModel {

    private MutableLiveData<List<Facility>> facilityNameList;

    public FacilityViewModel() {
        this.facilityNameList = new MutableLiveData<>();
    }

    public void setFacilityNameList(List<Facility> facilityNameList) {
        this.facilityNameList.postValue(facilityNameList);
    }

    public LiveData<List<Facility>> getFacilityList(){
        return facilityNameList;
    }
}
