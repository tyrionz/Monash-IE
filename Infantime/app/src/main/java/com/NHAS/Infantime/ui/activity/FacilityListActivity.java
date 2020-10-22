package com.NHAS.Infantime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.FacilityRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.Facility;
import com.NHAS.Infantime.data.remote.ENTInfoAPI;
import com.NHAS.Infantime.util.Enum.DomesticTravelEnum;
import com.NHAS.Infantime.viewmodel.FacilityViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FacilityListActivity extends AppCompatActivity {

    private ENTInfoAPI entInfoAPI;

    private FacilityViewModel facilityViewModel;

    private String selectedCategory;
    private List<Facility> facilityList;
    private List<String> categoryList;
    private ArrayAdapter<String> catagoryAdapter;

    private Spinner possibleActivitySpinner;
    private RecyclerView activityRecyclerView;
    private Button nextButton;
    private TextView viewPlacesTitleTextView;


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
        setContentView(R.layout.activity_facility_list);
        this.setTitle("Add Category");
        Intent intent = getIntent();
        Bundle infoBundle = intent.getBundleExtra("infoBundle");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
        facilityList = new ArrayList<>();

        findViews();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacilityListActivity.this);
        FacilityRecyclerViewAdapter adapter = new FacilityRecyclerViewAdapter(facilityList, FacilityListActivity.this);

        activityRecyclerView.setAdapter(adapter);
        activityRecyclerView.setLayoutManager(layoutManager);
        activityRecyclerView.addItemDecoration(new DividerItemDecoration(FacilityListActivity.this, LinearLayoutManager.VERTICAL));

        facilityViewModel = new ViewModelProvider(FacilityListActivity.this).get(FacilityViewModel.class);
        facilityViewModel.getFacilityList().observe(this, strings -> {
            facilityList = strings;
            activityRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        String destination = infoBundle.getString(DomesticTravelEnum.DESTINATION.toString());
        getCategoryThread(destination).start();

        // Populate the spinner
        // Populate the recycler view
        categoryList = new ArrayList<>();
        facilityList.clear();

        catagoryAdapter = new ArrayAdapter<>(FacilityListActivity.this, android.R.layout.simple_dropdown_item_1line, categoryList);
        possibleActivitySpinner.setAdapter(catagoryAdapter);
        possibleActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facilityList.clear();
                if (position != 0) {
                    selectedCategory = parent.getItemAtPosition(position).toString();
                    getFacilityThread(destination).start();
                } else {
                    selectedCategory = "";
                    viewPlacesTitleTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not called
            }
        });


        nextButton.setOnClickListener(v -> {
            if (!selectedCategory.isEmpty()) { // If the user has selected a category of place, proceed
                Intent medicineIntent = new Intent(FacilityListActivity.this, DomesticTravelChecklistActivity.class);
                infoBundle.putString(DomesticTravelEnum.CATEGORY.toString(), selectedCategory);
                medicineIntent.putExtra("infoBundle", infoBundle);
                startActivity(medicineIntent);
            } else { // Set error at the spinner if no item is selected
                TextView errorText = (TextView) possibleActivitySpinner.getSelectedView();
                errorText.setError("Please pick a category");
                errorText.setTextColor(Color.RED);
                errorText.setText("Please pick a category");
            }
        });
    }


    /**
     *
     */
    private void findViews() {
        activityRecyclerView = findViewById(R.id.activityRecyclerView);
        possibleActivitySpinner = findViewById(R.id.possibleActivitySpinner);
        activityRecyclerView = findViewById(R.id.activityRecyclerView);
        nextButton = findViewById(R.id.generateCheckListButton);
        viewPlacesTitleTextView = findViewById(R.id.viewPlacesTitleTextView);
    }

    /**
     *
     */
    private Thread getCategoryThread(String destination) {
        return new Thread(() -> {
            try {
                String result = entInfoAPI.listCatagoryBySuburb(destination);
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if (length > 0) {
                    categoryList.add("Pick a category");
                    for (int i = 0; i < length; i++) {
                        JSONObject thisCategory = resultJson.getJSONObject(i);
                        categoryList.add(thisCategory.getString("CATEGORY"));
                    }

                    // Notify ui change on success
                    runOnUiThread(() -> {
                        catagoryAdapter.notifyDataSetChanged();
                        possibleActivitySpinner.setVisibility(View.VISIBLE);
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     */
    private Thread getFacilityThread(String destination) {
        return new Thread() {
            @Override
            public void run() {
                String result = entInfoAPI.listFacilityByCatagoryAndSuburb(selectedCategory, destination);
                try {
                    JSONArray resultJson = new JSONArray(result);
                    int length = resultJson.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            JSONObject thisFacility = resultJson.getJSONObject(i);
                            facilityList.add(new Facility(thisFacility.getString("PARK_NAME"), thisFacility.getString("ADDRESS")));
                            facilityViewModel.setFacilityNameList(facilityList);
                        }

                        runOnUiThread(() -> viewPlacesTitleTextView.setVisibility(View.VISIBLE));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}