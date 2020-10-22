package com.nhas.NoBabyHosp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhas.NoBabyHosp.Entities.Disease;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.adapter.DiseaseSymptomRecyclerViewAdapter;
import com.nhas.NoBabyHosp.networkconnection.ENTInfoAPI;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DiseaseInfoActivity extends AppCompatActivity {

    private Disease thisDisease;
    private TextView diseaseCommonNameTextView;
    private TextView diseaseScienceNameTextView;

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
        setContentView(R.layout.activity_disease_info);

        Toolbar toolbar = findViewById(R.id.navbar_top);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        thisDisease = (Disease) intent.getSerializableExtra("disease");

        diseaseCommonNameTextView = findViewById(R.id.diseaseCommonNameTextView);
        diseaseCommonNameTextView.setText(thisDisease.getDiseaseCommonName());

        diseaseScienceNameTextView = findViewById(R.id.diseaseScienceNameTextView);
        diseaseScienceNameTextView.setText(thisDisease.getDiseaseScienceName());

        // Query the symptoms and tips info by DID
        GetInfo getInfo = new GetInfo();
        getInfo.execute(String.valueOf(thisDisease.getDiseaseID()));

    }

    private class GetInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String diseaseID = strings[0];
            ENTInfoAPI entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
            //Symptoms query by DID
            return entInfoAPI.listDiseaseSymptomsByDID(diseaseID);
        }


        @Override
        protected void onPostExecute(String results) {
            List<String> symptomList = new ArrayList<>();

            try {
                JSONArray symptomResultJson = new JSONArray(results);
                int length = symptomResultJson.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        symptomList.add(symptomResultJson.getJSONObject(i).getString("SYM_DES"));
                    }

                    RecyclerView symptomRV = findViewById(R.id.symptomRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DiseaseInfoActivity.this);
                    DiseaseSymptomRecyclerViewAdapter adapter = new DiseaseSymptomRecyclerViewAdapter(symptomList, DiseaseInfoActivity.this);
                    symptomRV.setAdapter(adapter);
                    symptomRV.setLayoutManager(layoutManager);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
