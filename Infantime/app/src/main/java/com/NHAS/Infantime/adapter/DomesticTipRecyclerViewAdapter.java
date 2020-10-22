package com.NHAS.Infantime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.data.entities.DomesticTip;
import com.NHAS.Infantime.data.entities.Tip;

import java.util.List;

public class DomesticTipRecyclerViewAdapter extends RecyclerView.Adapter<DomesticTipRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskNameTextView;
        public TextView taskPurposeTextView;
        public TextView taskDescriptionTextView;
        public LinearLayout linearLayout;
        public RelativeLayout detailRelativeLayout;
        public CheckBox doneCheckBox;
        public ImageView expandableImageView;

        public ViewHolder(View view) {
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            taskPurposeTextView = view.findViewById(R.id.taskPurposeTextView);
            taskDescriptionTextView = view.findViewById(R.id.taskDescriptionTextView);
            doneCheckBox = view.findViewById(R.id.doneCheckbox);

            linearLayout = view.findViewById(R.id.cardViewLinearLayout);
            detailRelativeLayout = view.findViewById(R.id.taskDetailrelativeLayout);

            expandableImageView = view.findViewById(R.id.expandableImageView);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tip selectedTip = tipList.get(getAdapterPosition());
                    selectedTip.setExpandable(!selectedTip.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

    }

    private List<DomesticTip> tipList;
    private DomesticTipRecyclerViewAdapter.CheckTask checkTaskListener;
    private String taskType;

    public DomesticTipRecyclerViewAdapter(List<DomesticTip> tasksList, DomesticTipRecyclerViewAdapter.CheckTask checkTaskListener) {
        this.tipList = tasksList;
        this.checkTaskListener = checkTaskListener;
    }

    @NonNull
    @Override
    public DomesticTipRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.rv_cardview_layout_tip, parent, false);
        DomesticTipRecyclerViewAdapter.ViewHolder viewHolder = new DomesticTipRecyclerViewAdapter.ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DomesticTipRecyclerViewAdapter.ViewHolder holder, int position) {
        DomesticTip thisTip = tipList.get(position);
        taskType = thisTip.getWhen();

        boolean isExpandable = thisTip.isExpandable();
        holder.detailRelativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        holder.expandableImageView.setImageResource(isExpandable ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);

        TextView taskName = holder.taskNameTextView;
        taskName.setText(thisTip.getTipName());

        TextView purpose = holder.taskPurposeTextView;
        purpose.setText(thisTip.getPurpose());

        TextView description = holder.taskDescriptionTextView;
        description.setText(thisTip.getTipDescription());

        CheckBox checkBox = holder.doneCheckBox;
        checkBox.setOnCheckedChangeListener(null);
        if (taskType.equals("During") || taskType.equals("null")) {
            checkBox.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) taskName.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            params = (RelativeLayout.LayoutParams) purpose.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) taskName.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
            params = (RelativeLayout.LayoutParams) purpose.getLayoutParams();
            params.removeRule(RelativeLayout.ALIGN_PARENT_START);

            checkBox.setVisibility(View.VISIBLE);
            boolean checked = thisTip.isDone();
            checkBox.setChecked(checked);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkTaskListener.checkTask(tipList.get(position), taskType, isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public interface CheckTask{
        void checkTask(DomesticTip tip, String taskType, Boolean isChecked);
    }
}
