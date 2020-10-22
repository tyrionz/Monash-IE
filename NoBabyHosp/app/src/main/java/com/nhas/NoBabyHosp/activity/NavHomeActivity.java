package com.nhas.NoBabyHosp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhas.NoBabyHosp.Entities.Task;
import com.nhas.NoBabyHosp.R;
import com.nhas.NoBabyHosp.fragment.AdditionalInfoFragment;
import com.nhas.NoBabyHosp.fragment.HomeFragment;
import com.nhas.NoBabyHosp.fragment.MedicineFragment;
import com.nhas.NoBabyHosp.fragment.TaskFragment;
import com.nhas.NoBabyHosp.fragment.TipsAndInfoFragment;
import com.nhas.NoBabyHosp.model.TaskViewModel;
import com.nhas.NoBabyHosp.networkconnection.ENTInfoAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavHomeActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navbar_top, menu);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_main);

        Toolbar toolbar = findViewById(R.id.navbar_top);
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Intent intent = new Intent(NavHomeActivity.this, TutorialActivity.class);
            startActivityForResult(intent, 1);
        } else {
            replaceFragment(new HomeFragment());
        }

        //Show landing page if it's the first time running
//        if (isFirstRun) {
//            Intent intent = new Intent(NavHomeActivity.this, LandingActivity.class);
//            startActivityForResult(intent, 1);
//        } else {
//            replaceFragment(new HomeFragment());
//        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                switch (id) {
                    //Fragments
                    //Home fragment
                    case R.id.menu_home:
                        replaceFragment(new HomeFragment());
                        break;

                    //Disease fragment
                    case R.id.menu_disease:
                        replaceFragment(new TipsAndInfoFragment());
                        break;

                    //Task fragment
                    case R.id.menu_tasks:
                        replaceFragment(new TaskFragment());
                        break;

                    //Medicine fragment
                    case R.id.menu_stock:
                        replaceFragment(new MedicineFragment());
                        break;

                    // FYI Fragment
                    case R.id.menu_additionalInfo:
                        replaceFragment(new AdditionalInfoFragment());
                        break;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {
            // Query the task table and populate Shared preference of Task
            GetTask getTask = new GetTask();
            getTask.execute();
            replaceFragment(new HomeFragment());

        }
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_profile:
//                // Show user profile screen
//                return true;
//
//            case R.id.action_setting:
//                // Show the setting screen
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private class GetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            ENTInfoAPI connection = new ENTInfoAPI(getString(R.string.entinfokey));
            return connection.listTask();
        }

        @Override
        protected void onPostExecute(String result) {
            //Generate a list of task object and store in a list
            ArrayList<Task> taskList = new ArrayList<>();
            try {
                JSONArray resultJson = new JSONArray(result);
                int length = resultJson.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject thisTask = resultJson.getJSONObject(i);
                        taskList.add(new Task(thisTask.getInt("TASK_ID"),
                                thisTask.getString("TASK_DES"),
                                thisTask.getInt("FREQUENCY"),
                                thisTask.getString("PURPOSE")));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (taskList.size() > 0) {
                TaskViewModel model = new ViewModelProvider(NavHomeActivity.this).get(TaskViewModel.class);
                for (Task thisTask : taskList) {
                    model.insert(thisTask);
                }
            }

            Toast.makeText(NavHomeActivity.this, taskList.size() + " Tasks generated. Please view and assign the tasks.", Toast.LENGTH_SHORT).show();

        }
    }
}