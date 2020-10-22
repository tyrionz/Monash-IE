package com.NHAS.Infantime.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.adapter.DomesticTravelRecyclerViewAdapter;
import com.NHAS.Infantime.ui.activity.AddDomesticTripActivity;
import com.NHAS.Infantime.viewmodel.DomesticTravelViewModel;

public class DomesticTravelFragment extends Fragment {

    private View view;

    private TextView noActivityTextView;
    private RecyclerView recyclerView;
    private DomesticTravelRecyclerViewAdapter adapter;

    public DomesticTravelFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_domestic_travel, container, false);

        noActivityTextView = view.findViewById(R.id.noActivityTextView);

        DomesticTravelViewModel model = new ViewModelProvider(requireActivity()).get(DomesticTravelViewModel.class);
        model.initializeVars(requireActivity().getApplication());
        model.getAllTrips().observe(getViewLifecycleOwner(), trips -> {
            if (!trips.isEmpty()) {
                recyclerView = view.findViewById(R.id.activityRecyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DomesticTravelFragment.this.getContext());
                adapter = new DomesticTravelRecyclerViewAdapter(trips, DomesticTravelFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                noActivityTextView.setVisibility(View.VISIBLE);
            }
        });

        Button addActivityButton = view.findViewById(R.id.addActivityButton);
        addActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddDomesticTripActivity.class);
            startActivity(intent);
        });




        return view;
    }
}
