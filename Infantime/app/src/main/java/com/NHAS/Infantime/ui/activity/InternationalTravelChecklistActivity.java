package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.InternationalTipRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;
import com.NHAS.Infantime.viewmodel.InternationalTipViewModel;
import com.NHAS.Infantime.viewmodel.InternationalTravelViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class InternationalTravelChecklistActivity extends AppCompatActivity implements InternationalTipRecyclerViewAdapter.CheckTask{

    private InternationalTipViewModel tipViewModel;
    private InternationalTravelViewModel internationalTravelViewModel;
    private InternationalTrip thisTrip;
    private HashMap<String, List<InternationalTip>> allTasks;
    private List<InternationalTip> filteredTips;
    private RecyclerView tipRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private InternationalTipRecyclerViewAdapter adapter;
    private SegmentedButtonGroup sbg;
    private Button saveButton;
    private EditText tripNameEditText;
    private String tripName;
    private boolean firstTime;
    private Button viewMedicineButton;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        this.setTitle("Checklist");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle infoBundle = intent.getBundleExtra("infoBundle");

        allTasks = new HashMap<>();
        allTasks.put("Pre", new ArrayList<>());
        allTasks.put("During", new ArrayList<>());
        allTasks.put("Post", new ArrayList<>());
        filteredTips = new ArrayList<>();
        firstTime = true;

        // Setup the UI and the view model
        findViews();
        initialiseRecyclerView();
        initialiseViewModel();

        // Change the filtered list base on the selected tab
        // Cases: 0 - Before, 1 - During, 2 - After
        // HashKey: - Pre,      - During,   - Post
        // Then notify the recyclerview to repopulate it
        sbg.setPosition(0, 0);
        sbg.setOnClickedButtonPosition(position -> {
            filteredTips.clear();
            switch (position){
                case 0:
                    filteredTips.addAll(allTasks.get("Pre"));
                    break;

                case 1:
                    filteredTips.addAll(allTasks.get("During"));
                    break;

                case 2:
                    filteredTips.addAll(allTasks.get("Post"));
                    break;

            }
            adapter.notifyDataSetChanged();
        });


        int requestCode = intent.getIntExtra("requestCode", 0);
        // Called by the add trip flow
        if (requestCode == 0) {
            tipViewModel.getMutableTipList().observe(InternationalTravelChecklistActivity.this, tipList -> {
                for(InternationalTip tip : tipList){
                    String key = tip.getWhen();
                    if(key.equals("null")){
                        key = "During";
                    }
                    allTasks.get(key).add(tip);
                }

                if (firstTime) {
                    filteredTips.clear();
                    filteredTips.addAll(allTasks.get("Pre"));
                    sbg.setPosition(0, 0);
                    firstTime = false;
                }
                adapter.notifyDataSetChanged();
            });


            String travelMode = infoBundle.getString(InternationalTravelEnum.MODE_OF_TRAVEL.toString());
            // For iteration2.5 purpose
            assert travelMode != null;
            if(!travelMode.equals("Air")){
                travelMode = "General";
            }
            getTipsByType(travelMode);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tripName = "";
                    tripName = tripNameEditText.getText().toString();

                    if (!tripName.trim().isEmpty() && !allTasks.isEmpty()) {
                        // Convert the hashmap to arraylist
                        List<InternationalTip> tipList = hashmapToArrayList(allTasks);
                        String location = infoBundle.getString(InternationalTravelEnum.LOCATION.toString());
                        long startDate = infoBundle.getLong(InternationalTravelEnum.START_DATE.toString(), 0);
                        long endDate = infoBundle.getLong(InternationalTravelEnum.END_DATE.toString(), 0);
                        @SuppressWarnings("unchecked")
                        List<Medicine> medicineList = (ArrayList<Medicine>) infoBundle.getSerializable(InternationalTravelEnum.MEDICINE_LIST.toString());

                        InternationalTravelWithMedicinesTips trip = new InternationalTravelWithMedicinesTips(
                                new InternationalTrip(tripName, location, new Date(startDate), new Date(endDate)),
                                medicineList, tipList);

                        internationalTravelViewModel.insertTrip(trip);

                        // Back to home page
                        Intent intent = new Intent(InternationalTravelChecklistActivity.this, BottomNavHomeActivity.class);
                        startActivity(intent);
                    } else if (tripName.trim().isEmpty()) {
                        tripNameEditText.setError("Name must not be empty.");
                    }

                }
            });
        } else {
            // Disable save button
            saveButton.setVisibility(View.INVISIBLE);
            saveButton.setEnabled(false);

            // Setup recyclerview
            thisTrip = (InternationalTrip) intent.getSerializableExtra(InternationalTravelEnum.INTERNATIONAL_TRIP.toString());
            TextView tripNameTextView = findViewById(R.id.tripNameTextView);
            tripNameTextView.setText(thisTrip.getTripName());
            tripNameEditText.setVisibility(View.INVISIBLE);
            tripNameEditText.setFocusable(false);
            tripNameEditText.setEnabled(false);

            tipViewModel.getInternationalTipByTid(thisTrip.getTripID()).observe(InternationalTravelChecklistActivity.this, new Observer<List<InternationalTip>>() {
                @Override
                public void onChanged(List<InternationalTip> tips) {
                    for(InternationalTip tip : tips){
                        String key = tip.getWhen();
                        if(key.equals("null")){
                            key = "During";
                        }
                        allTasks.get(key).add(tip);
                    }

                    if (firstTime) {
                        filteredTips.clear();
                        filteredTips.addAll(allTasks.get("Pre"));
                        sbg.setPosition(0, 0);
                        firstTime = false;
                    }
                }
            });

            // Setup view medicine button
            viewMedicineButton.setEnabled(true);
            viewMedicineButton.setVisibility(View.VISIBLE);
            viewMedicineButton.setOnClickListener(v -> {
                Intent medicineIntent = new Intent(InternationalTravelChecklistActivity.this, MedicineListActivity.class);
                medicineIntent.putExtra(InternationalTravelEnum.INTERNATIONAL_TRIP.toString(), thisTrip);
                medicineIntent.putExtra(InternationalTravelEnum.START_DATE.toString(), thisTrip.getStartDate().getTime());
                medicineIntent.putExtra(InternationalTravelEnum.END_DATE.toString(), thisTrip.getEndDate().getTime());
                medicineIntent.putExtra(InternationalTravelEnum.TRIP_ID.toString(), thisTrip.getTripID());
                medicineIntent.putExtra("requestCode", 1);
                startActivity(medicineIntent);
            });

        }
    }


    /**
     *
     */
    private void initialiseRecyclerView(){
        layoutManager = new LinearLayoutManager(InternationalTravelChecklistActivity.this);
        filteredTips = new ArrayList<>();
        adapter = new InternationalTipRecyclerViewAdapter(filteredTips, InternationalTravelChecklistActivity.this);
        tipRecyclerView.setAdapter(adapter);
        tipRecyclerView.setLayoutManager(layoutManager);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<InternationalTip> hashmapToArrayList(HashMap<String, List<InternationalTip>> tipHashMap){
        List<InternationalTip> tipList = new ArrayList<>();
        Iterator it = tipHashMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            tipList.addAll((Collection<? extends InternationalTip>) pair.getValue());
            it.remove();
        }
        return tipList;
    }

    private void initialiseViewModel() {
        // Setup the trip view model
        internationalTravelViewModel = new ViewModelProvider(InternationalTravelChecklistActivity.this).get(InternationalTravelViewModel.class);
        internationalTravelViewModel.initializeVars(getApplication());

        // Setup the tip view model
        tipViewModel = new ViewModelProvider(InternationalTravelChecklistActivity.this).get(InternationalTipViewModel.class);
        tipViewModel.initializeVars(getApplication());
    }

    private void findViews(){
        tipRecyclerView = findViewById(R.id.tipsRecyclerview);
        tripNameEditText = findViewById(R.id.tripNameEditText);
        saveButton = findViewById(R.id.saveButton);
        tipRecyclerView = findViewById(R.id.tipsRecyclerview);
        sbg = findViewById(R.id.segmentedControlButton);
        viewMedicineButton = findViewById(R.id.viewMedicineButton);
    }

    @Override
    public void checkTask(InternationalTip tip, String taskType, Boolean isChecked) {
        if(tip.getWhen().equals(taskType)) {
            tip.setDone(isChecked);
            tipViewModel.updateInternationalTip(tip);
        }
    }

    private void getTipsByType(String type){
        new Thread(){

            @Override
            public void run() {
                tipViewModel.fetchTipsByType(type, getString(R.string.entinfokey));
            }
        }.start();
    }
}
