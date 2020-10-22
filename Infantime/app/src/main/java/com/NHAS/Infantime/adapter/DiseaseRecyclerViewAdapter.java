package com.NHAS.Infantime.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.Disease;
import com.NHAS.Infantime.ui.activity.DiseaseInfoActivity;

import java.util.List;

/*
 * Copyright (C) 2020 NHAS
 * ...
 */


/**
 *  DiseaseRecyclerViewAdapter is responsible for preparing the layout for the Disease RecyclerView.
 *  The adapter is inflating the rv_cardview_layout_disease layout.
 */
public class DiseaseRecyclerViewAdapter extends RecyclerView.Adapter<DiseaseRecyclerViewAdapter.ViewHolder> {

    
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView diseaseCommonNameTextView;
        public TextView diseaseScienceNameTextView;

        public ViewHolder(View view) {
            super(view);
            this.diseaseCommonNameTextView = view.findViewById(R.id.commonNameTextView);
            this.diseaseScienceNameTextView = view.findViewById(R.id.scienceTextView);
        }
    }

    private List<Disease> diseaseList;
    private Context context;

    public DiseaseRecyclerViewAdapter(List<Disease> diseaseList, Context context) {
        this.diseaseList = diseaseList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    @NonNull
    @Override
    public DiseaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View diseaseView = inflater.inflate(R.layout.rv_cardview_layout_disease, parent, false);
        ViewHolder viewHolder = new ViewHolder(diseaseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Disease thisDisease = diseaseList.get(position);

        TextView diseaseCommonName = holder.diseaseCommonNameTextView;
        diseaseCommonName.setText(thisDisease.getDiseaseCommonName());

        TextView diseaseName = holder.diseaseScienceNameTextView;
        diseaseName.setText(thisDisease.getDiseaseScienceName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiseaseInfoActivity.class);
                intent.putExtra("disease", thisDisease);
                context.startActivity(intent);
            }
        });

    }
}