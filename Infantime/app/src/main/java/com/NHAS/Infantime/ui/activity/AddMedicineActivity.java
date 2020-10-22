package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;
import com.NHAS.Infantime.viewmodel.MedicineViewModel;

public class AddMedicineActivity extends AppCompatActivity {

    private MedicineViewModel model;
    private EditText nameEditText;
    private TextView stockTextView;
    private EditText dosageEditText;
    private Button saveBtn;
    private double dosage;
    private double stockNeeded;

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
        setContentView(R.layout.activity_add_medicine);
        this.setTitle("Add Medicine");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        long duration = intent.getLongExtra(InternationalTravelEnum.DURATION.toString(), 0);

        model = new ViewModelProvider(this).get(MedicineViewModel.class);
        model.initializeVars(getApplication());

        findViews();

        dosageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not called
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not called
            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = dosageEditText.getText().toString();
                userInput.replace(",", "");
                try {
                    dosage = Double.parseDouble(userInput);
                    stockNeeded = dosage * duration;
                    stockTextView.setText(String.valueOf(stockNeeded));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        saveBtn.setOnClickListener(view -> {
            // Get the medicine name
            String medicineName = nameEditText.getText().toString();

            if (dosage > 0) { // If the dosage is not 0 then proceed
                if (!medicineName.trim().isEmpty()) { // If the name is not empty then proceed
                    int requestCode = intent.getIntExtra("requestCode", 0);
                    Medicine newMed = new Medicine(medicineName, (int) Math.ceil(stockNeeded), dosage);
                    if(requestCode == 0){
                        model.insertToContainer(newMed);
                    }else {
                        // Save the medicine
                        long tid = intent.getLongExtra(InternationalTravelEnum.TRIP_ID.toString(), 0);
                        newMed.setFk_tid(tid);
                        model.insertMeds(newMed);
                    }
                    onBackPressed();
                } else {
                    nameEditText.setError("Name must not be empty");
                }
            } else {
                stockTextView.setError("Dosage must not be empty or 0");
            }


        });
    }

    /**
     *
     */
    private void findViews() {
        nameEditText = findViewById(R.id.medicineNameEditText);
        dosageEditText = findViewById(R.id.dosageEditText);
        saveBtn = findViewById(R.id.saveButton);
        nameEditText = findViewById(R.id.medicineNameEditText);
        stockTextView = findViewById(R.id.quantityTextView);
    }
}