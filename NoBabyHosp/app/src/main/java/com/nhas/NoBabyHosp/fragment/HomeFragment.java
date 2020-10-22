package com.nhas.NoBabyHosp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.activity.MedicineViewActivity;
import com.nhas.NoBabyHosp.activity.NavHomeActivity;
import com.nhas.NoBabyHosp.activity.TaskViewActivity;
import com.nhas.NoBabyHosp.model.MedicineViewModel;
import com.nhas.NoBabyHosp.model.TaskViewModel;
import com.nhas.NoBabyHosp.networkconnection.AQIInfoAPI;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements LocationListener {

    private View view = null;
    private TextView homeTextView;
    private TextView locationTextView;
    private TextView aqiTextView;
    private TextView aqiLevelTextView;
    private TextView dateTextView;
    private TextView warningTextView;
    private TextView taskNumberTextView;
    private TextView medicineNameTextView;
    private CardView checkTaskCardView;
    private TextView expiryDateView;
    private CardView checkMedicineCardView;
    private TextView taskNameTextView;
    private MenuItem taskMenuItem;
    private Location location;
    private List<Task> taskList;
    private List<Medicine> medicineList;

    private LocationManager locationManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_home_page_new, container, false);

        DateFormat dateFormat = new SimpleDateFormat("dd / MM", Locale.getDefault());
        String todayString = dateFormat.format(Calendar.getInstance().getTime());
        dateTextView = view.findViewById(R.id.taskDateTextView);
        expiryDateView = view.findViewById(R.id.expiryDateFieldTitleView);
        dateTextView.setText(todayString);
        expiryDateView.setText(todayString);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            try {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 900000L, 1000, this);
            if (locationManager != null){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }


        aqiTextView = view.findViewById(R.id.aqiTextView);
        aqiLevelTextView = view.findViewById(R.id.aqiLevelTextView);
        warningTextView = view.findViewById(R.id.warningTextView);
        locationTextView = view.findViewById(R.id.aqiLocationTextView);
        taskNameTextView = view.findViewById(R.id.taskNameTextView);
        medicineNameTextView = view.findViewById(R.id.medicineNameTextView);

        //Name the user
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        String userBabyName = sharedPreferences.getString("NAME", "User");
        homeTextView = view.findViewById(R.id.homeTextView);
        homeTextView.setText(userBabyName + "'s home");

        TaskViewModel model = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        model.initializeVars(getActivity().getApplication());
        Date today = Calendar.getInstance().getTime();
        Log.i("Query", String.valueOf(today.getTime()));
        model.getTaskByDate(today).observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskList = tasks;
                if(tasks.size() > 0) {
                    taskNameTextView.setText(tasks.get(0).getTaskDescription());
                } else {
                    taskNameTextView.setVisibility(View.GONE);
                }
                taskNumberTextView = view.findViewById(R.id.taskDueWatningTextView);
                taskNumberTextView.setText("There are " + tasks.size() + " task(s) due today.");
            }
        });

        checkTaskCardView = view.findViewById(R.id.taskCardView);
        checkTaskCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskList.size() > 0) {
                    Intent intent = new Intent(getActivity(), TaskViewActivity.class);
                    intent.putExtra("task", taskList.get(0));
                    startActivity(intent);
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, new TaskFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        MedicineViewModel medicineViewModel = new ViewModelProvider(requireActivity()).get(MedicineViewModel.class);
        medicineViewModel.initializeVars(getActivity().getApplication());
        medicineViewModel.getAllMedicines().observe(getViewLifecycleOwner(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                medicineList = medicines;
                if(medicines.size() > 0) {
                    medicineNameTextView.setText(medicines.get(0).getMedDescription());
                } else {
                    medicineNameTextView.setVisibility(View.GONE);
                }
            }
        });

        checkMedicineCardView = view.findViewById(R.id.medicalStockCardView);
        checkMedicineCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medicineList.size() > 0) {
                    Intent intent = new Intent(getActivity(), MedicineViewActivity.class);
                    intent.putExtra("medicine", medicineList.get(0));
                    startActivity(intent);
                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, new MedicineFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Get the AQI info
        GetAQITask getAQITask = new GetAQITask();
        getAQITask.execute(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetAQITask extends AsyncTask<Double, Void, String>{

        @Override
        protected String doInBackground(Double... geoCodes) {
            AQIInfoAPI api = new AQIInfoAPI();
            return api.getServerity(geoCodes[0], geoCodes[1]);
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject resultJson = new JSONObject(result);
                JSONObject data = resultJson.getJSONObject("data");

                // Set the aqi number
                int aqi = data.getInt("aqi");
                aqiTextView.setText(Integer.toString(aqi));

                // Set the city name
                String city = data.getJSONObject("city").getString("name");
                locationTextView.setText(city);


                // Set the severity
                String severity = "";
                String suggestion = "";
                if (aqi >= 200){
                    severity = "Hazardous";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Reschedule outdoor activities with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to red
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningRed));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if ( aqi >= 150) {
                    severity = "Very poor";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Reschedule outdoor activities with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to red
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningRed));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if ( aqi >= 100) {
                    severity = "Poor";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Not recommend outdoor activities with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to red
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningRed));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if ( aqi >= 67) {
                    severity = "Fair";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Reduce or reschedule outdoor activities with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to yellow
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningYellow));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if ( aqi >= 34) {
                    severity = "Good";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Safe to go out with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to yellow
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningYellow));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else if ( aqi >= 0) {
                    severity = "Very good";
                    aqiLevelTextView.setText(severity);
                    suggestion = "Safe to go out with infants";
                    warningTextView.setText(suggestion);
                    // Change colour of the text to yellow
                    try {
                        aqiLevelTextView.setTextColor(getContext().getColor(R.color.warningYellow));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}