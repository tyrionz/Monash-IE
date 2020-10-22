package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddInternationalTripActivity extends AppCompatActivity {

    private EditText locationEditText;
    private TextView dateOfTravelTextView;
    private RadioGroup travelRadioGroup;
    private Button generateCheckListButton;
    private String travelMode;
    private Date startDate;
    private Date endDate;

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
        setContentView(R.layout.activity_add_international_trip);
        this.setTitle("Add Trip");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();

        // Setup the date picker widget
        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        // Disable date before today
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        constraintBuilder.setStart(Calendar.getInstance().getTimeInMillis());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker(); // Set the builder to build a dateRangePicker
        builder.setTitleText("Select the travel range");
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
        builder.setCalendarConstraints(constraintBuilder.build()); // Set the constraint for the date picker
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();


        dateOfTravelTextView.setOnClickListener(v -> {
            if(!materialDatePicker.isAdded() && !materialDatePicker.isVisible()) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }

            dateOfTravelTextView.setError(null);
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
                // Get the start date
                try {
                    startDate = new Date(selection.first);
                } catch (NullPointerException e) {
                    setTextViewError(dateOfTravelTextView, "Please select a start date");
                }

                // Get the end date
                try {
                    endDate = new Date(selection.second);
                } catch (NullPointerException e) {
                    setTextViewError(dateOfTravelTextView, "Please select an end date");
                }

                String dateString = dateFormat.format(startDate) + " - " + dateFormat.format(endDate);
                dateOfTravelTextView.setText(dateString);

        }); // End material date picker onPositiveButtonClickListener
        }); // End TextView onClickListener


        // Generate checklist

        generateCheckListButton.setOnClickListener(v -> {
            // Get the location
            String location = locationEditText.getText().toString();

            // Get mode of travel
            checkTravelModeButton();

            if(!location.trim().isEmpty()){ // If the location is not empty
                if(startDate != null && endDate != null){ // If the start date and end date are selected
                    if(travelMode != null){ // If the travel mode is selected
                        // Proceed to the next domesticTrip (Add medicine)
                        Intent intent = new Intent(AddInternationalTripActivity.this, MedicineListActivity.class);
                        Bundle infoBundle = new Bundle();
                        infoBundle.putString(InternationalTravelEnum.LOCATION.toString(), location);
                        infoBundle.putLong(InternationalTravelEnum.START_DATE.toString(), startDate.getTime());
                        infoBundle.putLong(InternationalTravelEnum.END_DATE.toString(), endDate.getTime());
                        infoBundle.putString(InternationalTravelEnum.MODE_OF_TRAVEL.toString(), travelMode);
                        intent.putExtra("infoBundle", infoBundle);
                        startActivity(intent);
                    }
                }

                // If the start date is not selected
                if(startDate == null){
                    setTextViewError(dateOfTravelTextView, "Please select a start date.");
                }

                // If the end date is not selected
                if (endDate == null){
                    setTextViewError(dateOfTravelTextView, "Please select an end date.");
                }
            }else{ // If the location is empty
                locationEditText.setError("Location must not be empty");
            }

        });
    }

    private void findViews(){
        locationEditText = findViewById(R.id.locationEditText);
        dateOfTravelTextView = findViewById(R.id.dateOfTravelTextView);
        generateCheckListButton = findViewById(R.id.generateCheckListButton);
        locationEditText = findViewById(R.id.locationEditText);
        travelRadioGroup = findViewById(R.id.travelRadioGroup);
    }

    // Set error on TextView
    private void setTextViewError(TextView thisTextView, String errorMessage){
        thisTextView.requestFocus();
        thisTextView.setError(errorMessage);
    }

    //Get the selected travel mode
    private void checkTravelModeButton(){
        int radioId = travelRadioGroup.getCheckedRadioButtonId();
        try{
            RadioButton travelModeRadioButton = findViewById(radioId);
            String temp = travelModeRadioButton.getText().toString();
            switch (temp) {
                case "Air":
                    travelMode = "Air";
                    break;
                case "Boat":
                    travelMode = "Boat";
                    break;
                case "Road":
                    travelMode = "Road";
                    break;
            }
        } catch (Exception e){
            Toast.makeText(AddInternationalTripActivity.this, "Please select a Mode of travel.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}