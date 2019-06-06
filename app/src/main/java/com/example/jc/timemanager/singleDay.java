package com.example.jc.timemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class singleDay extends AppCompatActivity {
    PieChart pie;
    private float[] val= {8.09f,10.09f,16.09f,12.59f,18.88f,10.03f,11.5f,21.26f,0f};
    private String[] name = {"Leisure","Exercise","Education","Work","Other","Preparation","Traveling","Relaxing","Unaccounted"};
    private static final String TAG = "singleDay";
    private String description = "";
    private float totalTime = 0f;
    private boolean showUnacc=true, showDesc = true, showName = false;
    private Button button;
    private String[] s;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onPause() {
        // Whenever this activity is paused (i.e. looses focus because another activity is started etc)
        // Override how this activity is animated out of view
        // The new activity is kept still and this activity is pushed out to the left
        //overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }

    /**
     * Get values from intent and create pie chart
     */
    protected  void getValues(){
        //s is a string array that holds each categories values, s[8] holds the value from wake up timer(GREEN)
        float sum = Float.parseFloat(s[0]) + Float.parseFloat(s[1]) + Float.parseFloat(s[2]) + Float.parseFloat(s[3]) + Float.parseFloat(s[4]) + Float.parseFloat(s[5]) + Float.parseFloat(s[6]) + Float.parseFloat(s[7]);
        totalTime = Float.parseFloat(s[s.length-1]);
        
        //if the wakeup timer is larger than the sum of all other timers
        if(Float.parseFloat(s[s.length-1]) > sum) {
            if (showUnacc) { //show unaccounted slice
                val[val.length - 1] = ((Float.parseFloat(s[s.length - 1]) - sum) / Float.parseFloat(s[s.length - 1])) * 100;
                
            }else {
                //don't show unaccounted slice
                //any slice with a val < 1 will not be shown
                val[val.length - 1] = 0.5f;
                totalTime = sum;
            }
        }

        for(int i = 0; i<val.length-1;i++){
            val[i] = (Float.parseFloat(s[i])/totalTime)*100;
            Log.d(TAG, "onCreate: s["+ i + "] is " + s[i]);
        }

        pie = (PieChart) findViewById(R.id.pie);

        //convert totalTime form milli to (HH:MM:SS) and add description
        int sec = Math.round(totalTime / 1000);
        int min = (int) Math.floor(sec / 60);
        int hr = (int) Math.floor(min / 60);
        sec = sec - (min * 60);
        min = min - (hr * 60);

        Description d = new Description();
        NumberFormat numberFormat = new DecimalFormat("00");

        if(showDesc)
            description  = "Total Time " + numberFormat.format(hr)+":"+ numberFormat.format(min) +":"+numberFormat.format(sec)+" for "+getIntent().getStringExtra("day").substring(0,10);
        else
            description = " ";
        d.setText(description);
        d.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_description));
        pie.setDescription(d);

        pie.setTransparentCircleAlpha(128);
        pie.setTransparentCircleRadius(30f);
        pie.setHoleRadius(22f);
        pie.setCenterText(getIntent().getStringExtra("day").substring(0,10));
        pie.setCenterTextSize(10);
        pie.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
        pie.setEntryLabelTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
        addDataSet();
        pie.setEntryLabelColor(Color.BLACK);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stats_tabbed);
        
        s = getIntent().getStringExtra("value").split(",");

        Spinner spinner = findViewById(R.id.tab1Spinner);
        button = findViewById(R.id.tab1Button);
        spinner.setVisibility(View.GONE);


        getValues();

        //creates a dialog box for pie chart settings
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(singleDay.this);
                LayoutInflater inflater = singleDay.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_stats_tab1,null);

                final CheckBox name = dialogView.findViewById(R.id.checkboxShowName);
                final CheckBox desc = dialogView.findViewById(R.id.checkboxShowDesc);
                final CheckBox rotation = dialogView.findViewById(R.id.checkboxEnableRotation);
                final CheckBox unaccounted = dialogView.findViewById(R.id.checkboxShowUnaccounted);

                dialog.setTitle("Set parameters for pie chart");
                dialog.setView(dialogView);

                if(!pie.isDrawEntryLabelsEnabled()) {
                    name.setChecked(false);
                }
                else
                    name.setChecked(true);

                if(!pie.isRotationEnabled())
                    rotation.setChecked(false);

                if(!showUnacc)
                    unaccounted.setChecked(false);

                if(!showDesc)
                    desc.setChecked(false);

                dialog.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!name.isChecked()){
                            showName = false;
                            pie.setDrawEntryLabels(false);
                        }
                        else {
                            showName = true;
                            pie.setDrawEntryLabels(true);
                        }

                        if(!rotation.isChecked())
                            pie.setRotationEnabled(false);
                        else
                            pie.setRotationEnabled(true);

                        if(!unaccounted.isChecked())
                            showUnacc=false;
                        else
                            showUnacc=true;

                        if(!desc.isChecked()) {
                            description="";
                            showDesc=false;
                        }
                        else {
                            showDesc = true;
                        }
                        getValues();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        //Listen foe when a slice is pressed and display a relevant toast
        pie.setOnChartValueSelectedListener( new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h){
                Log.d("chart", "onValueSelected: "+e.toString());
                int pos = e.toString().indexOf("y: ");
                String sub = e.toString().substring(pos+3);
               // String[] s = getIntent().getStringExtra("value").split(",");
                float x = totalTime/1000;

                for(int i = 0; i<val.length;i++){
                    if(val[i]==Float.parseFloat(sub)){
                        pos = i;
                        //  z = val[i];
                        break;
                    }
                }


                int sec =  Math.round(x*(val[pos]/100));
                int min = (int) Math.floor(sec / 60);
                int hr = (int) Math.floor(min / 60);
                sec = sec - (min * 60);
                min = min - (hr * 60);


                NumberFormat numberFormat = new DecimalFormat("00");
                String text  =  "Time spent: "+numberFormat.format(hr)+":"+ numberFormat.format(min) +":"+numberFormat.format(sec);
                Toast.makeText(singleDay.this,text,Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(){

            }
        });
    }

    /**
     * Adds each categories value, corresponding color, and name to the pie chart's data set
     * Creates a legend for pie chart
     */
    private void addDataSet() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for(int i = 0; i< val.length;i++){
            if(val[i]>1) {
                yEntry.add(new PieEntry(val[i], name[i], i));
            }
        }

        for(int i = 1; i<name.length;i++){
            xEntry.add(name[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry,"");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(16);

        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueTextSize(18f);
        ArrayList<Integer> colors = new ArrayList<>();
        if(val[0] > 1)
            colors.add(Color.parseColor("#FFA500"));
        if(val[1] > 1)
            colors.add(Color.parseColor("#FF5050"));
        if(val[2] > 1)
            colors.add(Color.parseColor("#FF50FF"));
        if(val[3] > 1)
            colors.add(Color.parseColor("#1D7CF2"));
        if(val[4] > 1)
            colors.add(Color.parseColor("#C0C0C0"));
        if(val[5] > 1)
            colors.add(Color.parseColor("#00FFFF"));
        if(val[6] > 1)
            colors.add(Color.parseColor("#FFFF00"));
        if(val[7] > 1)
            colors.add(Color.parseColor("#FFC0CB"));
        if(val[8] > 1)
            colors.add(Color.parseColor("#44FF44"));


        Legend legend = pie.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        pie.setDrawEntryLabels(showName);
        pie.animateXY(1000,1000);
        legend.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
     //   legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));

        pieDataSet.setColors(colors);



        pieDataSet.setValueTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_description));

        PieData pieData = new PieData(pieDataSet);
        pie.setData(pieData);
        pie.getLegend().setWordWrapEnabled(true);
        pie.invalidate();
    }
}
