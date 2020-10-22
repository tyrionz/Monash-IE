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
import com.NHAS.Infantime.adapter.InternationalTravelRecyclerViewAdapter;
import com.NHAS.Infantime.ui.activity.AddInternationalTripActivity;
import com.NHAS.Infantime.viewmodel.InternationalTravelViewModel;


public class InternationalTravelFragment extends Fragment {

    private View view;

    private TextView noTripTextView;
    private RecyclerView recyclerView;
    private InternationalTravelRecyclerViewAdapter adapter;

    public InternationalTravelFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_international_travel, container, false);

        noTripTextView = view.findViewById(R.id.noTripTextView);

        InternationalTravelViewModel model = new ViewModelProvider(requireActivity()).get(InternationalTravelViewModel.class);
        model.initializeVars(requireActivity().getApplication());
        model.getAllTrips().observe(getViewLifecycleOwner(), trips -> {
            if (!trips.isEmpty()) {
                recyclerView = view.findViewById(R.id.tripRecyclerView);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InternationalTravelFragment.this.getContext());
                adapter = new InternationalTravelRecyclerViewAdapter(trips, InternationalTravelFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }else{
                noTripTextView.setVisibility(View.VISIBLE);
            }
        });

        Button addTripButton = view.findViewById(R.id.addTripButton);
        addTripButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddInternationalTripActivity.class);
            startActivity(intent);
        });

        return view;
    }

}
