package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class statsTab1 extends Fragment {
    PieChart pie;
    final String TAG = "statsTab1";
    private float[] val =  {0f,0f,0f,0f,0f,0f,0f,0f,0f};
    private String[] name = {"Leisure","Exercise","Education","Work","Other","Preparation","Traveling","Relaxing","Unaccounted"}; //{getString(R.string.Leisure),getString(R.string.Exercise),getString(R.string.Education),getString(R.string.Work),getString(R.string.Other),getString(R.string.Preparation),getString(R.string.Traveling),getString(R.string.Relaxing)};
    //  {R.string.Leisure,R.string.Exercise,R.string.Education,R.string.Work,R.string.Other,R.string.Preparation,R.string.Traveling,R.string.}
    private String description = "";
    private Button button;
    private float orangeTotal = 0, redTotal = 0, purpleTotal = 0, blueTotal = 0, greyTotal = 0, cyanTotal = 0, yellowTotal = 0, pinkTotal = 0, greenTotal = 0;
    int num = 0;
    private boolean showUnacc = true, showDesc = true, showName = false;

    public void getValues(int numberOfDays) {
        Log.d(TAG, "getValues: entering getValues");
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
        //if the user has saved before
        if (!keys.isEmpty()) {
            String[] arr = new String[31];//array is 8 instead of 7 because I am saving "Day"
            int ct = 0;
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.i("map", entry.getKey());
                //fill array with users savedPreferences
                arr[ct] = entry.getKey();
                ct++;
                Log.d(TAG, "getValues: inside map for loop");
                Log.d(TAG, "getValues: map loop");
            }

            int test = 0;
            if(ct-numberOfDays>=0) {
                test = ct - numberOfDays;
                numberOfDays--;

            }else
                numberOfDays = pref.getInt("Day",0);


            for (int i = test; i < arr.length; i++) {
                String tmp = arr[i];
                Log.d(TAG, "getValues: loop after map before if statment");
                if (i < ct - 1) {
                    Log.d(TAG, "getValues: loop after map AFTER if statment");
                    String a = keys.get(tmp).toString();
                    String[] split = a.split(",");

                    //add up each position form each savedPreferences
                    orangeTotal += Float.parseFloat(split[0]);
                    Log.d(TAG, "getValues: ORANGE IS "+Float.parseFloat(split[0]) );
                    redTotal += Float.parseFloat(split[1]);
                    purpleTotal += Float.parseFloat(split[2]);
                    blueTotal += Float.parseFloat(split[3]);
                    Log.d(TAG, "getValues: BLUE IS "+Float.parseFloat(split[3]) );
                    greyTotal += Float.parseFloat(split[4]);
                    cyanTotal += Float.parseFloat(split[5]);
                    Log.d(TAG, "getValues: CYAN IS "+Float.parseFloat(split[5]) );
                    yellowTotal += Float.parseFloat(split[6]);
                    pinkTotal += Float.parseFloat(split[7]);
                    greenTotal += Float.parseFloat(split[8]);
                    Log.d(TAG, "getValues: GREEN IS "+Float.parseFloat(split[8]) + "Green Total is "+ greenTotal);

                }
            }
            //assign val array according to the corresponding position
            val[8] = (greenTotal - (orangeTotal + redTotal + purpleTotal + blueTotal + greyTotal + cyanTotal + yellowTotal + pinkTotal))/greenTotal * 100;
            if(!showUnacc) {
                val[8] = 0.5f;
                greenTotal = (orangeTotal + redTotal + purpleTotal + blueTotal + greyTotal + cyanTotal + yellowTotal + pinkTotal);
            }
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
            NumberFormat numberFormat = new DecimalFormat("00");
            if(showDesc)
                description = "Total Time "+ numberFormat.format(hr)+":"+ numberFormat.format(min) +":"+numberFormat.format(sec)+" over the past "+numberOfDays+ " days!";
            else
                description = " ";
            Log.d(TAG, "getValues: exiting getResults");

            Description d = new Description();

            d.setText(description);
            d.setTextAlign(Paint.Align.RIGHT);
            d.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_description));
            pie.setDescription(d);

            pie.setTransparentCircleAlpha(128);
            pie.setTransparentCircleRadius(30f);
            //   pie.setRotationEnabled(true);
            pie.setHoleRadius(22f);
            pie.setCenterText("Total Time!");

            pie.setCenterTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));
            pie.setEntryLabelTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));

            //pie.setDrawEntryLabels(false);
            //pie.setUsePercentValues(true);
            Log.d(TAG, "onCreateView: before setting dataSet");
            addDataSet();
            Log.d(TAG, "onCreateView: after setting dataSet");
            pie.setEntryLabelColor(Color.BLACK);
        }
    }

    //draw pie chart and populate it.
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_stats_tabbed, container, false);


        Log.d(TAG, "onCreateView: ");
        pie = (PieChart) rootView.findViewById(R.id.pie);
        Spinner spinner = rootView.findViewById(R.id.tab1Spinner);
        button = rootView.findViewById(R.id.tab1Button);
        List<String> categories = new ArrayList<String>();
        // ,getString(R.string.Relaxing)};
        categories.add("Show past 30 days (Max)");
        categories.add("Show past 14 days ");
        categories.add("Show past 7 days (Min)");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.my_spinner_dropdown_item, categories);

        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int selectedItem = parent.getSelectedItemPosition();
                if(selectedItem == 0)
                {
                  //  getValues(31);
                    Log.d(TAG, "onItemSelected: selected 31 ");
                    orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
                    getValues(31);
                    num=31;
                    addDataSet();
                    //orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
//                    rootView.refreshDrawableState();

                }
                else if(selectedItem == 1)
                {
                    //getValues(15);
                    orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
                    getValues(15);
                    num=15;
                    addDataSet();
                    Log.d(TAG, "onItemSelected: selected 15 ");

                }
                else {
                    //getValues(8);
                    orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
                    getValues(8);
                    num=8;
                    addDataSet();
                    Log.d(TAG, "onItemSelected: selected 8 ");
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
               // LayoutInflater inflater = rootView.getLayoutInflater();
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
                        else pie.setRotationEnabled(true);

                        if(!unaccounted.isChecked())
                            showUnacc=false;
                        else
                            showUnacc=true;
                        if(!desc.isChecked()) {
                            description="";
                            showDesc=false;

                            //pie.invalidate();
                        }
                        else {
                            showDesc = true;
                            //pie.invalidate();
                        }
                        orangeTotal = 0; redTotal = 0; purpleTotal = 0; blueTotal = 0; greyTotal = 0; cyanTotal = 0; yellowTotal = 0; pinkTotal = 0; greenTotal = 0;
                        getValues(num);
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });


        Log.d(TAG, "onCreateView: before chartValue");
        pie.setOnChartValueSelectedListener( new OnChartValueSelectedListener(){
            @Override
            public void onValueSelected(Entry e, Highlight h){
                Log.d(TAG, "onValueSelected: entered");
                Log.d(TAG, "onValueSelected: "+e.toString());

                int pos = e.toString().indexOf("y: ");
                Log.d(TAG, "onValueSelected: pos orignal value is " + pos);
                String sub = e.toString().substring(pos+3);
                Log.d(TAG, "onValueSelected: sub orignal value is " + sub);
                float x = greenTotal/1000;

                Log.d(TAG, "\n onValueSelected: INSIDE COMPARE LOOP");
                for(int i = 0; i<val.length;i++) {
                    Log.d(TAG, "onValueSelected: val["+i+"] is: "+val[i] );//+ "\n"+"\t "+pie.getData().getDataSet().getEntriesForXValue(i));

                }


                for(int i = 0; i<val.length;i++){
                    Log.d(TAG, "onValueSelected: in for loop i is "+ i + "and val[i] is "+val[i]);
                    String s = String.format("%.2f",val[i]);
                    if(s.equals(String.format("%.2f",Float.parseFloat(sub)))){

                        pos = i;
                        Log.d(TAG, "onValueSelected: if(val...): pos is "+pos);
                        //  z = val[i];
                        break;
                    }
                }

                Log.d(TAG, "onValueSelected: after loop val length is " + val.length);

                Log.d(TAG, "onValueSelected: x is " + x + " val[pos] is "+ val[pos]);
                int sec =  Math.round(x*(val[pos]/100));
                int min = (int) Math.floor(sec / 60);
                int hr = (int) Math.floor(min / 60);
                Log.d(TAG, "onValueSelected: sec is : "+ sec);
                sec = sec - (min * 60);
                min = min - (hr * 60);

                Log.d(TAG, "onValueSelected: sec is : "+ sec);


                NumberFormat numberFormat = new DecimalFormat("00");

                String text  =  "Time spent: "+numberFormat.format(hr)+":"+ numberFormat.format(min) +":"+numberFormat.format(sec);

                Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(){

            }
        });


        Log.d(TAG, "onCreateView: after chartValue");

        return rootView;
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        for(int i = 0; i< val.length;i++){
            if(val[i]>1) {
                yEntry.add(new PieEntry(val[i], name[i], i));
                Log.d(TAG, "addDataSet: val["+i+"] is "+ val[i]);
            }
        }

        for(int i = 1; i<name.length;i++){
            xEntry.add(name[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntry,"");
        pieDataSet.setSliceSpace(2);



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

            if (val[8] > 1)
                colors.add(Color.parseColor("#44FF44"));


        Legend legend = pie.getLegend();

        legend.setForm(Legend.LegendForm.SQUARE);
        pie.setDrawEntryLabels(showName);
        pie.animateXY(1000,1000);
        legend.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_legend));

        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        Log.d(TAG, "addDataSet: legend CENTER");
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        Log.d(TAG, "addDataSet: legend BOTTOM");
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        Log.d(TAG, "addDataSet: legend HORIZONTAL");
        legend.setDrawInside(false);
        Log.d(TAG, "addDataSet: legend DRAW INSIDE FALSE");
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        Log.d(TAG, "addDataSet: legend LEFT TO RIGHT");
      //  legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);



        pieDataSet.setValueTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_stats_description));

        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);

        pie.setData(pieData);
        pie.invalidate();
        pie.getLegend().setWordWrapEnabled(true);

        Log.d(TAG, "addDataSet: leaving");
    }
}