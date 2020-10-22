package com.nhas.NoBabyHosp.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.repository.TaskRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private TaskRepository tRepository;
    private MutableLiveData<List<Task>> allTask;

    public TaskViewModel(){
       allTask = new MutableLiveData<>();
    }

    public void setTask(ArrayList<Task> taskList){
        allTask.setValue(taskList);
    }

    public LiveData<List<Task>> getAllTasks(){
        return tRepository.getAllTasks();
    }

    public void initializeVars(Application application){
        tRepository = new TaskRepository(application);
    }

    public void updateDateByID(int tid, Date newDate){
        tRepository.updateDateByID(tid, newDate);
    }

    public void updateFrequencyByID(int tid, int newfrequency){
        tRepository.updateFrequencyByID(tid, newfrequency);
    }

    public void insert(Task task){
        tRepository.insert(task);
    }

    public void insertAll(Task... tasks){
        tRepository.insertAll(tasks);
    }

    public LiveData<Task> getTaskById(int tid){
        return tRepository.getById(tid);
    }

    public LiveData<List<Task>> getTaskByDate (Date date){
        // Convert the date to 00:00:00 and the next day for comparison
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        String start = today + " 00:00:00";
        String end = today + " 23:59:59";

        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tRepository.getTaskByDate(startDate, endDate);
    }
}
