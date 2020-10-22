package com.NHAS.Infantime.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.Medicine;
import com.NHAS.Infantime.ui.activity.MedicineViewActivity;

import java.util.List;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicineNameTextView;
        public TextView currentStockTextView;
        public CheckBox checkBox;

        public ViewHolder(View view){
            super(view);
            medicineNameTextView = view.findViewById(R.id.medicineNameTextView);
            currentStockTextView = view.findViewById(R.id.currentStockTextView);
            checkBox = view.findViewById(R.id.doneCheckbox);
        }

    }

    private List<Medicine> medicineList;
    private Context context;
    private CheckMedicine checkMedicineListener;

    public MedicineRecyclerViewAdapter(List<Medicine> medicineList, Context context, CheckMedicine checkMedicineListener){
        this.medicineList = medicineList;
        this.context = context;
        this.checkMedicineListener = checkMedicineListener;
    }

    @NonNull
    @Override
    public MedicineRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View medicineView = inflater.inflate(R.layout.rv_cardview_layout_medicine, parent, false);
        ViewHolder viewHolder = new ViewHolder(medicineView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Medicine thisMedicine = medicineList.get(position);

        TextView medicineName = holder.medicineNameTextView;
        medicineName.setText(thisMedicine.getMedDescription());

        TextView currentStock = holder.currentStockTextView;
        currentStock.setText(Integer.toString(thisMedicine.getStockNeeded()));

        CheckBox checkBox = holder.checkBox;
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(thisMedicine.isGot());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkMedicineListener.checkMedicine(medicineList.get(position), isChecked);
            }
        });

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


    public interface CheckMedicine {
        void checkMedicine(Medicine medicine, Boolean isChecked);
    }
}
