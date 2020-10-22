package com.NHAS.Infantime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;

import java.util.List;

public class DiseaseSymptomRecyclerViewAdapter extends RecyclerView.Adapter<DiseaseSymptomRecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView symptomTextView;

        public ViewHolder(View view) {
            super(view);
            this.symptomTextView = view.findViewById(R.id.symptomTextView);
        }
    }

    private List<String> symptomList;

    public DiseaseSymptomRecyclerViewAdapter(List<String> symptomList) {
        this.symptomList = symptomList;
    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }

    @NonNull
    @Override
    public DiseaseSymptomRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View diseaseView = inflater.inflate(R.layout.rv_disease_symptoms_layout, parent, false);
        DiseaseSymptomRecyclerViewAdapter.ViewHolder viewHolder = new DiseaseSymptomRecyclerViewAdapter.ViewHolder(diseaseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseSymptomRecyclerViewAdapter.ViewHolder holder, int position) {
        final String thisSymptom = symptomList.get(position);

        TextView symptom = holder.symptomTextView;
        symptom.setText(thisSymptom);

    }


}

