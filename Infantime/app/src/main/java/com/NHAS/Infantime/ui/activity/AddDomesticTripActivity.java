package com.NHAS.Infantime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.Facility;
import com.NHAS.Infantime.data.remote.AQIInfoAPI;
import com.NHAS.Infantime.data.remote.ENTInfoAPI;
import com.NHAS.Infantime.data.remote.GoogleMapAPI;
import com.NHAS.Infantime.data.remote.WeatherInfoAPI;
import com.NHAS.Infantime.util.Enum.DomesticTravelEnum;
import com.NHAS.Infantime.util.helpers.AQIWarningModel;
import com.NHAS.Infantime.viewmodel.FacilityViewModel;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;

public class AddDomesticTripActivity extends AppCompatActivity {

    private AutoCompleteTextView destinationEdiText;
    private RelativeLayout detailRelativeLayout;
    private TextView aqiLevelTextView;
    private TextView aqiWarningTextView;
    private ImageButton searchButton;
    private Button generateChecklistButton;
    private TextView currentTemperatureTextView;
    private TextView currentHumidityTextView;


    private String destination;
    private ENTInfoAPI entInfoAPI;
    private List<String> suburb;
    private String[] suburbArray;
    private ArrayAdapter<String> adapter;


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
        setContentView(R.layout.activity_add_domestic_trip);
        this.setTitle("Add DomesticTrip");

        // Set up the back button
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
        suburb = new ArrayList<>();

        findViews();

        // Fetch all suburb and populate the autocomplete textview
        suburbArray = new String[1];
        suburbArray[0] = "Loading....";
        adapter = new ArrayAdapter<>(AddDomesticTripActivity.this,
                R.layout.custom_list_layout, R.id.text_view_list_item, suburbArray);
        destinationEdiText.setAdapter(adapter);
        getSuburbThread().start();

        searchButton.setOnClickListener(v -> {
            destination = destinationEdiText.getText().toString();
            if (destination.trim().isEmpty()) {
                destinationEdiText.setError("Please enter a suburb");
            } else {
                // Hide keyboard on success
                destinationEdiText.setError(null);
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getAQIThread().start();
                getWeatherThread().start();

            }
        }); // End searchButton

        // When the user click Done/Enter, the search button is triggered
        destinationEdiText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    searchButton.performClick();
                }
                return false;
            }
        });

        generateChecklistButton.setOnClickListener(v -> {
            if (!destination.isEmpty()) { // If the user has selected a destination, proceed
                Intent intent = new Intent(AddDomesticTripActivity.this, FacilityListActivity.class);
                Bundle infoBundle = new Bundle();
                infoBundle.putString(DomesticTravelEnum.DESTINATION.toString(), destination);
                intent.putExtra("infoBundle", infoBundle);
                startActivity(intent);
            } else { // Set error at the edittext if no destination is selected
                destinationEdiText.setError("Please provide a destination");
            }
        });
    }

    private Thread getWeatherThread() {
        return new Thread(){
            @Override
            public void run() {
                try{
                    WeatherInfoAPI weatherInfoAPI = new WeatherInfoAPI(getString(R.string.weatherkey));
                    String result = weatherInfoAPI.getCurrentWeather(destination);
                    JSONObject currentWeather = (new JSONObject(result)).getJSONObject("current");

                    int temperature = currentWeather.getInt("temperature");
                    int humidity = currentWeather.getInt("humidity");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currentTemperatureTextView.setText(temperature + "\u2103");
                            currentHumidityTextView.setText(humidity + "%");
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     *
     */
    private void findViews() {
        detailRelativeLayout = findViewById(R.id.detailView);
        destinationEdiText = findViewById(R.id.autoCompleteDestinationTextView);
        searchButton = findViewById(R.id.searchButton);
        aqiLevelTextView = findViewById(R.id.aqiLevelTextView);
        aqiWarningTextView = findViewById(R.id.aqiWarningTextView);
        generateChecklistButton = findViewById(R.id.generateCheckListButton);
        currentHumidityTextView = findViewById(R.id.currentHumidityTextView);
        currentTemperatureTextView = findViewById(R.id.currentTemperatureTextView);
    }


    private Thread getSuburbThread() {
        return new Thread() {
            @Override
            public void run() {
                try {
                    String result = entInfoAPI.listSuburb();
                    JSONArray resultJson = new JSONArray(result);
                    int length = resultJson.length();
                    if (length > 0) {
                        for (int i = 0; i < length; i++) {
                            suburb.add(resultJson.getJSONObject(i).getString("SUBURB"));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                suburbArray = new String[suburb.size()];
                                for (int i = 0; i < suburbArray.length; i++) {
                                    suburbArray[i] = suburb.get(i);
                                }

                                adapter = new ArrayAdapter<>(AddDomesticTripActivity.this,
                                        R.layout.custom_list_layout, R.id.text_view_list_item, suburbArray);
                                destinationEdiText.setAdapter(adapter);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     *
     */
    private Thread getAQIThread() {
        return new Thread() {
            @Override
            public void run() {
                try {
                    String result = entInfoAPI.listCatagoryBySuburb(destination);
                    JSONArray suburb = new JSONArray(result);
                    int length = suburb.length();
                    if (length > 0) {
                        GoogleMapAPI googleMapAPI = new GoogleMapAPI(getString(R.string.geocodekey));
                        AQIInfoAPI aqiInfoAPI = new AQIInfoAPI(getString(R.string.aqikey));

                        // Get the geocode of the suburb
                        LatLng resultLatLng = googleMapAPI.getGeoCode(destination);
                        // Extract lat and long from the result
                        Double lat = resultLatLng.latitude;
                        Double lon = resultLatLng.longitude;

                        // Get AQI from the AQI api
                        String serverity = aqiInfoAPI.getServerity(lat, lon);

                        // Extract the aqi from the result
                        JSONObject resultJson = new JSONObject(serverity);
                        JSONObject data = resultJson.getJSONObject("data");
                        int aqi = data.getInt("aqi");

                        runOnUiThread(() -> {
                            // Set the aqi number
                            aqiLevelTextView.setText(String.valueOf(aqi));

                            // Get the warning statements from the model
                            aqiWarningTextView.setText(AQIWarningModel.getSuggestion(aqi));

                            // Set the color of the text base on AQI
                            int colorCode = 0;
                            if (aqi >= 200) {
                                colorCode = getColor(R.color.warningRed);
                            } else if (aqi >= 150) {
                                colorCode = getColor(R.color.warningRed);
                            } else if (aqi >= 100) {
                                colorCode = getColor(R.color.warningOrange);
                            } else if (aqi >= 67) {
                                colorCode = getColor(R.color.warningOrange);
                            } else if (aqi >= 34) {
                                colorCode = getColor(R.color.warningGreen);
                            } else {
                                colorCode = getColor(R.color.warningGreen);
                            }

                            aqiLevelTextView.setTextColor(colorCode);
                            aqiWarningTextView.setTextColor(colorCode);

                            detailRelativeLayout.setVisibility(View.VISIBLE);
                        }); // End runOnUiThread
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                destinationEdiText.setError("No location found. Please enter a valid suburb name");
                                detailRelativeLayout.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}