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
import com.NHAS.Infantime.data.entities.DomesticTravelWithTips;
import com.NHAS.Infantime.data.entities.DomesticTrip;
import com.NHAS.Infantime.ui.activity.DomesticTravelChecklistActivity;
import com.NHAS.Infantime.util.Enum.DomesticTravelEnum;

import java.io.Serializable;
import java.util.List;

public class DomesticTravelRecyclerViewAdapter extends RecyclerView.Adapter<DomesticTravelRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView activityNameTextView;

        public ViewHolder(View view) {
            super(view);
            this.activityNameTextView = view.findViewById(R.id.activityNameTextView);
        }
    }

    private List<DomesticTravelWithTips> domesticTripList;
    private Context context;

    public DomesticTravelRecyclerViewAdapter(List<DomesticTravelWithTips> domesticTripList, Context context) {
        this.domesticTripList = domesticTripList;
        this.context = context;
    }

    @NonNull
    @Override
    public DomesticTravelRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View activityView = inflater.inflate(R.layout.rv_cardview_layout_domestic_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DomesticTrip thisDomesticTrip = domesticTripList.get(position).domesticTrip;

        TextView activityName = holder.activityNameTextView;
        activityName.setText(thisDomesticTrip.getTripName());

        // Set on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DomesticTravelChecklistActivity.class);
                intent.putExtra(DomesticTravelEnum.DOMESTIC_TRIP.toString(), (Serializable) thisDomesticTrip);
                intent.putExtra("requestCode", 1);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return domesticTripList.size();
    }


}
