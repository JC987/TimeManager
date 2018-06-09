package com.example.jc.timemanager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by JC on 6/4/2018.
 */

import android.support.v4.app.Fragment;
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

import java.util.Map;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class statsTab1 extends Fragment {
    PieChart pie;
    final String TAG = "map";
    private float[] val=  {0f,0f,0f,0f,0f,0f,0f,0f};
    private String[] name = {"Leisure","Exercise","Education","Work","Other","Preparation","Traveling","Nap"};
    private String description = "";



    public void getValues() {
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
        //if the user has saved before
        if (!keys.isEmpty()) {
            String[] arr = new String[8];//array is 8 instead of 7 because I am saving "Day"
            int ct = 0;
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.i("map", entry.getKey());
                //fill array with users savedPreferences
                arr[ct] = entry.getKey();
                ct++;
                Log.d(TAG, "getValues: inside map for loop");
            }

            float orangeTotal = 0, redTotal = 0, purpleTotal = 0, blueTotal = 0, greyTotal = 0, cyanTotal = 0, yellowTotal = 0, pinkTotal = 0, greenTotal = 0;
            for (int i = 0; i < arr.length; i++) {
                String tmp = arr[i];

                if (i < ct - 1) {
                    String a = keys.get(tmp).toString();
                    String[] split = a.split(",");

                     //add up each position form each savedPreferences
                    orangeTotal += Float.parseFloat(split[0]);
                    redTotal += Float.parseFloat(split[1]);
                    purpleTotal += Float.parseFloat(split[2]);
                    blueTotal += Float.parseFloat(split[3]);
                    greyTotal += Float.parseFloat(split[4]);
                    cyanTotal += Float.parseFloat(split[5]);
                    yellowTotal += Float.parseFloat(split[6]);
                    pinkTotal += Float.parseFloat(split[7]);
                    greenTotal += Float.parseFloat(split[8]);

                }
            }
            //assign val array according to the corresponding position
            val[0] = (orangeTotal/greenTotal)*100;
            val[1] = (redTotal/greenTotal)*100;
            val[2] = (purpleTotal/greenTotal)*100;
            val[3] = (blueTotal/greenTotal)*100;
            val[4] = (greyTotal/greenTotal)*100;
            val[5] = (cyanTotal/greenTotal)*100;
            val[6] = (yellowTotal/greenTotal)*100;
            val[7] = (pinkTotal/greenTotal)*100;

            // display the total time in a normal format
            int sec = Math.round(greenTotal / 1000);
            int min = (int) Math.floor(sec / 60);
            int hr = (int) Math.floor(min / 60);
            sec = sec - (min * 60);
            min = min - (hr * 60);

            //hh:mm:ss
            Log.d("map", "getValues: " + pref.getInt("Day",0));
           description = "Total Time (hh:mm:ss) " + hr+":"+ min +":"+sec+" over the past "+(pref.getInt("Day",0))+ " days!";
        }
    }


    //draw pie chart and populate it.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats_tabbed, container, false);
        getValues();
        Log.d(TAG, "onCreateView: ");
        pie = (PieChart) rootView.findViewById(R.id.pie);
        Description d = new Description();
        d.setText(description);
        d.setTextSize(12f);
        pie.setDescription(d);
        pie.setTransparentCircleAlpha(128);
        pie.setTransparentCircleRadius(30f);
     //   pie.setRotationEnabled(true);
        pie.setHoleRadius(15f);
        pie.setCenterText("Total Time!");
        pie.setCenterTextSize(10);

        addDataSet();
        pie.setEntryLabelColor(Color.BLACK);

        pie.setOnChartValueSelectedListener( new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h){
                Log.d("chart", "onValueSelected: "+e.toString());
                int pos = e.toString().indexOf("y: ");
                String sub = e.toString().substring(pos+3);
                Toast.makeText(getActivity(),sub,Toast.LENGTH_SHORT).show();
                for(int i = 0; i<val.length;i++){
                    if(val[i]==Float.parseFloat(sub)){
                        pos = i;
                        break;
                    }
                }
                String cat = name[pos];
                Toast.makeText(getActivity(),cat,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(){

            }
        });




        return rootView;
    }

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


        Legend legend = pie.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        // pie.setDrawEntryLabels(false);
        pie.animateXY(1000,1000);
        legend.setTextSize(10f);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pie.setData(pieData);
        pie.invalidate();
    }
}