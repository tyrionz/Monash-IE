package com.nhas.NoBabyHosp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nhas.NoBabyHosp.Entities.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task ORDER BY assigned_date ASC")
    LiveData<List<Task>> getAll();

    @Query("UPDATE task SET assigned_date = :assignedDate WHERE tid = :tid")
    void updateDateByID(int tid, Date assignedDate);

    @Query("UPDATE task SET frequency = :frequency WHERE tid = :tid")
    void  updateFrequencyByID(int tid, int frequency);

    @Query("SELECT * FROM task WHERE tid = :tid LIMIT 1")
    LiveData<Task> selectTaskById(int tid);

    @Query("SELECT * FROM task WHERE assigned_date BETWEEN :startDate AND :endDate")
    LiveData<List<Task>> getTaskByDate(Date startDate, Date endDate);

    @Insert
    void insertAll(Task... tasks);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task")
    void deleteAll();
}
