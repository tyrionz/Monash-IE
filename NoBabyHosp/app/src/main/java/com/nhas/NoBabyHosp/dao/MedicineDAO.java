package com.nhas.NoBabyHosp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.nhas.NoBabyHosp.Entities.Medicine;

import java.util.Date;
import java.util.List;

@Dao
public interface MedicineDAO {

    @Query("SELECT * FROM medicine ORDER BY expiry_date ASC")
    LiveData<List<Medicine>> getAllMedicines();

    @Query("UPDATE medicine SET expiry_date = :expiryDate WHERE medicine_description = :medDes")
    void updateExpiryDateByID(String medDes, Date expiryDate);

    @Query("UPDATE medicine SET current_stock = :current_stock WHERE medicine_description = :medDes")
    void  updateStockByID(String medDes, int current_stock);

    @Query("SELECT * FROM medicine WHERE medicine_description = :medDes LIMIT 1")
    LiveData<Medicine> selectMedicineById(String medDes);

    @Insert
    void insertAllMeds(Medicine... medicines);

    @Insert
    void insertMeds(Medicine medicine);

    @Delete
    void deleteMed(Medicine medicine);

    @Query("DELETE FROM medicine")
    void deleteAllMeds();
}
