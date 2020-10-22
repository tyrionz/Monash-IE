package com.nhas.NoBabyHosp.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.nhas.NoBabyHosp.Entities.Disease;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.adapter.DiseaseRecyclerViewAdapter;
import com.nhas.NoBabyHosp.model.DiseaseViewModel;
import com.nhas.NoBabyHosp.networkconnection.ENTInfoAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class TipsAndInfoFragment extends Fragment {
    private Spinner earAreaSpinner;
    private ArrayAdapter<String> earAreaSpinnerAdapter;
    private Button submitBtn;
    private String earArea;

    private Spinner symptomSpinner;
    private ArrayAdapter<String> symptomSpinnerAdapter;
    private String symptoms;
    private String symptomName;
    private EditText symptomEditText;
    private ShimmerFrameLayout shimmerFrameLayout;
    private DiseaseViewModel model;

    List<Disease> diseasesList;
    List<Disease> filteredDiseaseList;

    private View view;

    public TipsAndInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_tips_and_info, container, false);

        model = new ViewModelProvider(requireActivity()).get(DiseaseViewModel.class);
        model.getDiseaseList().observe(getViewLifecycleOwner(), new Observer<List<Disease>>() {
            @Override
            public void onChanged(List<Disease> diseases) {
                filteredDiseaseList = diseases;
                inflateRecyclerView();
            }
        });

        try {
            if (filteredDiseaseList == null) {
                GetDiseaseList getDiseaseList = new GetDiseaseList();
                getDiseaseList.execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        // Query the database for symptoms
        // Create an adapter for the Symptoms spinner
        filteredDiseaseList = new ArrayList<>();
        symptomSpinner = view.findViewById(R.id.symptomSpinner);

        try {
            GetSymptomList getSymptomList = new GetSymptomList();
            getSymptomList.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }



    private class GetSymptomList extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                ENTInfoAPI entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
                return entInfoAPI.listSymptom();
            } catch (Exception e){
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            List<String> symptomArray = new ArrayList<>();
            symptomArray.add("Pick a symptom");

            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if(length > 0){
                    for(int i = 0; i < length; i++){
                        JSONObject thisSymptom = resultJson.getJSONObject(i);
                        symptomArray.add(thisSymptom.getString("SYM_DES"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (symptomArray.size() > 0) {
                    symptomSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, symptomArray);
                    symptomSpinner.setAdapter(symptomSpinnerAdapter);
                    symptomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                filteredDiseaseList.clear();
                                filteredDiseaseList.addAll(diseasesList);
                                model.setDisease(filteredDiseaseList);
                                shimmerFrameLayout.stopShimmerAnimation();
                                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                            } else {
                                String selectedSymptom = parent.getItemAtPosition(position).toString();
                                if (diseasesList.size() > 0) {
                                    filteredDiseaseList.clear();
                                    model.setDisease(filteredDiseaseList);
                                    // Query the database
                                    GetDiseaseBySymptom getDiseaseBySymptom = new GetDiseaseBySymptom();
                                    getDiseaseBySymptom.execute(selectedSymptom);
                                } else {
                                    Toast.makeText(getContext(), "Loading data from database, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //Not called
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private class GetDiseaseBySymptom extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
        }

        @Override
        protected String doInBackground(String... strings) {
            ENTInfoAPI entInfoAPI = new ENTInfoAPI(getString(R.string.entinfokey));
            return entInfoAPI.listDiseaseBySymptoms(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if(length > 0){
                    for(int i = 0; i < length; i++){
                        JSONObject thisDisease = resultJson.getJSONObject(i);
                        filteredDiseaseList.add(new Disease(thisDisease.getInt("DISEASE_ID"),
                                thisDisease.getString("DISEASE_NAME"),
                                thisDisease.getString("COMMON_NAME")));
                    }
                }else{
                    Toast.makeText(getContext(), "No result found. Please try again", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.INVISIBLE);
            try {
                if (diseasesList.size() > 0) {
                    model.setDisease(filteredDiseaseList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    private  class GetDiseaseList extends AsyncTask<Void, Void, String>{

        @Override
        protected void onPreExecute() {
            shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                ENTInfoAPI connection = new ENTInfoAPI(getString(R.string.entinfokey));
                return connection.listDisease();
            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.INVISIBLE);
            diseasesList= new ArrayList<>();
            try{
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if(length > 0){
                    for(int i = 0; i < length; i++){
                        JSONObject thisDisease = resultJson.getJSONObject(i);
                        diseasesList.add(new Disease(thisDisease.getInt("DISEASE_ID"),
                                thisDisease.getString("DISEASE_NAME"),
                                thisDisease.getString("COMMON_NAME")));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (diseasesList.size() > 0) {
                    filteredDiseaseList.addAll(diseasesList);
                    model.setDisease(filteredDiseaseList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void inflateRecyclerView(){
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        DiseaseRecyclerViewAdapter adapter;

        recyclerView = view.findViewById(R.id.diseaseRecyclerView);
        layoutManager = new LinearLayoutManager(TipsAndInfoFragment.this.getContext());
        adapter = new DiseaseRecyclerViewAdapter(filteredDiseaseList, TipsAndInfoFragment.this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}