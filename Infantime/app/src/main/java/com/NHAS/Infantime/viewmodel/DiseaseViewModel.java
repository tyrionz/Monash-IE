package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.Disease;
import com.NHAS.Infantime.data.repository.DiseaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DiseaseViewModel extends ViewModel {
    private MutableLiveData<List<Disease>> mutableDiseaseList;
    private MutableLiveData<List<String>> mutableSymptomList;
    private DiseaseRepository diseaseRepository;
    private String key;

    public DiseaseViewModel() {
        mutableDiseaseList = new MutableLiveData<>();
        mutableSymptomList = new MutableLiveData<>();
    }

    public void initializeVars(Application application) {
        diseaseRepository = new DiseaseRepository(this.key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDisease(List<Disease> diseaseList) {
        mutableDiseaseList.postValue(diseaseList);
    }

    private void setSymptom(List<String> symptomList) {
        mutableSymptomList.postValue(symptomList);
    }

    public LiveData<List<Disease>> getDiseaseList() {
        return mutableDiseaseList;
    }

    public LiveData<List<String>> getSymptomList() {
        return mutableSymptomList;
    }

    public void getAllSymptoms() {
        Future<List<String>> future = diseaseRepository.getAllSymptoms();

        while (!future.isDone()) {
            continue;
        }

        List<String> symptomList = new ArrayList<>();
        try {
            symptomList = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (symptomList.size() == 0) {
            symptomList.add(0, "An error has occurred.");
        } else {
            symptomList.add(0, "Pick a symptom");
        }

        setSymptom(symptomList);
    }

    public void fetchAllDisease() {
        Future<List<Disease>> future = diseaseRepository.fetchAllDisease();

        // Wait until the thread is done
        while (!future.isDone()) {
            continue;
        }

        // Get the future
        List<Disease> diseaseList = new ArrayList<>();
        try {
            diseaseList = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setDisease(diseaseList);

    }

    public void fetchDiseaseBySymptom(String symptom) {
        Future<List<Disease>> future = diseaseRepository.fetchDiseaseBySymptom(symptom);

        // Wait until the thread is done
        while (!future.isDone()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
                future.cancel(true);
            }
        }

        // Get the future
        List<Disease> diseaseList = new ArrayList<>();
        try {
            diseaseList = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setDisease(diseaseList);
    }
}
