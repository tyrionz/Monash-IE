package com.NHAS.Infantime.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.data.local.dao.Medicine.MedicineDAO;
import com.NHAS.Infantime.data.local.database.MedicineDatabase;
import com.NHAS.Infantime.data.local.database.TravelDatabase;

import java.util.List;

public class MedicineRepository {

    private MedicineDAO dao;
    private LiveData<List<Medicine>> allMedicines;
    private LiveData<Medicine> medicine;
    private MedicineDAO temDao;

    public MedicineRepository(Application application){
        TravelDatabase db = TravelDatabase.getInstance(application);
        dao = db.medicineDAO();
        temDao = MedicineDatabase.getInstance(application).medicineDAO();
    }

    // Get all medicines in the database
    public LiveData<List<Medicine>> getAllMedicines(){
        allMedicines = dao.getAllMedicines();
        return allMedicines;
    }

    public LiveData<List<Medicine>> getAllMedicineByTid(long tid){
        allMedicines = dao.getAllMedicineByTid(tid);
        return allMedicines;
    }

    // Get all medicines in the database
    public LiveData<List<Medicine>> getAllTempMedicines(){
        allMedicines = temDao.getAllMedicines();
        return allMedicines;
    }

    public void insertTempMeds(final Medicine medicine){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                temDao.insertMeds(medicine);
            }
        });
    }

    public void deleteAllTemp(){
        MedicineDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                temDao.deleteAllMeds();
            }
        });
    }


    // Insert all medicines
    public void insertAllMeds(final Medicine... medicines){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAllMeds(medicines);
            }
        });
    }

    // Insert one task
    public void insertMeds(final Medicine medicine){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertMeds(medicine);
            }
        });
    }

    // Update medicine
    public void updateMed(final Medicine medicine){
        TravelDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMed(medicine);
            }
        });
    }

    public LiveData<Medicine> getByMedId(final String medDes){
        return dao.selectMedicineById(medDes);
    }

}
