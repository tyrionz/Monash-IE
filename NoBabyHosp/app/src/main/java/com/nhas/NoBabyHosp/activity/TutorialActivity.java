package com.nhas.NoBabyHosp.activity;


import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hololo.tutorial.library.PermissionStep;
import com.hololo.tutorial.library.Step;
import com.nhas.NoBabyHosp.R;

public class TutorialActivity extends com.hololo.tutorial.library.TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder()
                .setTitle("Welcome to NoBabyHosp")
                .setContent("We aim to help first-time parents to keep their kids away from ENT infection")
                .setDrawable(R.drawable.welcome)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .build());

        addFragment(new Step.Builder()
                .setTitle("Don't know what are you dealing at?")
                .setContent("We may have some ideas")
                .setSummary("Simply search possible disease by symptoms and find out possible infection that may be happening.")
                .setDrawable(R.drawable.disease)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .build());

        addFragment(new PermissionStep.Builder()
                .setTitle("Like outing?")
                .setContent("We keep track of Air Quality Index base on your location and show recommendation")
                .setSummary("NoBabyHosp will show the AQI on the home screen and give suggestions base on pollution level!")
                .setDrawable(R.drawable.kids)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                .build());

        addFragment(new Step.Builder()
                .setTitle("Don't know what to do?")
                .setContent("We got your back!")
                .setSummary("NoBabyHosp has included a list of task to do and remind you when the time comes to help keeping your baby away from ENT infection.")
                .setDrawable(R.drawable.todolist)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .build());

        setPrevText("Back");
        setNextText("Next");
        setGivePermissionText("Permit");
        setFinishText("Done");
    }

    @Override
    public void finishTutorial() {
        super.finishTutorial();
        Intent intent = new Intent(TutorialActivity.this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
    }

    @Override
    public void currentFragmentPosition(int position) {

    }
}