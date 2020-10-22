package com.NHAS.Infantime.data.local.dao.Travel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.entities.DomesticTravelWithTips;
import com.NHAS.Infantime.data.entities.DomesticTrip;

import java.util.List;

@Dao
public interface DomesticTravelDAO {

    @Transaction
    @Query("SELECT * FROM DomesticTrip")
    LiveData<List<DomesticTravelWithTips>> getAllDomesticTravel();

    @Insert
    void insertDomesticTips(List<DomesticTip> tips);

    @Transaction
    @Insert
    long insertDomesticTrip(DomesticTrip domesticTrip);

    @Update
    void updateDomesticTrip(DomesticTrip domesticTrip);

    @Delete
    void deleteDomesticTrip(DomesticTrip domesticTrip);
}

