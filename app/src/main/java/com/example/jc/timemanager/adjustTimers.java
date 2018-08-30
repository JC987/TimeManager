package com.example.jc.timemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    Spinner spnTimerCategories,spnAdjustTime,spnAffectWakeUp;
    Button btnAdjust;
    private static final String TAG = "adjustTimers";

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_timers);
        //assign number pickers
        numPickerMins = findViewById(R.id.numPickerMins);
        numPickerTensMins = findViewById(R.id.numPickerTensMins);
        numPickerHours = findViewById(R.id.numPickerHours);

        //set number pickers
        numPickerMins.setMinValue(0);
        numPickerMins.setMaxValue(9);
        numPickerMins.computeScroll();
        numPickerTensMins.setMinValue(0);
        numPickerTensMins.setMaxValue(5);
        numPickerTensMins.computeScroll();
        numPickerHours.setMinValue(0);
        numPickerHours.setMaxValue(23);
        numPickerHours.computeScroll();

        //assign spinners
        spnTimerCategories=findViewById(R.id.spnTimerCategories);
        spnAdjustTime=findViewById(R.id.spnAdjustTime);
        spnAffectWakeUp = findViewById(R.id.spnAffectWakeUp);

        //assign button
        btnAdjust = findViewById(R.id.btnAdjust);

        Log.d(TAG, "onCreate: instaniate views");

        //categories to be added to the first spinner
        List<String> categories = new ArrayList<String>();
        categories.add("~~Please Select a Category~~");
      // ,getString(R.string.Relaxing)};
        categories.add(getString(R.string.Leisure));
        categories.add(getString(R.string.Exercise));
        categories.add(getString(R.string.Education));
        categories.add(getString(R.string.Work));
        categories.add(getString(R.string.Other));
        categories.add(getString(R.string.Preparation));
        categories.add(getString(R.string.Traveling));
        categories.add(getString(R.string.Relaxing));
        categories.add(getString(R.string.wake_up));

        //categories to be added to second spinner
        List<String> categories2 = new ArrayList<String>();
        categories2.add("~~Add or Remove Time~~");
        categories2.add("Add Time");
        categories2.add("Remove Time");

        List<String> categories3 = new ArrayList<String>();
        categories3.add("~~Affect WakeUp Timer~~");
        categories3.add("Yes");
        categories3.add("No");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnTimerCategories.setAdapter(dataAdapter);
        spnAdjustTime.setAdapter(dataAdapter2);
        spnAffectWakeUp.setAdapter(dataAdapter3);

        Log.d(TAG, "onCreate: created spinner adapters");

        spnTimerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals(getString(R.string.wake_up)))
                {
                    spnAffectWakeUp.setSelection(1);
                    Log.d(TAG, "onItemSelected: wake up, was selected ");

                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        /**
         * Return the desired value for adjusting time and the category to adjust
         */
        btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                long result;
                int hr = numPickerHours.getValue(), tmin = numPickerTensMins.getValue(), min = numPickerMins.getValue();
                Intent resultIntent = new Intent();
                //If result is negative then we are adding time, if positive we are subtracting time

                //-1 raised to the spinners position gives us the sign of result
                //convert the desired min, ten min, and hour to millisecond then add them up and
                    // multiple it by the sign to get the result
                result=((long)Math.pow (-1,spnAdjustTime.getSelectedItemPosition()) *((60 * 1000 * min) + (60 * 10000 * tmin) + (60 * 60 * 1000 * hr)) );

                //return the result and category
                resultIntent.putExtra("sub",result);//(-1* ((60 * 1000 * min) + (60 * 10000 * tmin) + (60 * 60 * 1000 * hr))) );
                resultIntent.putExtra("sign",spnAdjustTime.getSelectedItemPosition());
                resultIntent.putExtra("cat",spnTimerCategories.getSelectedItem().toString());
                boolean tmpBool = true;
                if(spnAffectWakeUp.getSelectedItem().toString().equals("No"))
                    tmpBool = false;
                resultIntent.putExtra("affect",tmpBool);
               //finish activity
                if(spnTimerCategories.getSelectedItemPosition() == 0 || spnAdjustTime.getSelectedItemPosition() == 0 || spnAffectWakeUp.getSelectedItemPosition() == 0){
                    Toast.makeText(adjustTimers.this,"Please enter all data",Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "onClick: result ok, return to main");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,resultIntent);
        Log.d(TAG, "onBackPressed: result canceled, return to main");
        super.onBackPressed();
    }
}
