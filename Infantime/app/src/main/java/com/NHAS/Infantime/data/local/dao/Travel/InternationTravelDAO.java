package com.NHAS.Infantime.data.local.dao.Travel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.entities.Medicine;

import java.util.List;

@Dao
public interface InternationTravelDAO {

    @Transaction
    @Query("SELECT * FROM internationaltrip ORDER BY start_date")
    LiveData<List<InternationalTravelWithMedicinesTips>> getAllTrips();

    @Insert
    void insertInternationalTips(List<InternationalTip> tips);

    @Insert
    void insertMedicines(List<Medicine> medicine);

    @Transaction
    @Insert
    long insertInternationalTrip(InternationalTrip trip);

    @Update
    void updateInternationalTrip(InternationalTrip trip);

    @Delete
    void deleteInternationalTrip(InternationalTrip trip);


}
