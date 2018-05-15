package com.example.jc.timemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class adjustTimers extends AppCompatActivity {
    NumberPicker numPickerMins, numPickerTensMins,numPickerHours;
    Spinner spnTimerCategories,spnAdjustTime;int i;
    Button btnAdjust;
    Boolean bl ;
    Long l,time= SystemClock.elapsedRealtime();

    String lastBtn;
    long cm_o,cm_r,cm_p,lp_o,lp_r,lp_p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_timers);
        numPickerMins = findViewById(R.id.numPickerMins);
        numPickerTensMins = findViewById(R.id.numPickerTensMins);
        numPickerHours = findViewById(R.id.numPickerHours);
        numPickerMins.setMinValue(0);
        numPickerMins.setMaxValue(9);
        numPickerMins.computeScroll();
        numPickerTensMins.setMinValue(0);
        numPickerTensMins.setMaxValue(5);
        numPickerTensMins.computeScroll();
        numPickerHours.setMinValue(0);
        numPickerHours.setMaxValue(23);
        numPickerHours.computeScroll();

        spnTimerCategories=findViewById(R.id.spnTimerCategories);
        spnAdjustTime=findViewById(R.id.spnAdjustTime);
        btnAdjust = findViewById(R.id.btnAdjust);
        List<String> categories = new ArrayList<String>();
        categories.add("~~Please Select a Category~~");
        categories.add("Leisure");
        categories.add("Exercise");
        categories.add("Education");
        categories.add("Work");
        categories.add("Other");
        categories.add("Preparation");
        categories.add("Traveling");
        categories.add("Nap");

        List<String> categories2 = new ArrayList<String>();
        categories2.add("~~Add or Remove Time~~");
        categories2.add("Add Time");
        categories2.add("Remove Time");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnTimerCategories.setAdapter(dataAdapter);
        spnAdjustTime.setAdapter(dataAdapter2);
        Intent intent = getIntent();
        if (null != intent) {
            l = intent.getLongExtra("cmBase", 0);
            lastBtn=intent.getStringExtra("lastBtn");

            cm_o= intent.getLongExtra("cm_o",0);
            cm_r = intent.getLongExtra("cm_r",0);
            cm_p = intent.getLongExtra("cm_p",0);
            lp_o = intent.getLongExtra("lp_o",0);
            lp_r = intent.getLongExtra("lp_r",0);
            lp_p = intent.getLongExtra("lp_p",0);
        }
        i=spnAdjustTime.getSelectedItemPosition();
        bl = i==1;




        btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                long result;
               int result2 ,hr = numPickerHours.getValue(), tmin = numPickerTensMins.getValue(), min = numPickerMins.getValue();
                Intent resultIntent = new Intent();
                result=((long)Math.pow (-1,spnAdjustTime.getSelectedItemPosition()) *((60 * 1000 * min) + (60 * 10000 * tmin) + (60 * 60 * 1000 * hr)) );
                resultIntent.putExtra("sub",result);//(-1* ((60 * 1000 * min) + (60 * 10000 * tmin) + (60 * 60 * 1000 * hr))) );
                resultIntent.putExtra("sign",spnAdjustTime.getSelectedItemPosition());
                resultIntent.putExtra("cat",spnTimerCategories.getSelectedItem().toString());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

            }
        });
    }

}
