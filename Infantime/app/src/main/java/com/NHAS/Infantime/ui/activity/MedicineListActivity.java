package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.MedicineRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;
import com.NHAS.Infantime.viewmodel.MedicineViewModel;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends AppCompatActivity implements MedicineRecyclerViewAdapter.CheckMedicine {

    private Button addMedicineButton;
    private RecyclerView medicineRecyclerView;
    private Button nextButton;
    private RecyclerView.LayoutManager layoutManager;
    private MedicineRecyclerViewAdapter adapter;
    private List<Medicine> medicineList;
    private MedicineViewModel model;
    private long startDate;
    private long endDate;
    private long duration;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        this.setTitle("Medicine Cabinet");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();

        layoutManager = new LinearLayoutManager(MedicineListActivity.this);

        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("requestCode", 0);
        // Get information from bundle if it's calling from the create trip flow
        if (requestCode == 0) {
            Bundle infoBundle = intent.getBundleExtra("infoBundle");
            startDate = infoBundle.getLong(InternationalTravelEnum.START_DATE.toString(), 0);
            endDate = infoBundle.getLong(InternationalTravelEnum.END_DATE.toString(), 0);
            LocalDate start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
            duration = ChronoUnit.DAYS.between(start, end);
        }else{
            // Calculate the duration for a known trip
            startDate = intent.getLongExtra(InternationalTravelEnum.START_DATE.toString(), 0);
            endDate = intent.getLongExtra(InternationalTravelEnum.END_DATE.toString(), 0);
            LocalDate start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
            duration = ChronoUnit.DAYS.between(start, end);
        }



        medicineList = new ArrayList<>();
        adapter = new MedicineRecyclerViewAdapter(medicineList, MedicineListActivity.this, MedicineListActivity.this);
        medicineRecyclerView.setAdapter(adapter);
        medicineRecyclerView.setLayoutManager(layoutManager);

        model = new ViewModelProvider(this).get(MedicineViewModel.class);
        model.initializeVars(getApplication());



        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addMedIntent = new Intent(MedicineListActivity.this, AddMedicineActivity.class);
                if (requestCode != 0) {
                    long tid = intent.getLongExtra(InternationalTravelEnum.TRIP_ID.toString(), 0);
                    addMedIntent.putExtra(InternationalTravelEnum.TRIP_ID.toString(), tid);
                }
                addMedIntent.putExtra("requestCode", requestCode);
                addMedIntent.putExtra(InternationalTravelEnum.DURATION.toString(), duration);
                startActivity(addMedIntent);
            }
        });


        // Calling from the add trip flow
        if (requestCode == 0) {
            Bundle infoBundle = intent.getBundleExtra("infoBundle");
            long startDate = infoBundle.getLong(InternationalTravelEnum.START_DATE.toString(), 0);
            long endDate = infoBundle.getLong(InternationalTravelEnum.END_DATE.toString(), 0);
            LocalDate start = Instant.ofEpochMilli(startDate).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = Instant.ofEpochMilli(endDate).atZone(ZoneId.systemDefault()).toLocalDate();
            long duration = ChronoUnit.DAYS.between(start, end);

            model.getTempContainer().observe(this, new Observer<List<Medicine>>() {
                @Override
                public void onChanged(List<Medicine> medicines) {
                    medicineList.clear();
                    // Update the stock needed for the current trip
                    for (Medicine thisMed : medicines) {
                        int stockNeeded = (int) Math.round(thisMed.getDosage() * duration);
                        thisMed.setStockNeeded(stockNeeded);
                        medicineList.add(thisMed);
                    }
                    adapter.notifyDataSetChanged();
                }
            });


            // On clicking Next Button
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MedicineListActivity.this, InternationalTravelChecklistActivity.class);
                    infoBundle.putSerializable(InternationalTravelEnum.MEDICINE_LIST.toString(), (Serializable) medicineList);
                    intent.putExtra("infoBundle", infoBundle);
                    startActivity(intent);
                    model.deleteAllTemp();
                }
            });
        } else { // Calling from the view trip flow
            long tid = intent.getLongExtra(InternationalTravelEnum.TRIP_ID.toString(), 0);
            model.getAllMedicinesByTid(tid).observe(this, new Observer<List<Medicine>>() {
                @Override
                public void onChanged(List<Medicine> medicines) {
                    medicineList.clear();
                    medicineList.addAll(medicines);
                    adapter.notifyDataSetChanged();
                }
            });

            nextButton.setEnabled(false);
            nextButton.setVisibility(View.GONE);
        }

    }

    /**
     *
     */
    private void findViews() {
        medicineRecyclerView = findViewById(R.id.medicineRecyclerView);
        addMedicineButton = findViewById(R.id.addMedicineButton);
        nextButton = findViewById(R.id.nextButton);
    }

    @Override
    public void checkMedicine(Medicine medicine, Boolean isChecked) {
        medicine.setGot(isChecked);
        model.updateMed(medicine);
    }
}