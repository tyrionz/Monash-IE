package com.NHAS.Infantime.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.NHAS.Infantime.R;
import com.NHAS.Infantime.ui.fragment.DiseaseFragment;
import com.NHAS.Infantime.ui.fragment.DomesticTravelFragment;
import com.NHAS.Infantime.ui.fragment.HomeFragment;
import com.NHAS.Infantime.ui.fragment.InternationalTravelFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_home);

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            Intent intent = new Intent(BottomNavHomeActivity.this, TutorialActivity.class);
            startActivityForResult(intent, 1);
        } else {
            replaceFragment(new HomeFragment());
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.navbar_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                switch (id) {
                    //Fragments
                    //Home fragment
                    case R.id.navigation_home:
                        replaceFragment(new HomeFragment());
                        break;

                    //DomesticTrip fragment
                    case R.id.navigation_activity:
                        replaceFragment(new DomesticTravelFragment());
                        break;

                    //Trip fragment
                    case R.id.navigation_trip:
                        replaceFragment(new InternationalTravelFragment());
                        break;

                    //Disease Fragment
                    case R.id.navigation_disease:
                        replaceFragment(new DiseaseFragment());
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
            SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putBoolean("isFirstRun", false);
            spEditor.apply();
            replaceFragment(new HomeFragment());
        }
    }
}

