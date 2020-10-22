package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.repository.InternationalTipRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class InternationalTipViewModel extends ViewModel {

    private MutableLiveData<List<InternationalTip>> mutableTipList;
    private InternationalTipRepository repository;

    public InternationalTipViewModel() {
        mutableTipList = new MutableLiveData<>();
    }

    public void initializeVars(Application application){
        repository = new InternationalTipRepository(application);
    }

    public void setMutableTipList(List<InternationalTip> tipList){
        mutableTipList.postValue(tipList);
    }

    public LiveData<List<InternationalTip>> getMutableTipList() {
        return mutableTipList;
    }

    public LiveData<List<InternationalTip>> getInternationalTipByTid(long tid){
        return repository.getAllTipByTid(tid);
    }

    public void updateInternationalTip(InternationalTip tip){
        repository.updateInternationalTip(tip);
    }

    public void fetchTipsByType(String type, String key) {
        Future<List<InternationalTip>> future = repository.fetchTipByType(type, key);

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
