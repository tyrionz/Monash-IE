package com.NHAS.Infantime.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.data.repository.MedicineRepository;

import java.util.ArrayList;
import java.util.List;

public class MedicineViewModel extends ViewModel {
    private MedicineRepository mRepository;
    private MutableLiveData<List<Medicine>> allMedicines;

    public MedicineViewModel() {
        allMedicines = new MutableLiveData<>();
    }

    public void setAllMedicines(ArrayList<Medicine> medicineList) {
        allMedicines.setValue(medicineList);
    }

    public LiveData<List<Medicine>> getTempContainer() {
        return mRepository.getAllTempMedicines();
    }


    public void insertToContainer(Medicine medicine) {
        mRepository.insertTempMeds(medicine);
    }

    public void deleteAllTemp() {
        mRepository.deleteAllTemp();
    }


    public LiveData<List<Medicine>> getAllMedicines() {
        return mRepository.getAllMedicines();
    }

    public LiveData<List<Medicine>> getAllMedicinesByTid(long tid) {
        return mRepository.getAllMedicineByTid(tid);
    }

    public void initializeVars(Application application) {
        mRepository = new MedicineRepository(application);
    }

    public void insertMeds(Medicine medicine) {
        mRepository.insertMeds(medicine);
    }

    public void insertAllMeds(Medicine... medicines) {
        mRepository.insertAllMeds(medicines);
    }

    public void updateMed(Medicine medicine) {
        mRepository.updateMed(medicine);
    }

    public LiveData<Medicine> getMedById(String medDes) {
        return mRepository.getByMedId(medDes);
    }
}
