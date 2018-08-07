package com.example.jc.timemanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;

public class basicActivity extends AppCompatActivity {
    private int newButtons = 0;
    private LinearLayout linearLayout;// = findViewById(R.id.linearLayoutBasic);

    @Override
    protected void onResume() {

        SharedPreferences sharedPref = getSharedPreferences("basic", MODE_PRIVATE);
        for(int i =0; i<sharedPref.getInt("numberOfButtons",0);i++){
            load(sharedPref.getString("button "+i,"button 0"));
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getSharedPreferences("basic", MODE_PRIVATE);

        int z = sharedPref.getInt("numberOfButtons",0);



        linearLayout = findViewById(R.id.linearLayoutBasic);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                create();
            }
        });

        Button button = findViewById(R.id.wakeBasic);
        button.setText(" "+z);
        final Chronometer chronometer = findViewById(R.id.chronoBasic);

        for(int i =0; i<sharedPref.getInt("numberOfButtons",0);i++){
            load(sharedPref.getString("button "+i,"button 0"));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });
    }
    public void create(){
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;

        int px = (int) Math.ceil(100 * logicalDensity);
        int textSize = (int) (getResources().getDimension(R.dimen.text_size_util_btn) / getResources().getDisplayMetrics().density);
        int px_m8 = (int) Math.ceil(8 * logicalDensity);
        ConstraintLayout constraintLayout = new ConstraintLayout(basicActivity.this);



        LinearLayout.LayoutParams constParams = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
        constraintLayout.setLayoutParams(constParams);

        ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(constraintLayout.getLayoutParams());

        Button button = new Button(basicActivity.this);
        button.setBackgroundResource(R.drawable.test_activity_button);

        button.setTextSize(textSize);
        button.setMinimumHeight(px);
        button.setMinimumWidth(metrics.widthPixels);
        int z = View.generateViewId();
        button.setId(z);
        Log.d("basic","id is "+z);
        String s ="New Button " +z;
        button.setText(s);
        buttonParams.setMargins(0,px_m8,0,0);
        buttonParams.setMarginStart(px_m8);
        buttonParams.setMarginEnd(px_m8);
        buttonParams.validate();
        button.setLayoutParams(buttonParams);
        constraintLayout.addView(button);
        linearLayout.addView(constraintLayout);

        newButtons++;
        saveButton(s);

    }
    public void saveButton(String name){
        SharedPreferences sharedPref = getSharedPreferences("basic", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        int z = newButtons + sharedPref.getInt("numberOfButtons",0);
        editor.putInt("numberOfButtons",z);
        String key = "button "+z;
        editor.putString(key, name);
        editor.apply();
    }

    public void load(String name){
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;

        int px = (int) Math.ceil(100 * logicalDensity);
        int textSize = (int) (getResources().getDimension(R.dimen.text_size_util_btn) / getResources().getDisplayMetrics().density);
        int px_m8 = (int) Math.ceil(8 * logicalDensity);
        ConstraintLayout constraintLayout = new ConstraintLayout(basicActivity.this);



        LinearLayout.LayoutParams constParams = new LinearLayout.LayoutParams(linearLayout.getLayoutParams());
        constraintLayout.setLayoutParams(constParams);

        ConstraintLayout.LayoutParams buttonParams = new ConstraintLayout.LayoutParams(constraintLayout.getLayoutParams());

        Button button = new Button(basicActivity.this);
        button.setBackgroundResource(R.drawable.test_activity_button);
        button.setText(name);
        button.setTextSize(textSize);
        button.setMinimumHeight(px);
        button.setMinimumWidth(metrics.widthPixels);
        int z = View.generateViewId();
        button.setId(z);
        Log.d("basic","id is (loading) "+z);
        buttonParams.setMargins(0,px_m8,0,0);
        buttonParams.setMarginStart(px_m8);
        buttonParams.setMarginEnd(px_m8);
        buttonParams.validate();
        button.setLayoutParams(buttonParams);
        constraintLayout.addView(button);
        linearLayout.addView(constraintLayout);
    }


    @Override
    public void finish() {

        super.finish();
    }
}
