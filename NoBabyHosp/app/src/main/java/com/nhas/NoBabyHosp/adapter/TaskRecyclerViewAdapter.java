package com.nhas.NoBabyHosp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.activity.TaskViewActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView taskNameTextView;
        public TextView assignmentDateTextView;
        public TextView checkAssignedTextView;
        public TextView dayNumberTextView;

        public ViewHolder(View view){
            super(view);
            taskNameTextView = view.findViewById(R.id.taskNameTextView);
            checkAssignedTextView = view.findViewById(R.id.checkAssignedTextView);
            assignmentDateTextView = view.findViewById(R.id.assignmentDateTextView);
            dayNumberTextView = view.findViewById(R.id.dayNumberTextView);
        }

    }

    private List<Task> taskList;
    private Context context;
    // Setup standard datetime display format
    private DateFormat dateFormat;

    public TaskRecyclerViewAdapter(List<Task> tasksList, Context context){
        this.taskList = tasksList;
        this.context = context;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    }

    @NonNull
    @Override
    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.task_cardview_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task thisTask = taskList.get(position);

        TextView taskName = holder.taskNameTextView;
        taskName.setText(thisTask.getTaskDescription());

        TextView dayNumber = holder.dayNumberTextView;
        dayNumber.setText(Integer.toString(thisTask.getFrequency()));

        if (thisTask.getAssignedDate().getTime() != 253402261199000L){
            TextView checkAssignedTextView = holder.checkAssignedTextView;
            checkAssignedTextView.setText("Task due on: ");

            TextView assignmentDate = holder.assignmentDateTextView;
            assignmentDate.setVisibility(View.VISIBLE);
            assignmentDate.setText(dateFormat.format(thisTask.getAssignedDate()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TaskViewActivity.class);
                intent.putExtra("task", thisTask);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
