package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class testActivity extends AppCompatActivity {

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
    Button ib_o, ib_r, ib_p, ib_g, ib_b, ib_gr, ib_c, ib_y, ib_pk;
    long result;
    myTimer orange, red, purple, green, blue, grey, cyan, yellow, pink;
    Button endDay, reflect, adjust, stat;
    ScrollView mScrollView;
    Button curr;
    Chronometer curr_c;

    //following methods are not currently in use
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

                        if (value) { //Ending day
                            Toast.makeText(testActivity.this,
                                    "Day has ended!", Toast.LENGTH_SHORT).show();
                            //Activate all buttons and reset
                            saveDay();
                            setEnableBtns(value);
                            reset();
                        }
                        else {//Reflecting on day
                            Toast.makeText(testActivity.this,
                                    "Reflect on the day!", Toast.LENGTH_SHORT).show();
                            //Deactivate all buttons and stops all timers
                            setEnableBtns(value);
                            myTimer.stopAll();
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

        curr_c.setBase(SystemClock.elapsedRealtime());
        curr_c.stop();
        curr.setText("Nothing is running");

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
        boolean affect = data.getBooleanExtra("affect",true);
        result = data.getLongExtra("sub", 0);//get the result from data
        String cat = data.getStringExtra("cat");//get the categories from data
        int x;
        switch (requestCode) {

            case (FINAL_INT): {
                if (resultCode == Activity.RESULT_OK) {
                    switch (cat) {
                        case "Leisure":
                            x = orange.getTextValueMilli();
                            if (x - result > 0)
                                orange.add(result,affect);
                            break;

                        case "Exercise":
                            x = red.getTextValueMilli();
                            if (x - result > 0)
                                red.add(result,affect);
                            break;

                        case "Education":
                            x = purple.getTextValueMilli();
                            if (x - result > 0)
                                purple.add(result,affect);
                            break;

                        case "Work":
                            x = blue.getTextValueMilli();
                            if (x - result > 0)
                                blue.add(result,affect);
                            break;

                        case "Other":
                            x = grey.getTextValueMilli();
                            if (x - result > 0)
                                grey.add(result,affect);
                            break;

                        case "Preparation":
                            x = cyan.getTextValueMilli();
                            if (x - result > 0)
                                cyan.add(result,affect);
                            break;

                        case "Traveling":
                            x = yellow.getTextValueMilli();
                            if (x - result > 0)
                                yellow.add(result,affect);
                            break;

                        case "Nap":
                            x = pink.getTextValueMilli();
                            if (x - result > 0)
                                pink.add(result,affect);
                            break;

                        default:

                    }
                }
                break;
            }
        }
        ib_g.setEnabled(false);


    }

    /**
     * saves the day after user has chosen to end day.

     */

    public void saveDay(){

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

        Toast.makeText(testActivity.this, str + "", Toast.LENGTH_SHORT).show();


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

    private Boolean isViewVisible(View view) {
        Rect scrollBounds = new Rect();
        mScrollView.getHitRect(scrollBounds);
        if (view.getLocalVisibleRect(scrollBounds)) {
            //     Toast.makeText(MainActivity.this,"in",Toast.LENGTH_SHORT).show();
            curr.setVisibility(View.GONE);
            curr_c.setVisibility(View.GONE);
            return true;
            // Any portion of the imageView, even a single pixel, is within the visible window
        } else {
            curr.setVisibility(View.VISIBLE);
            curr_c.setVisibility(View.VISIBLE);
            //    Toast.makeText(MainActivity.this,"out",Toast.LENGTH_SHORT).show();
            return false;
            // NONE of the imageView is within the visible window
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        curr = findViewById(R.id.imgBtnCurr);
        curr_c = findViewById(R.id.chronometerCurr);
        mScrollView = findViewById(R.id.scrollView02);
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


        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences pref2 = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref2.edit();
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




        /*Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,testActivity.class);
                startActivity(intent);
            }
        });*/



        /**
         * When clicked start adjustTimer for a result
         */
        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);

                Intent i = new Intent(testActivity.this,adjustTimers.class);
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
                v.startAnimation(animScale);

                showAlert(v,"Do you want to end the day",false);

                editor.clear();

            }
        }));
        stat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(testActivity.this,statsTabbed.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                //  int scrollX = rootScrollView.getScrollX(); //for horizontalScrollView
                //  int scrollY = rootScrollView.getScrollY(); //for verticalScrollView
                //DO SOMETHING WITH THE SCROLL COORDINATES
                if(myTimer.getLastTimer().getLastTimer()!=null)
                    isViewVisible(myTimer.getLastTimer().getTimer());
                Log.d("basic","scroll");
            }
        });


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
                setCurr(orange,ib_o);
                v.startAnimation(animAlpha);
                //  orange.start();

                ib_g.setEnabled(false);
            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(red,ib_r);
                v.startAnimation(animAlpha);
                //  red.start();

                ib_g.setEnabled(false);
            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(purple,ib_p);
                v.startAnimation(animAlpha);
                //  purple.start();

                ib_g.setEnabled(false);
            }
        });
        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(blue,ib_b);
                v.startAnimation(animAlpha);
                //blue.start();
                ib_g.setEnabled(false);

            }
        });
        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(grey,ib_gr);
                v.startAnimation(animAlpha);
                //grey.start();

                ib_g.setEnabled(false);
            }
        });
        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(cyan,ib_c);
                v.startAnimation(animAlpha);
                //cyan.start();

                ib_g.setEnabled(false);
            }
        });
        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(yellow,ib_y);
                v.startAnimation(animAlpha);
                // yellow.start();

                ib_g.setEnabled(false);
            }
        });
        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setCurr(pink,ib_pk);
                v.startAnimation(animAlpha);
                //   pink.start();
                ib_g.setEnabled(false);

            }
        });

        re();
    }

    public void setCurr(myTimer timer, Button button){
        isViewVisible(timer.getTimer());
        curr.setText(button.getText());
        curr_c.stop();
        curr_c.setBase(SystemClock.elapsedRealtime());

      /*  String string[] = timer.getTimer().getText().toString().split(":");
        if(string.length==2){
           curr_c.setBase( -1 * ((Integer.parseInt(string[0])*60*1000) + (Integer.parseInt(string[1])*1000))+SystemClock.elapsedRealtime());
        }
        else
            curr_c.setBase( -1 *(Integer.parseInt(string[0])*60*60*1000) + (Integer.parseInt(string[1])*60*1000) + (Integer.parseInt(string[2])*1000)+SystemClock.elapsedRealtime());
        */
        if(timer.getLastPause()>0)
            curr_c.setBase(timer.getTimer().getBase() + SystemClock.elapsedRealtime() - timer.getLastPause());
        else
            curr_c.setBase(SystemClock.elapsedRealtime());
        curr_c.start();
        timer.start();

    }
    public void re(){
        final SharedPreferences pref = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        String b = pref.getString("orange","00:00");
        long c = pref.getLong("green",red.getTimer().getBase());
        long l;
        String a[];
        a = b.split(":");//.getText().toString().split(":");
        if(a.length == 2)
            l =  ( (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000));
        else
            l = ( (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000));


        //   red.getTimer().setBase(c);
        //   Toast.makeText(this,b+" meth!! "+l,Toast.LENGTH_SHORT).show();

    }
/*
    @Override
    public void onResume(){

        Toast.makeText(this," Resuming!! ",Toast.LENGTH_SHORT).show();

        super.onResume();
    }
    @Override
    public void onStart(){
        Toast.makeText(this,"Starting!",Toast.LENGTH_SHORT).show();
        super.onStart();

    }
    @Override
    public void onStop(){
        Toast.makeText(this,"Stopping!",Toast.LENGTH_SHORT).show();

        super.onStop();
    }
    @Override
    public void onPause(){
        Toast.makeText(this,"Pauseing! "+ orange.getTimer().getBase(),Toast.LENGTH_SHORT).show();


        final SharedPreferences pref = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        if(orange.getLastPause()!=0)
            editor.putLong("green",orange.getTimer().getBase());
        editor.putString("orange", orange.getTimer().getText().toString() );//
        editor.apply();
        super.onPause();
    }
    @Override
    public void finish(){
        Toast.makeText(this,"Finishing!",Toast.LENGTH_SHORT).show();
        super.finish();
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this,"Destroying!",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }*/


    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Min!",Toast.LENGTH_LONG).show();
        moveTaskToBack(true);


    }



}