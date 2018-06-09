package com.example.jc.timemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class statsMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_main);
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode

        TextView tv1 = findViewById(R.id.textViewStats);
        TextView tv2 = findViewById(R.id.textViewStatsTitle);

        TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
        if(!keys.isEmpty()){
            String [] arr = new String[8];
            int ct=0;
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                Log.i("map", entry.getKey());
                //some code
                arr[ct] = entry.getKey();
                ct++;
            }

            float orangeTotal=0,redTotal=0,purpleTotal=0,blueTotal=0,greyTotal=0,cyanTotal=0,yellowTotal=0,pinkTotal=0,greenTotal=0;
            for(int i = 0; i<arr.length;i++){
                String tmp = arr[i];
               // Toast.makeText(statsMain.this, "I is " + i + "  ct is "+ ct, Toast.LENGTH_SHORT).show();
                if(i<ct-1) {
                    String a = keys.get(tmp).toString();
                    String[] split = a.split(",");
                    //Toast.makeText(statsMain.this, "adsfasdf   " +tmp, Toast.LENGTH_SHORT).show();
                    String tmp1 = split[0];
                    orangeTotal += Float.parseFloat(tmp1);
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
            int sec = Math.round(greenTotal/1000);
            int min = (int)Math.floor(sec/60);
            int hr = (int)Math.floor(min/60);
            sec = sec-(min*60);
            min = min-(hr*60);
            String msg = "Total Time doing Leisure: " + (Math.round((orangeTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Exercise: " + (Math.round((redTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Education: " + (Math.round((purpleTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Work: " + (Math.round((blueTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Other: " + (Math.round((greyTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Preparation: " + (Math.round((cyanTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Traveling: " + (Math.round((yellowTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time doing Nap: " + (Math.round((pinkTotal/greenTotal) *10000d)/100d) + "%"
                    +"\n"+"Total Time in Milliseconds : " + greenTotal
                    +"\n"+"Total Time (hh:mm:ss) :   " + hr+":"+ min +":"+sec;

            tv1.setText(msg);
        }



    }

}
