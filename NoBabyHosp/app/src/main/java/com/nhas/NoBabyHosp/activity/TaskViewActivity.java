package com.nhas.NoBabyHosp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.model.TaskViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskViewActivity extends AppCompatActivity {

    private TextView taskNameTextView;
    private Button completeBtn;
    private Button assignBtn;
    private Button importToCalendarBtn;
    private CalendarView calendarView;
    private int frequency;
    private long milliseconds;
    private Task thisTask;
    private TextView assignmentTextView;
    private TextView purposeTextView;
    private TaskViewModel model;
    private Date selectedDate;
    private DateFormat dateFormat;
    private EditText frequencyEditText;
    private TextView suggestedFrequencyTextField;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_top, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        Toolbar toolbar = findViewById(R.id.navbar_top);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Setup standard datetime display format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Intent intent = getIntent();
        thisTask = (Task) intent.getSerializableExtra("task");

        frequency = thisTask.getFrequency();

        // Set the name of the task
        taskNameTextView = findViewById(R.id.taskNameTextView);
        taskNameTextView.setText(thisTask.getTaskDescription());

        // Set the purpose of the task
        purposeTextView = findViewById(R.id.taskPurposeTextView);
        purposeTextView.setText(thisTask.getPurpose());

        // Setup the frequency EditText
        frequencyEditText = findViewById(R.id.frequencyEditText);
        frequencyEditText.setText(String.valueOf(thisTask.getFrequency()));

        suggestedFrequencyTextField = findViewById(R.id.suggestedDayTextView);
        suggestedFrequencyTextField.setText(String.valueOf(thisTask.getSuggestedFrequency()));

        //Listen to change of selected date
        calendarView = findViewById(R.id.calendarView);
        selectedDate = new Date(calendarView.getDate());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateString = String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(dayOfMonth);
                setSelectedDate(dateString);
            }
        });

        // Initialise the view model for observing changes and the current task
        model = new ViewModelProvider(this).get(TaskViewModel.class);
        model.initializeVars(getApplication());
        model.getTaskById(thisTask.getTaskID()).observe(this, new Observer<Task>() {
            @Override
            public void onChanged(Task task) {
                thisTask = task;
            }
        });

        // Get the assigned date for the task
        completeBtn = findViewById(R.id.completeBtn);
        importToCalendarBtn = findViewById(R.id.importBtn);
        assignBtn = findViewById(R.id.assignBtn);
        Date assignedDate = thisTask.getAssignedDate();
        assignmentTextView = findViewById(R.id.assignmentTextView);
        if(assignedDate.getTime() != 253402261199000L) {
            String assignedDateString = dateFormat.format(assignedDate);
            assignmentTextView.setText("Task assigned to " + assignedDateString);
            milliseconds = assignedDate.getTime();
            calendarView.setDate(milliseconds);
        }else{
            completeBtn.setVisibility(View.GONE);
            importToCalendarBtn.setVisibility(View.GONE);
        }

        // Set the completion button
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Update the frequency to the newly input one if it's different from the old one
                    if (frequency != Integer.parseInt(frequencyEditText.getText().toString().trim())) {
                        frequency = Integer.parseInt(frequencyEditText.getText().toString().trim());
                        model.updateFrequencyByID(thisTask.getTaskID(), frequency);
                    }

                    milliseconds = thisTask.getAssignedDate().getTime();
                    milliseconds += TimeUnit.DAYS.toMillis(frequency);
                    Date newDate = new Date(milliseconds);
                    model.updateDateByID(thisTask.getTaskID(), newDate);
                    calendarView.setDate(milliseconds);
                    String assignedDateString = dateFormat.format(newDate);
                    assignmentTextView.setText("Task assigned to " + assignedDateString);
                    Toast.makeText(TaskViewActivity.this, "Task re-assigned to " + assignedDateString, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    frequencyEditText.setError("Please only enter integers.");
                }
            }
        });


        // Set the assignment button

        assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    // Update the frequency to the newly input one if it's different from the old one
                    if (frequency != Integer.parseInt(frequencyEditText.getText().toString().trim())){
                        frequency =  Integer.parseInt(frequencyEditText.getText().toString().trim());
                        model.updateFrequencyByID(thisTask.getTaskID(), frequency);
                    }

                    milliseconds = calendarView.getDate();
                    model.updateDateByID(thisTask.getTaskID(), selectedDate);
                    String assignedDateString = dateFormat.format(selectedDate);
                    assignmentTextView.setText("Task assigned to " + assignedDateString);
                    Toast.makeText(TaskViewActivity.this, "Task assigned to " + assignedDateString, Toast.LENGTH_SHORT).show();
                    completeBtn.setVisibility(View.VISIBLE);
                    importToCalendarBtn.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    frequencyEditText.setError("Please only enter integers.");
                }
            }
        });

        // Set the add to calendar button

        importToCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Events.TITLE, thisTask.getTaskDescription());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, thisTask.getPurpose());
                intent.putExtra(CalendarContract.Events.ALL_DAY, true);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, thisTask.getAssignedDate().getTime());

                startActivity(intent);
            }
        });


    }

    public void setSelectedDate(String newDateString){
        try {
            selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}