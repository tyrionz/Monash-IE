package com.nhas.NoBabyHosp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.model.MedicineViewModel;
import com.nhas.NoBabyHosp.model.TaskViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMedicineActivity extends AppCompatActivity {

    private Button saveBtn;
    private MedicineViewModel model;
    private EditText nameEditText;
    private EditText stockEditText;
    private EditText expiryEditText;


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
        setContentView(R.layout.activity_add_medicine);

        Toolbar toolbar = findViewById(R.id.navbar_top);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        model = new ViewModelProvider(this).get(MedicineViewModel.class);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEditText = findViewById(R.id.nameEditText);
                stockEditText = findViewById(R.id.stockNumberEditText);
                expiryEditText = findViewById(R.id.expiryDateEditText);

                if(nameEditText.getText().toString().trim().length() > 0){
                    String newName = nameEditText.getText().toString().trim();
                    if(expiryEditText.getText().toString().trim().length() > 0){
                        String[] expiryDate = expiryEditText.getText().toString().split("/");
                        if(expiryDate.length == 2){
                            DateFormat todate = new SimpleDateFormat("mm/YYYY");
                            try {
                                Date newExpiryDate = todate.parse(expiryEditText.getText().toString());
                                if (Calendar.getInstance().getTime().compareTo(newExpiryDate) < 0){
                                    if(stockEditText.getText().toString().trim().length() > 0){
                                        try{
                                            int newStock = Integer.parseInt(stockEditText.getText().toString().trim());
                                            model.initializeVars(AddMedicineActivity.this.getApplication());
                                            model.insertMeds(new Medicine(newName, newStock, newExpiryDate));
                                            onBackPressed();
                                        }catch(NumberFormatException  e){
                                            stockEditText.setError("Please enter a valid number.");
                                        }
                                    }else{
                                        stockEditText.setError("Stock myst not be empty.");
                                    }
                                }else{
                                    expiryEditText.setError("The med has expired.");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            expiryEditText.setError("Please enter the correct format MM/YYYY.");
                        }
                    } else{
                        expiryEditText.setError("Date must not be empty");
                    }
                } else {
                    nameEditText.setError("Name must not be empty");
                }
            }
        });

    }
}