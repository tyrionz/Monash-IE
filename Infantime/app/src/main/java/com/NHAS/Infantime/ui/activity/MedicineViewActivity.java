package com.NHAS.Infantime.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.viewmodel.MedicineViewModel;

public class MedicineViewActivity extends AppCompatActivity {

    private TextView medicineNameTextView;
    private Medicine thisMedicine;
    private TextView displayCurrentStock;
    private MedicineViewModel model;
//    private TextView clickToEditStockTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
        this.setTitle("Medicine");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();

        Intent intent = getIntent();
        thisMedicine = (Medicine) intent.getSerializableExtra("medicine");

        model = new ViewModelProvider(MedicineViewActivity.this).get(MedicineViewModel.class);
        model.initializeVars(getApplication());
        model.getMedById(thisMedicine.getMedDescription()).observe(MedicineViewActivity.this, new Observer<Medicine>() {
            @Override
            public void onChanged(Medicine medicine) {
                // Set the name of the medicine
                medicineNameTextView.setText(thisMedicine.getMedDescription());

                // Display the current stock
                displayCurrentStock.setText(String.valueOf(thisMedicine.getStockNeeded()));

                // Display the dosage of the medicine

            }
        });


//        clickToEditStockTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Show the number picker dialog on click
//                showNumberPickerDialog().show();
//            }
//        });

    }

    private void findViews(){
        medicineNameTextView = findViewById(R.id.medicineNameTextValue);
        displayCurrentStock = findViewById(R.id.displayCurrentStockValue);
//        clickToEditStockTextView = findViewById(R.id.clickToEditStockTitleTextView);
    }

    /**
     *
     * @return
     */
    private AlertDialog.Builder showNumberPickerDialog(){
        final NumberPicker picker = new NumberPicker(MedicineViewActivity.this);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        picker.setValue(thisMedicine.getStockNeeded());

        FrameLayout layout = new FrameLayout(MedicineViewActivity.this);
        layout.addView(picker ,new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));


        return new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("Ok", (dialogInterface, i) ->{
                    int newStock = picker.getValue();
                    if(newStock != thisMedicine.getStockNeeded()){
                        thisMedicine.setStockNeeded(newStock);
                        model.updateMed(thisMedicine);
                    }
                })
                .setNegativeButton("Cancel", null);
    }

}