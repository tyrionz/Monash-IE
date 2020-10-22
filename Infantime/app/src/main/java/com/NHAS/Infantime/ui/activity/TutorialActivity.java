package com.NHAS.Infantime.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.NHAS.Infantime.R;
import com.hololo.tutorial.library.PermissionStep;
import com.hololo.tutorial.library.Step;

public class TutorialActivity extends com.hololo.tutorial.library.TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addFragment(new Step.Builder()
                .setTitle("Welcome to Infantime")
                .setContent("Has your kid shown ENT related symptoms like ear pain, scratchy nose or throat infection?")
                .setDrawable(R.drawable.ic_launcher_foreground)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .build());

        addFragment(new Step.Builder()
                .setTitle("Don't know what are you dealing at?")
//                .setContent("We may have some ideas")
                .setContent("Simply search possible disease by symptoms and find out possible infection that may be happening.")
                .setDrawable(R.drawable.ic_launcher_foreground)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .build());

        addFragment(new PermissionStep.Builder()
                .setTitle("Going out with your kid to do some activity around the city?")
                .setContent("We keep track of Air Quality Index based on your location and show recommendations")
//                .setSummary("Infantime will show the AQI on the home screen and give suggestions base on pollution level!")
                .setDrawable(R.drawable.ic_launcher_foreground)
                .setBackgroundColor(Color.parseColor("#0069c0"))
                .setPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION})
                .build());

        addFragment(new Step.Builder()
                .setTitle("Don't know what to do to prevent ENT infections?")
                .setContent("Infantime has included a list of tasks to do to help keeping your baby away from ENT infections.")
//                .setSummary("We got your back!")
                .setDrawable(R.drawable.ic_launcher_foreground)
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
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void currentFragment(@Nullable View view, Integer position) {
        super.currentFragment(view, position);
        TextView title = view.findViewById(R.id.title);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(Typeface.DEFAULT_BOLD);

        TextView content = view.findViewById(R.id.content);
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    }
}