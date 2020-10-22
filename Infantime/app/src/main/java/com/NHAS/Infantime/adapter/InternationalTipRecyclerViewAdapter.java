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
import com.NHAS.Infantime.data.entities.InternationalTip;
import com.NHAS.Infantime.data.entities.Tip;

import java.util.List;


public class InternationalTipRecyclerViewAdapter extends RecyclerView.Adapter<InternationalTipRecyclerViewAdapter.ViewHolder> {

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

    private List<InternationalTip> tipList;
    private CheckTask checkTaskListener;
    private String taskType;

    public InternationalTipRecyclerViewAdapter(List<InternationalTip> tasksList, CheckTask checkTaskListener) {
        this.tipList = tasksList;
        this.checkTaskListener = checkTaskListener;
    }

    @NonNull
    @Override
    public InternationalTipRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.rv_cardview_layout_tip, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InternationalTip thisTip = tipList.get(position);
        taskType = thisTip.getWhen();

        TextView taskName = holder.taskNameTextView;
        taskName.setText(thisTip.getTipName());

        TextView purpose = holder.taskPurposeTextView;
        purpose.setText(thisTip.getPurpose());

        TextView description = holder.taskDescriptionTextView;
        description.setText(thisTip.getTipDescription());

        CheckBox checkBox = holder.doneCheckBox;
        checkBox.setOnCheckedChangeListener(null);
        if (taskType.equals("During") || taskType.equals("null")) {
            checkBox.setVisibility(View.GONE);
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
            checkBox.setChecked(thisTip.isDone());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkTaskListener.checkTask(tipList.get(position), taskType, isChecked);
                }
            });
        }

        boolean isExpandable = thisTip.isExpandable();
        holder.detailRelativeLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        holder.expandableImageView.setImageResource(isExpandable ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
    }

    @Override
    public int getItemCount() {
        return tipList.size();
    }

    public interface CheckTask {
        void checkTask(InternationalTip tip, String taskType, Boolean isChecked);
    }


}
