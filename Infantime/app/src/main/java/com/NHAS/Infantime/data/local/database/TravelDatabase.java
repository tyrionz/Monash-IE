package com.NHAS.Infantime.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.entities.DomesticTrip;
import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.data.local.dao.Medicine.MedicineDAO;
import com.NHAS.Infantime.data.local.dao.Tip.DomesticTipDAO;
import com.NHAS.Infantime.data.local.dao.Tip.InternationalTipDAO;
import com.NHAS.Infantime.data.local.dao.Travel.DomesticTravelDAO;
import com.NHAS.Infantime.data.local.dao.Travel.InternationTravelDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        InternationalTrip.class,
        InternationalTip.class,
        DomesticTrip.class,
        DomesticTip.class,
        Medicine.class},
        version = 2, exportSchema = false)

public abstract class TravelDatabase extends RoomDatabase {
    // All dao needed
    public abstract InternationTravelDAO internationTravelDAO();
    public abstract DomesticTravelDAO domesticTravelDAO();
    public abstract MedicineDAO medicineDAO();
    public abstract DomesticTipDAO domesticTipDAO();
    public abstract InternationalTipDAO internationalTipDAO();


    private static TravelDatabase INSTANCE;

    // Create a fixed thread pool to handle background writing operations
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized TravelDatabase getInstance(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                    TravelDatabase.class, "TravelDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
