package com.nhas.NoBabyHosp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.dao.TaskDAO;
import com.nhas.NoBabyHosp.typeconverter.DateConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();

    private static TaskDatabase INSTANCE;

    // Create an ExecutorService with a fixed thread pool to run database operations asyn on the background thread
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized TaskDatabase getInstance(final Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, "TaskDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }
}
