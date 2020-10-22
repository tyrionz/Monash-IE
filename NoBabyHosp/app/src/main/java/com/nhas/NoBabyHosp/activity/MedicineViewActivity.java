package com.nhas.NoBabyHosp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.model.MedicineViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicineViewActivity extends AppCompatActivity {

    private TextView medicineNameTextView;
    private Medicine thisMedicine;
    private DateFormat dateFormat;
    private TextView displayCurrentStock;
    private TextView expiryDateTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_top, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_view);

        Toolbar toolbar = findViewById(R.id.navbar_top);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        thisMedicine = (Medicine) intent.getSerializableExtra("medicine");

        // Set the name of the medicine
        medicineNameTextView = findViewById(R.id.medicineNameTextValue);
        medicineNameTextView.setText(thisMedicine.getMedDescription());

        // Display the current stock
        displayCurrentStock = findViewById(R.id.displayCurrentStockValue);
        displayCurrentStock.setText(String.valueOf(thisMedicine.getCurrentStock()));

        // Display the expiry date
        dateFormat = new SimpleDateFormat("mm / YYYY");
        expiryDateTextView = findViewById(R.id.infoExpiryDateTextView);
        expiryDateTextView.setText(dateFormat.format(thisMedicine.getExpiryDate()));

    }
}