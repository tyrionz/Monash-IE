package com.nhas.NoBabyHosp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.dao.TaskDAO;
import com.nhas.NoBabyHosp.database.TaskDatabase;

import java.util.Date;
import java.util.List;

public class TaskRepository {

    private TaskDAO dao;
    private LiveData<List<Task>> allTasks;
    private LiveData<Task> task;

    public TaskRepository(Application application){
        TaskDatabase db = TaskDatabase.getInstance(application);
        dao = db.taskDAO();
    }

    // Get all task in the database
    public LiveData<List<Task>> getAllTasks(){
        allTasks = dao.getAll();
        return allTasks;
    }

    // Update assigned date for the specified task id
    public void updateDateByID(final int tid, final Date newDate){
        TaskDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateDateByID(tid, newDate);
            }
        });
    }

    // Update the frequency for the specified task id
    public void updateFrequencyByID(final int tid, final int frequency){
        TaskDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateFrequencyByID(tid, frequency);
            }
        });
    }


    // Insert all tasks
    public void insertAll(final Task... tasks){
        TaskDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(tasks);
            }
        });
    }

    // Insert one task
    public void insert(final Task task){
        TaskDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(task);
            }
        });
    }

    public LiveData<Task> getById(final int tid){
        return dao.selectTaskById(tid);
    }

    public LiveData<List<Task>> getTaskByDate(Date startDate, Date endDate){
        return dao.getTaskByDate(startDate, endDate);
    }


    public void setLiveTask(LiveData<Task> thisTask){
        this.task = thisTask;
    }


}
