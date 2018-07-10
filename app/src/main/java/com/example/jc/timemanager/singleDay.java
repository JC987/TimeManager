package com.example.jc.timemanager;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private float[] val= {8.09f,10.09f,16.09f,12.59f,18.88f,10.03f,11.5f,21.26f};
    private String[] name = {"Leisure","Exercise","Education","Work","Other","Preparation","Traveling","Nap"};
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

        for(int i = 0; i<val.length;i++){
            val[i] = (Float.parseFloat(s[i])/Float.parseFloat(s[s.length-1]))*100;
        }
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
        d.setTextSize(16f);
        pie.setDescription(d);

        pie.setTransparentCircleAlpha(128);
        pie.setTransparentCircleRadius(30f);
        pie.setRotationEnabled(true);
        pie.setHoleRadius(15f);
        pie.setCenterText(getIntent().getStringExtra("day").substring(0,10));
        pie.setCenterTextSize(10);

        addDataSet();
        pie.setEntryLabelColor(Color.BLACK);

        pie.setOnChartValueSelectedListener( new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h){
                Log.d("chart", "onValueSelected: "+e.toString());
                int pos = e.toString().indexOf("y: ");
                String sub = e.toString().substring(pos+3);
                Toast.makeText(singleDay.this,sub,Toast.LENGTH_SHORT).show();
                for(int i = 0; i<val.length;i++){
                    if(val[i]==Float.parseFloat(sub)){
                        pos = i;
                        break;
                    }
                }
                String cat = name[pos];
                Toast.makeText(singleDay.this,cat,Toast.LENGTH_SHORT).show();
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


        Legend legend = pie.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
       // pie.setDrawEntryLabels(false);
        pie.animateXY(1000,1000);
        legend.setTextSize(10f);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        pieDataSet.setColors(colors);

        //pieDataSet.setLabel("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        PieData pieData = new PieData(pieDataSet);
        /*DecimalFormat df = new DecimalFormat("###,###,##1");
        pieData.setValueFormatter(new PercentFormatter(df));

        */pie.setData(pieData);
        pie.invalidate();
    }
}
