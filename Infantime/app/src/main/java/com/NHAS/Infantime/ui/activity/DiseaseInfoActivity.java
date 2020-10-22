package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.DiseaseSymptomRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.Disease;
import com.NHAS.Infantime.data.remote.ENTInfoAPI;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DiseaseInfoActivity extends AppCompatActivity {

    private ShimmerFrameLayout shimmer;
    private TextView diseaseCommonNameTextView;
    private TextView diseaseScienceNameTextView;
    private RecyclerView symptomRV;

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
        setContentView(R.layout.activity_disease_info);
        this.setTitle("Disease");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();

        Intent intent = getIntent();
        Disease thisDisease = (Disease) intent.getSerializableExtra("disease");

        // Set the common name and the science name as the title and subtitle
        diseaseCommonNameTextView.setText(thisDisease.getDiseaseCommonName());
        diseaseScienceNameTextView.setText(thisDisease.getDiseaseScienceName());

        // Query the symptoms and tips info by DID
        getInfo(String.valueOf(thisDisease.getDiseaseID()));

    }

    private void getInfo(String diseaseIDString) {
        new Thread() {
            @Override
            public void run() {
                try {
                    // Start the shimmer loading animation while loading
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shimmer.setVisibility(View.VISIBLE);
                            shimmer.startShimmerAnimation();
                        }
                    });

                    ENTInfoAPI entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
                    String results = entInfoAPI.listDiseaseSymptomsByDID(diseaseIDString);
                    List<String> symptomList = new ArrayList<>();
                    JSONArray symptomResultJson = new JSONArray(results);
                    // Extract the symptom list from the result
                    /**
                     *
                     */
                    int length = symptomResultJson.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            symptomList.add(symptomResultJson.getJSONObject(i).getString("SYM_DES"));
                        }

                        runOnUiThread(() -> {
                            inflateRecyclerView(symptomList);
                            shimmer.stopShimmerAnimation();
                            shimmer.setVisibility(View.GONE);
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     *
     */
    private void findViews(){
        shimmer = findViewById(R.id.shimmer_layout);
        diseaseCommonNameTextView = findViewById(R.id.diseaseCommonNameTextView);
        diseaseScienceNameTextView = findViewById(R.id.diseaseScienceNameTextView);
        symptomRV = findViewById(R.id.symptomRecyclerView);
    }

    /**
     *
     * @param symptomList
     */
    private void inflateRecyclerView(List<String> symptomList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DiseaseInfoActivity.this);
        DiseaseSymptomRecyclerViewAdapter adapter = new DiseaseSymptomRecyclerViewAdapter(symptomList);
        symptomRV.setAdapter(adapter);
        symptomRV.setLayoutManager(layoutManager);
        symptomRV.addItemDecoration(new DividerItemDecoration(DiseaseInfoActivity.this, LinearLayoutManager.VERTICAL));
    }
}
