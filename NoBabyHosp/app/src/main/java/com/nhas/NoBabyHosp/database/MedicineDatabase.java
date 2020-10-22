package com.nhas.NoBabyHosp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.dao.MedicineDAO;
import com.nhas.NoBabyHosp.typeconverter.DateConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Medicine.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
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
