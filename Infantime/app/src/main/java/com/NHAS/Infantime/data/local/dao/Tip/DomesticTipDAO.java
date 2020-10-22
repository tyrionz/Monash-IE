package com.NHAS.Infantime.data.local.dao.Tip;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.NHAS.Infantime.data.entities.DomesticTip;

import java.util.List;

@Dao
public interface DomesticTipDAO {

    @Query("SELECT * FROM domestictip WHERE fk_aid = :aid")
    LiveData<List<DomesticTip>> getAllTipByAid(long aid);

    @Update
    void updateDomesticTip(DomesticTip tip);

}
