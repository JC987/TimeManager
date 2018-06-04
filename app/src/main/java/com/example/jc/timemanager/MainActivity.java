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
    Chronometer cm_o,cm_r,cm_p,cm_g,cm_b,cm_gr,cm_c,cm_y,cm_pk;
    public static final int FINAL_INT=0;
    /**
     * ImageButtons for starting/swapping myTimers
     * A long for retrieving the results from adjustTimer class
     * myTimers for managing chronometers
     */
    ImageButton ib_o,ib_r,ib_p,ib_g,ib_b,ib_gr,ib_c,ib_y,ib_pk;
    long result;
    myTimer orange,red,purple,green,blue,grey,cyan,yellow,pink;
    Button endDay,reflect,adjust,stat;

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
     * @param view is never used however may be used in later versions
     * @param msg the message to be displayed
     * @param value to decide if we are ending the day or reflecting, and then
     *             pass value to setEnableBtns
     */
    public void showAlert(View view, String msg, final Boolean value ){

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

                        if(value) {
                            Toast.makeText(MainActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                           // saveData();
                            setEnableBtns(value);//activate all buttons, except adjust and endDay
                            reset();//resets all timers
                        }
                        else{
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
    public void reset(){
        orange.reset();red.reset();purple.reset(); blue.reset();grey.reset();cyan.reset(); yellow.reset();pink.reset();

    }

    /**
     * Enable or disable  all buttons, except adjust and endDay
     * @param val used to enable or disable the buttons
     */
    public void setEnableBtns(Boolean val){
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
     * @param requestCode
     * @param resultCode
     * @param data get the Intent data from adjustTimer
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FINAL_INT) : {
                if (resultCode == Activity.RESULT_OK) {
                    result = data.getLongExtra("sub",0);//get the result from data
                    String cat = data.getStringExtra("cat");//get the categories from data
                    if(cat.equals("Leisure")){
                        orange.add(result);//add the results
                    }
                    if(cat.equals("Exercise")){
                        red.add(result);
                    }
                    if(cat.equals("Education")){
                        purple.add(result);
                    }
                    if(cat.equals("Work")){
                        blue.add(result);
                    }
                    if(cat.equals("Other")){
                        grey.add(result);
                    }
                    if(cat.equals("Preparation")){
                        cyan.add(result);
                    }
                    if(cat.equals("Traveling")){
                        yellow.add(result);
                    }
                    if(cat.equals("Nap")){
                        pink.add(result);
                    }
                    ib_g.setEnabled(false);

                }
                break;
            }
        }
    }

    public void calc(){

        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
         final SharedPreferences.Editor editor = pref.edit();

        double o,r,p,b,gr,c,y,pk,sum;
        float to,tr,tp,tb,tgr,tc,ty,tpk;
        o = orange.getTextValueMilli();
        r = red.getTextValueMilli();
        p = purple.getTextValueMilli();
        b = blue.getTextValueMilli();
        gr = grey.getTextValueMilli();
        c = cyan.getTextValueMilli();
        y = yellow.getTextValueMilli();
        pk = pink.getTextValueMilli();
        sum = o+r+p+b+gr+c+y+pk;

        int day = pref.getInt("Day",0);
        if (day>7)
            day = 7;
            String str="";
        to = (float) o;//(Math.round((o / sum) * 10000d) / 100d);
            //total = (float) d;
        tr = (float) r;//(Math.round((r / sum) * 10000d) / 100d);
        tp = (float) p;//(Math.round((p / sum) * 10000d) / 100d);
        tb = (float) b;//(Math.round((b / sum) * 10000d) / 100d);
        tgr = (float) gr;//(Math.round((gr / sum) * 10000d) / 100d);
        tc = (float) c;//(Math.round((c / sum) * 10000d) / 100d);
        ty = (float) y;//(Math.round((y / sum) * 10000d) / 100d);
        tpk = (float) pk;//(Math.round((pk / sum) * 10000d) / 100d);


        str = to + ","+tr + ","+tp + ","+tb + ","+tgr + ","+tc + ","+ty + ","+tpk + ","+sum;

            Toast.makeText(MainActivity.this, str + "", Toast.LENGTH_SHORT).show();
            //editor.clear();
            //  editor.putFloat("orange",total);
            editor.putInt("Day", (day + 1));
            Log.i("map","Day is "+day);
            Date currentDate = Calendar.getInstance().getTime();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
        if(day>=7) {

            TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
            String firstEntry = keys.firstKey();
            editor.remove(firstEntry);
        }
        editor.putString(currentDateandTime, str);
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

        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        // final SharedPreferences.Editor editor = pref.edit();


        //set myTimers, which requier a chronometer and a boolean value(true only for wakeUp)
        long tmp = pref.getLong("endTime",0);
        green = new myTimer(cm_g,true);
        orange = new myTimer(cm_o,false);//0,//pref.getLong("l",cm_o.getBase()));//orange = new myTimer(cm_o,false);//lp_o);
        red = new myTimer(cm_r,false);//0,pref.getLong("r",cm_r.getBase())-(tmp-SystemClock.elapsedRealtime()));//lp_r);
        purple = new myTimer(cm_p,false);//0,pref.getLong("p",cm_p.getBase())-(tmp-SystemClock.elapsedRealtime()));//lp_p);
        purple.getTimer().stop();
        red.getTimer().stop();
        /*if(pref.getLong("lastTimer",0) == red.getTimer().getBase())
            myTimer.setLastTimer(red);
        if(pref.getLong("lastTimer",0) == purple.getTimer().getBase())
            myTimer.setLastTimer(purple);
        */
        blue = new myTimer(cm_b,false);//0,pref.getLong("b",cm_b.getBase()));//lp_b);
        grey = new myTimer(cm_gr,false);//lp_gr);
        cyan = new myTimer(cm_c,false);//lp_c);
        yellow = new myTimer(cm_y,false);//0);
        pink = new myTimer(cm_pk,false);//0);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
       // setDefaults("name","Baseball",this);





        /**
         * When clicked start adjustTimer for a result
         */
        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);

                Intent i = new Intent(MainActivity.this,adjustTimers.class);
                startActivityForResult(i, FINAL_INT);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

            }
        });


        /**
         * When clicked call showAlert() and pass a message and true
         */
        endDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                v.startAnimation(animScale);
                calc();

             //   editor.putFloat("name", 50.0f);
                showAlert(v,"Do you want to end the day",true);

            }
        });
        /**
         * When clicked call showAlert() and pass a message and false
         */
        reflect.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences.Editor editor = pref.edit();
                v.startAnimation(animScale);
                editor.clear();
                editor.commit();
                showAlert(v,"Do you want to reflect on the day",false);

            }
        }));
        stat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainActivity.this,statsMain.class);
                startActivity(i);

            }
        }));

        /**
         * When a imageButton is clicked start the corresponding myTimer and disable ib_g
         */
        ib_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animAlpha);
                myTimer.wakeUp();//start the wakeUp timer
                ib_g.setEnabled(false);
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
        //orange.setDefaults("orange",this);
       // finish();

    }


    @Override
    public void finish(){
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        String [] z = myTimer.getWakeUp().getText().toString().split(":");

       // if(Integer.parseInt(z[1])>0) {
       /*if(orange.getLastPause()!=0)
            editor.putLong("l", orange.getTimer().getBase());
        if(red.getLastPause()!=0)
            editor.putLong("r", red.getTimer().getBase());
        if(purple.getLastPause()!=0)
            editor.putLong("p", purple.getTimer().getBase());
        if(blue.getLastPause()!=0)
            editor.putLong("b",blue.getTimer().getBase());*/
        if(myTimer.getLastTimer()==orange){
            editor.putLong("l",orange.getTimer().getBase());
            editor.putLong("p",purple.getTimer().getBase()+SystemClock.elapsedRealtime()-purple.getLastPause());

            editor.putLong("r",red.getTimer().getBase()+SystemClock.elapsedRealtime()-red.getLastPause());
            editor.putLong("endTime",SystemClock.elapsedRealtime());
            editor.putLong("lastTimer",myTimer.getLastTimer().getTimer().getBase());
            editor.putBoolean("bool",true);
        }
        //}


        editor.remove("name");
        editor.putString("name","explostion");


        editor.apply();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }


    /*@Override
    public void onStop(){
        SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        editor.putLong("l",orange.getTimer().getBase());
        editor.putLong("r",red.getTimer().getBase());
        editor.putLong("p",purple.getTimer().getBase());
        editor.putLong("b",blue.getTimer().getBase());
        editor.remove("name");
        editor.putString("name","Bam");
        editor.putLong("gr",grey.getTimer().getBase());
        editor.putLong("c",cyan.getTimer().getBase());
        editor.putLong("y",yellow.getTimer().getBase());
        editor.putLong("p",pink.getTimer().getBase());
        editor.apply();
        super.onStop();
    }*/

}