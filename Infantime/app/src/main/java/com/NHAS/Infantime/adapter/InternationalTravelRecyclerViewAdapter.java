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
import com.NHAS.Infantime.data.entities.InternationalTravelWithMedicinesTips;
import com.NHAS.Infantime.data.entities.Trip;
import com.NHAS.Infantime.ui.activity.InternationalTravelChecklistActivity;
import com.NHAS.Infantime.util.Enum.InternationalTravelEnum;

import java.io.Serializable;
import java.util.List;

public class InternationalTravelRecyclerViewAdapter extends RecyclerView.Adapter<InternationalTravelRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tripNameTextView;

        public ViewHolder(View view) {
            super(view);
            this.tripNameTextView = view.findViewById(R.id.activityNameTextView);
        }
    }

    private List<InternationalTravelWithMedicinesTips> tripList;
    private Context context;

    public InternationalTravelRecyclerViewAdapter(List<InternationalTravelWithMedicinesTips> tripList, Context context) {
        this.tripList = tripList;
        this.context = context;
    }

    @NonNull
    @Override
    public InternationalTravelRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View activityView = inflater.inflate(R.layout.rv_cardview_layout_domestic_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trip thisTrip = tripList.get(position).trip;

        TextView activityName = holder.tripNameTextView;
        activityName.setText(thisTrip.getTripName());

        // Set on click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InternationalTravelChecklistActivity.class);
                intent.putExtra(InternationalTravelEnum.INTERNATIONAL_TRIP.toString(), (Serializable) thisTrip);
                intent.putExtra("requestCode", 1);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }


}
