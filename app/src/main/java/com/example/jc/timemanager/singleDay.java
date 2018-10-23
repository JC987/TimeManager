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
    private boolean showUnacc=true;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //  overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.fragment_stats_tabbed);
        // Intent intent = new Intent();
        String[] s = getIntent().getStringExtra("value").split(",");
        float total = 0f;
        Spinner spinner = findViewById(R.id.tab1Spinner);
        Button button = findViewById(R.id.tab1Button);
        spinner.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        for(int i = 0; i<val.length-1;i++){
            val[i] = (Float.parseFloat(s[i])/Float.parseFloat(s[s.length-1]))*100;
            total += Float.parseFloat(s[i]);
            Log.d(TAG, "onCreate: s[i] is " + s[i]);
            Log.d(TAG, "onCreate: total is " + total);
        }
        Log.d(TAG, "onCreate: last one is " + s[s.length-1]);
        if(Float.parseFloat(s[s.length-1]) > total)
        val[val.length-1] = ((Float.parseFloat(s[s.length-1]) - total) / Float.parseFloat(s[s.length-1])) * 100;
        //float greenTotal = Float.parseFloat(s[s.length-1]);




        int sec = Math.round(Float.parseFloat(s[s.length-1]) / 1000);
        int min = (int) Math.floor(sec / 60);
        int hr = (int) Math.floor(min / 60);
        sec = sec - (min * 60);
        min = min - (hr * 60);






        pie = (PieChart) findViewById(R.id.pie);
        Description d = new Description();
        NumberFormat numberFormat = new DecimalFormat("00");
        
        String desc  = "Total Time (hh:mm:ss) " + numberFormat.format(hr)+":"+ numberFormat.format(min) +":"+numberFormat.format(sec)+" for "+getIntent().getStringExtra("day").substring(0,10);
        d.setText(desc);
        //d0.setTextSize(16f);
        d.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_description));
        pie.setDescription(d);

        pie.setTransparentCircleAlpha(128);
        pie.setTransparentCircleRadius(30f);
        pie.setRotationEnabled(true);
        pie.setHoleRadius(22f);
        pie.setCenterText(getIntent().getStringExtra("day").substring(0,10));
        pie.setCenterTextSize(10);
        pie.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
        pie.setEntryLabelTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
        addDataSet();
        pie.setEntryLabelColor(Color.BLACK);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(singleDay.this);
                 LayoutInflater inflater = singleDay.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_stats_tab1,null);

                final CheckBox name = dialogView.findViewById(R.id.checkboxShowName);
                CheckBox percent = dialogView.findViewById(R.id.checkboxShowPercent);
                final CheckBox rotation = dialogView.findViewById(R.id.checkboxEnableRotation);
                final CheckBox unaccounted = dialogView.findViewById(R.id.checkboxShowUnaccounted);

                dialog.setTitle("Set parameters for pie chart");
                dialog.setView(dialogView);

                if(!pie.isDrawEntryLabelsEnabled())
                    name.setChecked(false);
                if(!pie.isRotationEnabled())
                    rotation.setChecked(false);
                if(!showUnacc)
                    unaccounted.setChecked(false);

                dialog.setPositiveButton("Show", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!name.isChecked()){
                            pie.setDrawEntryLabels(false);
                        }
                        else
                            pie.setDrawEntryLabels(true);

                        if(!rotation.isChecked())
                            pie.setRotationEnabled(false);
                        else pie.setRotationEnabled(true);

                        if(!unaccounted.isChecked())
                            showUnacc=false;
                        else
                            showUnacc=true;
                       // orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
                        //getValues(zz);
                        //addDataSet();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });



        pie.setOnChartValueSelectedListener( new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h){
                Log.d("chart", "onValueSelected: "+e.toString());
                int pos = e.toString().indexOf("y: ");
                String sub = e.toString().substring(pos+3);
                String[] s = getIntent().getStringExtra("value").split(",");
                float x = Float.parseFloat(s[s.length-1])/1000;

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

    private void addDataSet() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for(int i = 0; i< val.length;i++){
            if(val[i]>1) {
                yEntry.add(new PieEntry(val[i], name[i], i));
            }
        }
        /*for(int i = 0; i<val.length;i++){
            if(yEntry.get(i).getValue()<10f){
                yEntry.get(i).setLabel("");
            }
        }*/
        //   PieEntry p = new PieEntry(val[0],name[0],0);
        for(int i = 1; i<name.length;i++){
            xEntry.add(name[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry,"");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(16);
        //pieDataSet.setColor(Color.RED);

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
       // pie.setDrawEntryLabels(false);
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
        //pieDataSet.setLabel("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        PieData pieData = new PieData(pieDataSet);
        /*DecimalFormat df = new DecimalFormat("###,###,##1");
        pieData.setValueFormatter(new PercentFormatter(df));

        */pie.setData(pieData);
        pie.getLegend().setWordWrapEnabled(true);
        pie.invalidate();
    }
}
