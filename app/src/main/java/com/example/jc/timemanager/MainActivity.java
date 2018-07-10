package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * MainActivity
 * -Will contain 9 myTimers, 1 of which is a WakeUp timer.
 * -As well as 3 buttons, one for ending the day, another for reflecting on the day,
 * and another for adjusting the values for a myTimer
 */
public class MainActivity extends AppCompatActivity {


    /**
     * Chronometers to pass the id of the chronometers to a myTimer
     */
    Chronometer cm_o, cm_r, cm_p, cm_g, cm_b, cm_gr, cm_c, cm_y, cm_pk;
    public static final int FINAL_INT = 0;
    /**
     * ImageButtons for starting/swapping myTimers
     * A long for retrieving the results from adjustTimer class
     * myTimers for managing chronometers
     */
    ImageButton ib_o, ib_r, ib_p, ib_g, ib_b, ib_gr, ib_c, ib_y, ib_pk;
    long result;
    myTimer orange, red, purple, green, blue, grey, cyan, yellow, pink;
    Button endDay, reflect, adjust, stat;

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    /**
     * -Display a dialog box that ask the users if they want to end the day
     * or ask the user if they want to reflect on the day.
     *
     * @param view  is never used however may be used in later versions
     * @param msg   the message to be displayed
     * @param value to decide if we are ending the day or reflecting, and then
     *              pass value to setEnableBtns
     */
    public void showAlert(View view, String msg, final Boolean value) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //Display msg
        alert.setMessage(msg)

                .setNegativeButton("Not yet!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .setPositiveButton("I'm ready!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        if (value) {
                            Toast.makeText(MainActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                            // saveData();
                            save();
                            setEnableBtns(value);//activate all buttons, except adjust and endDay
                            reset();//resets all timers
                        } else {
                            Toast.makeText(MainActivity.this, "Reflect on the day!", Toast.LENGTH_SHORT).show();
                            setEnableBtns(value);//Deactivate all buttons, except adjust and endDay
                            myTimer.stopAll();//stops all myTimers
                        }
                    }
                })

                .create();
        alert.show();

    }

    /**
     * Reset all myTimers
     */
    public void reset() {
        orange.reset();
        red.reset();
        purple.reset();
        blue.reset();
        grey.reset();
        cyan.reset();
        yellow.reset();
        pink.reset();

    }

    /**
     * Enable or disable  all buttons, except adjust and endDay
     *
     * @param val used to enable or disable the buttons
     */
    public void setEnableBtns(Boolean val) {
        ib_o.setEnabled(val);
        ib_p.setEnabled(val);
        ib_r.setEnabled(val);
        ib_g.setEnabled(val);

        ib_b.setEnabled(val);
        ib_gr.setEnabled(val);
        ib_c.setEnabled(val);
        ib_y.setEnabled(val);
        ib_pk.setEnabled(val);
        reflect.setEnabled(val);
    }

    /**
     * Get results from adjustTimer class and apply that to the proper myTimer
     *
     * @param requestCode
     * @param resultCode
     * @param data        get the Intent data from adjustTimer
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (FINAL_INT): {
                if (resultCode == Activity.RESULT_OK) {
                    result = data.getLongExtra("sub", 0);//get the result from data
                    String cat = data.getStringExtra("cat");//get the categories from data
                    if (cat.equals("Leisure") ) {
                        String a [] = orange.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        orange.add(result);//add the results
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Exercise") ) {
                        String a [] = red.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        red.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Education")) {
                        String a [] = purple.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        purple.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Work")) {
                        String a [] = blue.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        blue.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Other") ) {
                        String a [] = grey.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        grey.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Preparation")) {
                        String a [] = cyan.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        cyan.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Traveling") ) {
                        String a [] = yellow.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        yellow.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else if (cat.equals("Nap") ) {
                        String a [] = pink.getTimer().getText().toString().split(":");
                        int x;
                        if(a.length==3)
                            x = (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000);

                        else
                            x = (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000);
                        //Toast.makeText(MainActivity.this,"x is " + x + "\n" + "t is "+orange.getTimer().getBase() + "\n" + "r is "+result,Toast.LENGTH_LONG).show();

                        if(x-result>0)
                        pink.add(result);
                        else
                            Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    }
                    ib_g.setEnabled(false);

                }
                break;
            }
        }
    }

    /**
     * saves the day after user has chosen to end day.

     */

    public void save(){

        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        float o,r,p,b,gr,c,y,pk,sum;
        //gets the value from the text of each chronometer

        o = (float) orange.getTextValueMilli();
        r = (float) red.getTextValueMilli();
        p =(float) purple.getTextValueMilli();
        b = (float) blue.getTextValueMilli();
        gr =(float) grey.getTextValueMilli();
        c = (float) cyan.getTextValueMilli();
        y = (float) yellow.getTextValueMilli();
        pk = (float) pink.getTextValueMilli();

        //use the sum of all the timers instead of text from the wakeUp timer.
        sum = o+r+p+b+gr+c+y+pk;

        //day is a counter for my current day which can not be more than 7
        //this counter is not necessary and will probably be removed
        int day = pref.getInt("Day",0);
        if (day>30)
            day = 30;

        //str is going to hold our value
        String str= o + ","+r + ","+p + ","+b + ","+gr + ","+c + ","+y + ","+pk + ","+sum;

        Toast.makeText(MainActivity.this, str + "", Toast.LENGTH_SHORT).show();


        editor.putInt("Day", (day + 1));
        Log.i("map","Day is "+day);


        //currentDateandTime is going to hold our key
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());
        if(day>=30) {
            //Since 7 is our maximum amount of days we remove the first day we put in
            TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
            String firstEntry = keys.firstKey();
            editor.remove(firstEntry);
        }
        //add to savedPreferences
        //key looks like "20180601_123430"
        //value looks like "1000,2000,3000,4000,5000,6000,7000,8000"
        editor.putString(currentDateAndTime, str);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"Creating!",Toast.LENGTH_LONG).show();
        //set imagebuttons to corresponding imagebuttons on layout
        ib_o = findViewById(R.id.imgBtnOrange);
        ib_r = findViewById(R.id.imgBtnRed);
        ib_g = findViewById(R.id.imgBtnGreen);
        ib_p = findViewById(R.id.imgBtnPurple);
        ib_b = findViewById(R.id.imgBtnBlue);
        ib_gr = findViewById(R.id.imgBtnGrey);
        ib_c = findViewById(R.id.imgBtnCyan);
        ib_y = findViewById(R.id.imgBtnYellow);
        ib_pk =findViewById(R.id.imgBtnPink);

        //set chronometer to corresponding chronometer on layout
        cm_o = findViewById(R.id.chronometerOrange);
        cm_r = findViewById(R.id.chronometerRed);
        cm_p = findViewById(R.id.chronometerPurple);
        cm_g = findViewById(R.id.chronometerGreen);
        cm_b = findViewById(R.id.chronometerBlue);
        cm_gr = findViewById(R.id.chronometerGrey);
        cm_c = findViewById(R.id.chronometerCyan);
        cm_y = findViewById(R.id.chronometerYellow);
        cm_pk = findViewById(R.id.chronometerPink);

        //set buttons to corresponding buttons on layout
        endDay = findViewById(R.id.btnEndDay);
        reflect = findViewById(R.id.btnReset);
        adjust = findViewById(R.id.btnAdjust);
        stat = findViewById(R.id.btnStat);

        TextView textView = findViewById(R.id.textView);
        textView.append("  "+endDay.getTextSize());
        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        // final SharedPreferences.Editor editor = pref.edit();


        //set myTimers, which requier a chronometer and a boolean value(true only for wakeUp)
        long tmp = pref.getLong("endTime",0);
        green = new myTimer(cm_g,true);
        orange = new myTimer(cm_o,false);
        red = new myTimer(cm_r,false);
        purple = new myTimer(cm_p,false);
        blue = new myTimer(cm_b,false);
        grey = new myTimer(cm_gr,false);
        cyan = new myTimer(cm_c,false);
        yellow = new myTimer(cm_y,false);
        pink = new myTimer(cm_pk,false);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);




        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,testActivity.class);
                startActivity(intent);
            }
        });

        /**
         * When clicked start adjustTimer for a result
         */
        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);

                Intent i = new Intent(MainActivity.this,adjustTimers.class);
                startActivityForResult(i, FINAL_INT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        /**
         * When clicked call showAlert() and pass a message and true
         */
        endDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                v.startAnimation(animScale);

                showAlert(v,"Do you want to end the day",true);

            }
        });
        /**
         * When clicked call showAlert() and pass a message and false
         */
        reflect.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert(v,"Do you want to reflect on the day",false);

            }
        }));
        stat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainActivity.this,statsTabbed.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));

        /**
         * When a imageButton is clicked start the corresponding myTimer and disable ib_g
         */
        ib_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                myTimer.wakeUp(SystemClock.elapsedRealtime());//start the wakeUp timer
                ib_g.setEnabled(false);
               // Toast.makeText(MainActivity.this,"Start another timer to start wake up",Toast.LENGTH_SHORT).show();
            }
        });
        ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                orange.start();

                ib_g.setEnabled(false);
            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                red.start();

                ib_g.setEnabled(false);
            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                purple.start();
                ib_g.setEnabled(false);
            }
        });
        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                blue.start();
                ib_g.setEnabled(false);
            }
        });
        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                grey.start();
                ib_g.setEnabled(false);
            }
        });
        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                cyan.start();
                ib_g.setEnabled(false);
            }
        });
        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                yellow.start();
                ib_g.setEnabled(false);
            }
        });
        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                pink.start();
                ib_g.setEnabled(false);
            }
        });
    }
    @Override
    public void onResume(){
      //  Toast.makeText(this,"Resuming!!",Toast.LENGTH_LONG).show();

        super.onResume();
    }
    @Override
    public void onStart(){
       // Toast.makeText(this,"Starting!",Toast.LENGTH_LONG).show();
        super.onStart();

    }
    @Override
    public void onStop(){
      //  Toast.makeText(this,"Stopping!",Toast.LENGTH_LONG).show();

        super.onStop();
    }
    @Override
    public void onPause(){
      //  Toast.makeText(this,"Pauseing!",Toast.LENGTH_LONG).show();

        super.onPause();
    }
    @Override
    public void onDestroy(){
      //  Toast.makeText(this,"Destroying!",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Min!",Toast.LENGTH_LONG).show();
        moveTaskToBack(true);


    }



}