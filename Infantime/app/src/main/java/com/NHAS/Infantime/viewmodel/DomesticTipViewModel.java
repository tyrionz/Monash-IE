package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.repository.DomesticTipRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DomesticTipViewModel extends ViewModel {

    private MutableLiveData<List<DomesticTip>> mutableTipList;
    private DomesticTipRepository repository;

    public DomesticTipViewModel() {
        mutableTipList = new MutableLiveData<>();
    }

    public void initializeVars(Application application){
        repository = new DomesticTipRepository(application);
    }

    public void setMutableTipList(List<DomesticTip> tipList){
        mutableTipList.postValue(tipList);
    }

    public LiveData<List<DomesticTip>> getTipList(){
        return mutableTipList;
    }

    public LiveData<List<DomesticTip>> getDomesticTipsByAid(long aid){
        return repository.getAllTipByAid(aid);
    }

    public void updateDomesticTip(DomesticTip tip){
        repository.updateDomesticTip(tip);
    }


    public void fetchTipsByType(String type, String key) {
        Future<List<DomesticTip>> future = repository.fetchTipByType(type, key);

        while(!future.isDone()){
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            setMutableTipList(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
