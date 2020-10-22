package com.nhas.NoBabyHosp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhas.NoBabyHosp.R;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity {

    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioBtn;
    private Spinner ageSpinner;
    private EditText nameEditText;
    private Button confirmBtn;

    private ArrayAdapter<String> ageSpinnerAdapter;

    private int age;
    private String babyName;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        // Create an adapter for the spinner
        ageSpinner = findViewById(R.id.ageSpinner);
        final List<String> ageArray = new ArrayList<>();
        ageArray.add("Please select an age group");
        ageArray.add("0-6M");
        ageArray.add("6-12M");
        ageArray.add("12-24M");
        ageArray.add("3Y");
        ageArray.add("4Y");

        ageSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, ageArray);
        ageSpinner.setAdapter(ageSpinnerAdapter);

        //Onclick start button
        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position != 0){
                            int selectedAge = Integer.parseInt(parent.getItemAtPosition(position).toString());
                            age = selectedAge;
                        }else{
                            Toast.makeText(LandingActivity.this, "Please pick an age for your baby.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //Not calling this method
                    }
                });

                // Get the info for the name
                nameEditText = findViewById(R.id.babyNameEditText);
                babyName = nameEditText.getText().toString();

                checkGenderButton(v);
                if(!babyName.trim().isEmpty()){
                    //Get the info for the gender
                    checkGenderButton(v);
                    if(gender != null) {
                        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spEditor = sharedPreferences.edit();
                        spEditor.putString("NAME", babyName);
                        spEditor.putString("GENDER", gender);
                        spEditor.putInt("AGE", age);
                        spEditor.putBoolean("isFirstRun", false);
                        spEditor.apply();

                        //Generate tasks and save to shared preference as TASK

                        Intent intent = getIntent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }else{
                    nameEditText.setError("Please enter the baby's name.");
                }
            }
        });
    }

    //Get the selected gender
    protected void checkGenderButton(View v){
        int radioId = genderRadioGroup.getCheckedRadioButtonId();
        try{
            genderRadioBtn = findViewById(radioId);
            String temp = genderRadioBtn.getText().toString();
            if(temp.equals("It's a boy")){
                gender = "M";
            }else if (temp.equals("It's a girl")){
                gender = "F";
            }
        } catch (Exception e){
            Toast.makeText(LandingActivity.this, "Please select a gender.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}