package com.nhas.NoBabyHosp.fragment;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.adapter.TaskRecyclerViewAdapter;
import com.nhas.NoBabyHosp.model.TaskViewModel;

import java.util.List;


public class TaskFragment extends Fragment {


    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TaskRecyclerViewAdapter adapter;


    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_task, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(TaskFragment.this.getContext());

        TaskViewModel model = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        model.initializeVars(getActivity().getApplication());
        model.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter = new TaskRecyclerViewAdapter(tasks, TaskFragment.this.getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });

        return view;
    }
}