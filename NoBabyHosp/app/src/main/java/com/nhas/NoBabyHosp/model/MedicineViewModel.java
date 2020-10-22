package com.nhas.NoBabyHosp.model;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.repository.MedicineRepository;
import com.nhas.NoBabyHosp.repository.TaskRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedicineViewModel extends ViewModel {
    private MedicineRepository mRepository;
    private MutableLiveData<List<Medicine>> allMedicines;

    public MedicineViewModel(){
       allMedicines = new MutableLiveData<>();
    }

    public void setAllMedicines(ArrayList<Medicine> medicineList){
        allMedicines.setValue(medicineList);
    }

    public LiveData<List<Medicine>> getAllMedicines(){
        return mRepository.getAllMedicines();
    }

    public void initializeVars(Application application){
        mRepository = new MedicineRepository(application);
    }

    public void updateExpiryDateByID(String medDes, Date newExpiryDate){
        mRepository.updateExpiryDateByID(medDes, newExpiryDate);
    }

    public void updateStockByID(String medDes, int current_stock){
        mRepository.updateStockByID(medDes, current_stock);
    }

    public void insertMeds(Medicine medicine){
        mRepository.insertMeds(medicine);
    }

    public void insertAllMeds(Medicine... medicines){
        mRepository.insertAllMeds(medicines);
    }

    public LiveData<Medicine> getMedById(String medDes){
        return mRepository.getByMedId(medDes);
    }
}
