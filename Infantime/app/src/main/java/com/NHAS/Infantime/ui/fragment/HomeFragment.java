package com.NHAS.Infantime.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.InternationalTrip;
import com.NHAS.Infantime.data.remote.AQIInfoAPI;
import com.NHAS.Infantime.ui.activity.AddDomesticTripActivity;
import com.NHAS.Infantime.ui.activity.AddInternationalTripActivity;
import com.NHAS.Infantime.ui.activity.InternationalTravelChecklistActivity;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;
import com.NHAS.Infantime.util.helpers.AQIWarningModel;
import com.NHAS.Infantime.viewmodel.InternationalTravelViewModel;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements LocationListener {

    private View view;

    private TextView aqiLocationTextView;
    private TextView aqiTextView;
    private TextView aqiLevelTextView;
    private TextView aqiWarningTextView;
    private CardView tripCardView;

    private Thread getAQIThread;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        // Request current location for AQI data
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            try {
                if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestLocationUpdates(Objects.requireNonNull(locationManager.getBestProvider(criteria, false)), 9000L, 100, this);
        }



        // Display recommendation base on the AQI model
        aqiLocationTextView = view.findViewById(R.id.aqiLocationTextView);
        aqiTextView = view.findViewById(R.id.aqiTextView);
        aqiLevelTextView = view.findViewById(R.id.aqiLevelTextView);
        aqiWarningTextView = view.findViewById(R.id.aqiWarningTextView);

        // Button to create new domesticTrip
        Button activityButton = view.findViewById(R.id.activityButton);
        activityButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDomesticTripActivity.class);
            startActivity(intent);
        });

        // Find the upcoming trip from the database
        InternationalTravelViewModel model = new ViewModelProvider(requireActivity()).get(InternationalTravelViewModel.class);
        model.initializeVars(requireActivity().getApplication());
        model.getAllTrips().observe(getViewLifecycleOwner(), new Observer<List<InternationalTravelWithMedicinesTips>>() {
            @Override
            public void onChanged(List<InternationalTravelWithMedicinesTips> tripList) {
                if(!tripList.isEmpty()){
                    // Get the first trip on the list
                    final InternationalTrip upcomingTrip = tripList.get(0).trip;

                    // Display the name of the trip
                    TextView upcomingTripTextView = view.findViewById(R.id.upcomingTripTextView);
                    upcomingTripTextView.setText(upcomingTrip.getTripName());

                    // Display the start date of the trip
                    TextView tripDateTextView = view.findViewById(R.id.tripDateTextView);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    tripDateTextView.setText(dateFormat.format(upcomingTrip.getStartDate()));

                    tripCardView = view.findViewById(R.id.tripCardView);
                    tripCardView.setOnClickListener((View.OnClickListener) v -> {
                        Intent intent = new Intent(getActivity(), InternationalTravelChecklistActivity.class);
                        intent.putExtra(InternationalTravelEnum.INTERNATIONAL_TRIP.toString(), (Serializable) upcomingTrip);
                        intent.putExtra("requestCode", 1);
                        startActivity(intent);
                    });
                }
            }
        });


        // Button to create new trip
        Button tripButton = view.findViewById(R.id.tripButton);
        tripButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddInternationalTripActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Get the AQI info
        setGetAQIThreadgetAQI(location.getLatitude(), location.getLongitude());
        getAQIThread.start();
    }

    @Override
    @Deprecated
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getAQIThread != null) {
            if (getAQIThread.isAlive()) {
                getAQIThread.interrupt();
            }
        }
    }

    private void setGetAQIThreadgetAQI(double lat, double lon){
        getAQIThread = new Thread(){
            @Override
            public void run() {
                try {
                    AQIInfoAPI api = new AQIInfoAPI(getString(R.string.aqikey));
                    String result = api.getServerity(lat, lon);
                    JSONObject resultJson = new JSONObject(result);
                    JSONObject data = resultJson.getJSONObject("data");

                    // Set the aqi number
                    int aqi = data.getInt("aqi");
                    // Set the city name
                    String city = data.getJSONObject("city").getString("name").split(",")[0];

                    requireActivity().runOnUiThread(() -> {
                        // Get the warning statements from the model
                        aqiLevelTextView.setText(AQIWarningModel.getServerity(aqi));
                        aqiTextView.setText(String.valueOf(aqi));
                        aqiLocationTextView.setText(city);
                        aqiWarningTextView.setText(AQIWarningModel.getSuggestion(aqi));

                        int color = 0;
                        // Set the color of the text base on AQI
                        if (aqi >= 200){
                            color = getContext().getColor(R.color.warningRed);
                        } else if ( aqi >= 150) {
                            color = getContext().getColor(R.color.warningRed);
                        } else if ( aqi >= 100) {
                            color = getContext().getColor(R.color.warningOrange);
                        } else if ( aqi >= 67) {
                            color = getContext().getColor(R.color.warningOrange);
                        } else if ( aqi >= 34) {
                            color = getContext().getColor(R.color.warningGreen);
                        } else if ( aqi >= 0) {
                            color = getContext().getColor(R.color.warningGreen);
                        }
                        aqiTextView.setTextColor(color);
                        aqiLevelTextView.setTextColor(color);
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}
