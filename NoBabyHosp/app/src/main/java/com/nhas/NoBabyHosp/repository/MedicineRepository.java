package com.nhas.NoBabyHosp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.dao.MedicineDAO;
import com.nhas.NoBabyHosp.database.MedicineDatabase;

import java.util.Date;
import java.util.List;

public class MedicineRepository {

    private MedicineDAO dao;
    private LiveData<List<Medicine>> allMedicines;
    private LiveData<Medicine> medicine;

    public MedicineRepository(Application application){
        MedicineDatabase db = MedicineDatabase.getInstance(application);
        dao = db.medicineDAO();
    }

    // Get all medicines in the database
    public LiveData<List<Medicine>> getAllMedicines(){
        allMedicines = dao.getAllMedicines();
        return allMedicines;
    }

    // Update expiry date for the specified medicine id
    public void updateExpiryDateByID(final String medDes, final Date newExpiryDate){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateExpiryDateByID(medDes, newExpiryDate);
            }
        });
    }

    // Update the stock for the specified medicine id
    public void updateStockByID(final String medDes, final int current_stock){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateStockByID(medDes, current_stock);
            }
        });
    }


    // Insert all medicines
    public void insertAllMeds(final Medicine... medicines){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAllMeds(medicines);
            }
        });
    }

    // Insert one task
    public void insertMeds(final Medicine medicine){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertMeds(medicine);
            }
        });
    }

    public LiveData<Medicine> getByMedId(final String medDes){
        return dao.selectMedicineById(medDes);
    }

}
