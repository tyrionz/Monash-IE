package com.NHAS.Infantime.data.local.dao.Tip;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.NHAS.Infantime.data.entities.InternationalTip;

import java.util.List;

@Dao
public interface InternationalTipDAO {

    @Query("SELECT * FROM internationaltip WHERE fk_tid = :tid")
    LiveData<List<InternationalTip>> getAllTipByTid(long tid);

    @Update
    void updateTip(InternationalTip tip);
}
