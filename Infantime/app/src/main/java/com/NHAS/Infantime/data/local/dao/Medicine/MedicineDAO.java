package com.NHAS.Infantime.data.local.dao.Medicine;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.NHAS.Infantime.data.entities.Medicine;

import java.util.List;

@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medicine")
    LiveData<List<Medicine>> getAllMedicines();

    @Query("SELECT * FROM medicine WHERE medicine_description = :medDes LIMIT 1")
    LiveData<Medicine> selectMedicineById(String medDes);

    @Query("SELECT * FROM medicine WHERE fk_tid = :tid")
    LiveData<List<Medicine>> getAllMedicineByTid(long tid);

    @Insert
    void insertAllMeds(Medicine... medicines);

    @Insert
    void insertMeds(Medicine medicine);

    @Update
    void updateMed(Medicine medicine);

    @Delete
    void deleteMed(Medicine medicine);

    @Query("DELETE FROM medicine")
    void deleteAllMeds();
}
