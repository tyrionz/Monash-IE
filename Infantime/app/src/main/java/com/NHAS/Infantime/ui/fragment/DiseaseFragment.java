package com.NHAS.Infantime.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.DiseaseRecyclerViewAdapter;
import com.NHAS.Infantime.data.entities.Disease;
import com.NHAS.Infantime.viewmodel.DiseaseViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class DiseaseFragment extends Fragment {

    private Spinner symptomSpinner;
    private RecyclerView diseaseRecyclerView;
    private List<Disease> diseaseList;
    private RecyclerView.LayoutManager layoutManager;
    private DiseaseViewModel diseaseViewModel;
    private List<String> symptomList;
    private ShimmerFrameLayout shimmerFrameLayout;


    public DiseaseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);

        symptomSpinner = view.findViewById(R.id.symptomSpinner);
        diseaseRecyclerView = view.findViewById(R.id.diseaseRecyclerView);
        layoutManager = new LinearLayoutManager(DiseaseFragment.this.getContext());
        shimmerFrameLayout = view.findViewById(R.id.shimmer_layout);

        // Get symptoms and populate the spinner
        diseaseViewModel = new ViewModelProvider(requireActivity()).get(DiseaseViewModel.class);
        diseaseViewModel.setKey(getString(R.string.entinfokey));
        diseaseViewModel.initializeVars(requireActivity().getApplication());

        symptomList = new ArrayList<>();
        symptomList.add("Loading...");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, symptomList);
        symptomSpinner.setAdapter(adapter);

        startShimmerAnimation();

        // Get symptoms
        diseaseViewModel.getSymptomList().observe(requireActivity(), symptoms -> {
            symptomList.clear();
            symptomList.addAll(symptoms);
            adapter.notifyDataSetChanged();
        });

        // Get diseases
        diseaseViewModel.getDiseaseList().observe(requireActivity(), diseases -> {
            diseaseList = diseases;
            inflateRecyclerView();
        });

        symptomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    new Thread(){
                        @Override
                        public void run() {
                            diseaseViewModel.fetchAllDisease();

                            try {
                                requireActivity().runOnUiThread(DiseaseFragment.this::stopShimmerAnimation);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } else {
                    new Thread(){
                        @Override
                        public void run() {
                            diseaseViewModel.fetchDiseaseBySymptom(symptomList.get(position));
                        }
                    }.start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not called
            }
        });

        if (symptomList.get(0).equals("Loading...")) {
            symptomSpinner.setEnabled(false);
            new Thread() {
                @Override
                public void run() {
                    diseaseViewModel.getAllSymptoms();
                    try {
                        requireActivity().runOnUiThread(() -> symptomSpinner.setEnabled(true));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        return view;
    }

    private void inflateRecyclerView(){
        DiseaseRecyclerViewAdapter adapter = new DiseaseRecyclerViewAdapter(diseaseList, DiseaseFragment.this.getContext());
        diseaseRecyclerView.setAdapter(adapter);
        diseaseRecyclerView.setLayoutManager(layoutManager);

    }

    private void startShimmerAnimation(){
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
    }

    private void stopShimmerAnimation(){
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmerAnimation();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(diseaseList != null) {
            diseaseList.clear();
        }
    }
}
