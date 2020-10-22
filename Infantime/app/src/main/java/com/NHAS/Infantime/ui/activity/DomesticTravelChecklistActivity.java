package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.DomesticTipRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.entities.DomesticTravelWithTips;
import com.NHAS.Infantime.data.entities.DomesticTrip;
import com.NHAS.Infantime.util.Enum.DomesticTravelEnum;
import com.NHAS.Infantime.viewmodel.DomesticTipViewModel;
import com.NHAS.Infantime.viewmodel.DomesticTravelViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;

public class DomesticTravelChecklistActivity extends AppCompatActivity implements DomesticTipRecyclerViewAdapter.CheckTask{

    private DomesticTipViewModel tipViewModel;
    private DomesticTravelViewModel domesticTravelViewModel;
    private DomesticTrip thisDomesticTrip;
    private HashMap<String, List<DomesticTip>> allTasks;
    private List<DomesticTip> filteredTips;
    private RecyclerView tipRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DomesticTipRecyclerViewAdapter adapter;
    private SegmentedButtonGroup sbg;
    private Button saveButton;
    private TextView tripNameEditText;
    private String tripName;
    private boolean firstTime;

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
        setContentView(R.layout.activity_checklist);
        this.setTitle("Checklist");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        firstTime = true;
        allTasks = new HashMap<>();
        allTasks.put("Pre", new ArrayList<>());
        allTasks.put("During", new ArrayList<>());
        allTasks.put("Post", new ArrayList<>());

        filteredTips = new ArrayList<>();

        // Setup the UI and view models
        findViews();
        initialiseRecyclerView();
        initialiseViewModel();


        // Change the filteredTask list base on the selected tab,
        // then repopulate the recyclerview
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
        // Calling from the add domesticTrip flow
        if (requestCode == 0) {
            Bundle infoBundle = intent.getBundleExtra("infoBundle");

            // Start observing the list of tips
            tipViewModel.getTipList().observe(DomesticTravelChecklistActivity.this, tipList -> {
                updateTipLists(tipList);
                adapter.notifyDataSetChanged();
            });

            tripNameEditText.setText(infoBundle.getString(DomesticTravelEnum.DESTINATION.toString()) + " " + infoBundle.getString(DomesticTravelEnum.CATEGORY.toString()) + " trip");

            String category = infoBundle.getString(DomesticTravelEnum.CATEGORY.toString());
            String activityType = "";
            if (category.toLowerCase().equals("swimming pool")){
                activityType = "Swimming";
            }else{
                activityType = "General";
            }
            getTipsByTypeThread(activityType).start();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the name of the trip
                    tripName = tripNameEditText.getText().toString();

                    // If the trip name is not empty and the tipList is not empty as well, proceed to save it
                    if(!tripName.trim().isEmpty() && !allTasks.isEmpty()){
                        // Get the destination of the domesticTrip
                        String destination = infoBundle.getString(DomesticTravelEnum.DESTINATION.toString());

                        // Convert the hashmap to arraylist
                        List<DomesticTip> tipList = hashmapToArrayList(allTasks);

                        DomesticTravelWithTips thisDomesticTrip = new DomesticTravelWithTips(
                                new DomesticTrip(tripName, destination), tipList);

                        domesticTravelViewModel.insertTrip(thisDomesticTrip);

                        // Back to home page
                        Intent intent = new Intent(DomesticTravelChecklistActivity.this, BottomNavHomeActivity.class);
                        startActivity(intent);
                    } else if (tripName.trim().isEmpty()){ // Set error at the EditText if the name of domesticTrip is not set
                        tripNameEditText.setError("Name must not be empty.");
                    }

                }
            });
        } else { // Calling from the view domesticTrip flow
            // Disable and hide the save button
            saveButton.setVisibility(View.GONE);
            saveButton.setEnabled(false);

            // Get the domesticTrip object
            thisDomesticTrip = (DomesticTrip) intent.getSerializableExtra(DomesticTravelEnum.DOMESTIC_TRIP.toString());

            // Disable the EditText
            TextView tripNameTextView = findViewById(R.id.tripNameTextView);
            tripNameTextView.setText(thisDomesticTrip.getTripName());
            tripNameEditText.setVisibility(View.INVISIBLE);
            tripNameEditText.setFocusable(false);
            tripNameEditText.setEnabled(false);

            // Populate the ViewModel
            tipViewModel.getDomesticTipsByAid(thisDomesticTrip.getTripID()).observe(DomesticTravelChecklistActivity.this, tipList -> {
                updateTipLists(tipList);
                adapter.notifyDataSetChanged();
            });

        }
    }

    private void updateTipLists(List<DomesticTip> tipList){
        allTasks.get("Pre").clear();
        allTasks.get("During").clear();
        allTasks.get("Post").clear();

        for(DomesticTip tip : tipList){
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

    /**
     *
     */
    private void initialiseRecyclerView(){
        layoutManager = new LinearLayoutManager(DomesticTravelChecklistActivity.this);
        filteredTips = new ArrayList<>();
        adapter = new DomesticTipRecyclerViewAdapter(filteredTips, DomesticTravelChecklistActivity.this);
        tipRecyclerView.setAdapter(adapter);
        tipRecyclerView.setLayoutManager(layoutManager);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<DomesticTip> hashmapToArrayList(HashMap<String, List<DomesticTip>> tipHashMap){
        List<DomesticTip> tipList = new ArrayList<>();
        Iterator it = tipHashMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            tipList.addAll((Collection<? extends DomesticTip>) pair.getValue());
            it.remove();
        }
        return tipList;
    }

    /**
     *
     */
    @Override
    public void checkTask(DomesticTip thisTip, String taskType, Boolean isChecked) {
        if(thisTip.getWhen().equals(taskType)) {
            thisTip.setDone(isChecked);
            tipViewModel.updateDomesticTip(thisTip);
        }
    }

    /**
     *
     */
    private void findViews(){
        tripNameEditText = findViewById(R.id.tripNameEditText);
        saveButton = findViewById(R.id.saveButton);
        tipRecyclerView = findViewById(R.id.tipsRecyclerview);
        sbg = findViewById(R.id.segmentedControlButton);
    }

    /**
     *
     */
    private void initialiseViewModel(){
        // Setup the domesticTrip view model
        domesticTravelViewModel = new ViewModelProvider(DomesticTravelChecklistActivity.this).get(DomesticTravelViewModel.class);
        domesticTravelViewModel.initializeVars(getApplication());

        // Setup the tip view model
        tipViewModel = new ViewModelProvider(DomesticTravelChecklistActivity.this).get(DomesticTipViewModel.class);
        tipViewModel.initializeVars(getApplication());
    }

    /**
     *
     */
    private Thread getTipsByTypeThread(String type){
        return new Thread(() -> tipViewModel.fetchTipsByType(type, getString(R.string.entinfokey)));
    }

}