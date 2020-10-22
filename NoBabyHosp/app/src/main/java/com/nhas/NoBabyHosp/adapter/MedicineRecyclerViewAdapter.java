package com.nhas.NoBabyHosp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhas.NoBabyHosp.Entities.Medicine;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.activity.MedicineViewActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicineNameTextView;
        public TextView expiryDateTextView;
        public TextView currentStockTextView;

        public ViewHolder(View view){
            super(view);
            medicineNameTextView = view.findViewById(R.id.medicineNameTextView);
            expiryDateTextView = view.findViewById(R.id.expiryDateTextView);
            currentStockTextView = view.findViewById(R.id.currentStockTextView);
        }

    }

    private List<Medicine> medicineList;
    private Context context;
    // Setup standard datetime display format
    private DateFormat dateFormat;

    public MedicineRecyclerViewAdapter(List<Medicine> medicineList, Context context){
        this.medicineList = medicineList;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("MM-YYYY",Locale.getDefault());
    }

    @NonNull
    @Override
    public MedicineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View medicineView = inflater.inflate(R.layout.medicine_cardview_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(medicineView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Medicine thisMedicine = medicineList.get(position);

        TextView medicineName = holder.medicineNameTextView;
        medicineName.setText(thisMedicine.getMedDescription());

        TextView currentStock = holder.currentStockTextView;
        currentStock.setText(Integer.toString(thisMedicine.getCurrentStock()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicineViewActivity.class);
                intent.putExtra("medicine", thisMedicine);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }


}
