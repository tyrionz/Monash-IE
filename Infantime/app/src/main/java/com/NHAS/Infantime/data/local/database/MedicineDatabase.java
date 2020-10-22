package com.NHAS.Infantime.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.data.local.dao.Medicine.MedicineDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medicine.class}, version = 6, exportSchema = false)
public abstract class MedicineDatabase extends RoomDatabase {
    public abstract MedicineDAO medicineDAO();

    private static MedicineDatabase INSTANCE;

    // Create an ExecutorService with a fixed thread pool to run database operations asyn on the background thread
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized MedicineDatabase getInstance(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MedicineDatabase.class, "MedicineDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }
}
