package com.nhas.NoBabyHosp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.activity.AddMedicineActivity;
import com.nhas.NoBabyHosp.activity.MedicineViewActivity;
import com.nhas.NoBabyHosp.adapter.MedicineRecyclerViewAdapter;
import com.nhas.NoBabyHosp.model.MedicineViewModel;

import java.util.List;


public class MedicineFragment extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MedicineRecyclerViewAdapter adapter;
    private Button addMedicineBtn;


    public MedicineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_medicine, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MedicineFragment.this.getContext());

        MedicineViewModel model = new ViewModelProvider(requireActivity()).get(MedicineViewModel.class);
        model.initializeVars(getActivity().getApplication());
        model.getAllMedicines().observe(getViewLifecycleOwner(), new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                adapter = new MedicineRecyclerViewAdapter(medicines, MedicineFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        addMedicineBtn = view.findViewById(R.id.addMedicineBtn);
        addMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMedicineActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}