package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;


import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.ScrollView;
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
    Boolean comingFromActivity = false;
    String cat="";
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
                            Toast.makeText(MainActivity.this,
                                    "Day has ended!", Toast.LENGTH_SHORT).show();
                            final SharedPreferences preferences = MainActivity.this.getSharedPreferences("timerValues", 0); // 0 - for private mode
                            final SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();
                            cat="";
                            //Activate all buttons and reset
                            saveDay();
                            setEnableBtns(value);
                            reset();
                        }
                        else {//Reflecting on day
                            Toast.makeText(MainActivity.this,
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

        result = data.getLongExtra("sub", 0);//get the result from data
        String cat1 = data.getStringExtra("cat");//get the categories from data
        int x;
        if (resultCode == Activity.RESULT_OK) {
            switch (cat1) {
                case "Leisure":
                    x = orange.getTextValueMilli();
                    if (x - result > 0)
                        orange.add(result);
                    build(orange,"Leisure");
                    break;

                case "Exercise":
                    x = red.getTextValueMilli();
                    if (x - result > 0)
                        red.add(result);
                    build(red,"Exercise");
                    break;

                case "Education":
                    x = purple.getTextValueMilli();
                    if (x - result > 0)
                        purple.add(result);
                    build(purple,"Education");
                    break;

                case "Work":
                    x = blue.getTextValueMilli();
                    if (x - result > 0)
                        blue.add(result);
                    build(blue,"Work");
                    break;

                case "Other":
                    x = grey.getTextValueMilli();
                    if (x - result > 0)
                        grey.add(result);
                    build(grey,"Other");
                    break;

                case "Preparation":
                    x = cyan.getTextValueMilli();
                    if (x - result > 0)
                        cyan.add(result);
                    build(cyan,"Preparation");
                    break;

                case "Traveling":
                    x = yellow.getTextValueMilli();
                    if (x - result > 0)
                        yellow.add(result);
                    build(yellow,"Traveling");
                    break;

                case "Nap":
                    x = pink.getTextValueMilli();
                    if (x - result > 0)
                        pink.add(result);
                    build(pink,"Relaxing");
                    break;

                default:

            }
            ib_g.setEnabled(false);
        }

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

  /*  private Boolean isViewVisible(View view) {
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
*/
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
        final SharedPreferences pref2 = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref2.edit();
        cat = pref2.getString("running","");
        // final SharedPreferences.Editor editor = pref.edit();


        //set myTimers, which requier a chronometer and a boolean value(true only for wakeUp)
        long tmp = pref.getLong("endTime",0);

        Log.d("mainActivity", "onCreate: "+cat);
        green = new myTimer(cm_g,true);
        orange = new myTimer(cm_o,pref2.getLong("orange",0),cat.equals("orange"));
        red = new myTimer(cm_r,pref2.getLong("red",0),cat.equals("red"));
        purple = new myTimer(cm_p,pref2.getLong("purple",0),cat.equals("purple"));
        blue = new myTimer(cm_b,pref2.getLong("blue",0),cat.equals("blue"));
        grey = new myTimer(cm_gr,pref2.getLong("grey",0),cat.equals("grey"));
        cyan = new myTimer(cm_c,pref2.getLong("cyan",0),cat.equals("cyan"));
        yellow = new myTimer(cm_y,pref2.getLong("yellow",0),cat.equals("yellow"));
        pink = new myTimer(cm_pk,pref2.getLong("pink",0),cat.equals("pink"));

        switch (cat) {
            case "orange":
                orange.start();
                myTimer.wakeUp(pref2.getLong("green",0));
           //     ib_o.setPressed(true);
                Log.d("mainActivity", "onCreate: orange");
                break;

            case "red":
                Log.d("mainActivity", "onCreate: red");
                red.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "purple":
                Log.d("mainActivity", "onCreate: purple");
                purple.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "blue":
                blue.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                //     ib_o.setPressed(true);
                Log.d("mainActivity", "onCreate: blue");
                break;

            case "grey":
                Log.d("mainActivity", "onCreate: grey");
                grey.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "cyan":
                Log.d("mainActivity", "onCreate: cyan");
                cyan.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "yellow":
                Log.d("mainActivity", "onCreate: yellow");
                yellow.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "pink":
                Log.d("mainActivity", "onCreate: pink");
                pink.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                break;
            case "paused":
                Log.d("mainActivity", "onCreate: paused");

                myTimer.wakeUp(pref2.getLong("green",0));

            default:
                Log.d("mainActivity", "onCreate: def");
        }

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);




/*        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           // build();
           //     Intent intent = new Intent(MainActivity.this,testActivity.class);
             //   startActivity(intent);
            }
        });*/



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
                v.startAnimation(animScale);

                showAlert(v,"Do you want to reflect on the day",false);
                final SharedPreferences pref3 = MainActivity.this.getSharedPreferences("timerValues", 0); // 0 - for private mode
                final SharedPreferences.Editor ed = pref3.edit();
                ed.clear();
                ed.apply();


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
                v.startAnimation(animScale);
                myTimer.wakeUp(SystemClock.elapsedRealtime());//start the wakeUp timer
                ib_g.setEnabled(false);
               // Toast.makeText(MainActivity.this,"Start another timer to start wake up",Toast.LENGTH_SHORT).show();
            }
        });
        ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setCurr(orange,ib_o);
                v.startAnimation(animScale);
                orange.start();
                Log.d("mainActivity", "onCreate: ib_o");

                cat = "orange";
                if(orange.isPaused())
                    cat = "paused";

                build(orange,"Leisure");

                ib_g.setEnabled(false);
                Log.d("mainActivity", "onCreate: "+cat);
            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  setCurr(red,ib_r);
                v.startAnimation(animScale);
                red.start();
                cat = "red";
                if(red.isPaused())
                    cat = "paused";
                build(red,"Exercise");
                ib_g.setEnabled(false);
                Log.d("mainActivity", "onCreate: "+cat);
            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // setCurr(purple,ib_p);
                v.startAnimation(animScale);
                purple.start();
                cat = "purple";

                if(purple.isPaused())
                    cat = "paused";
                build(purple,"Education");
                ib_g.setEnabled(false);Log.d("mainActivity", "onCreate: "+cat);
            }
        });
        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //setCurr(blue,ib_b);
                v.startAnimation(animScale);
                blue.start();
                cat = "blue";
                if(blue.isPaused())
                    cat = "paused";
                build(blue,"Work");
                ib_g.setEnabled(false);

            }
        });
        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             //   setCurr(grey,ib_gr);
                v.startAnimation(animScale);
                grey.start();
                cat = "grey";
                if(grey.isPaused())
                    cat = "paused";
                build(grey,"Other");
                ib_g.setEnabled(false);
            }
        });
        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             //   setCurr(cyan,ib_c);
                v.startAnimation(animScale);
                cyan.start();
                cat = "cyan";
                if(cyan.isPaused())
                    cat = "paused";
                build(cyan,"Preparation");
                ib_g.setEnabled(false);
            }
        });
        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // setCurr(yellow,ib_y);
                v.startAnimation(animScale);
                yellow.start();
                cat = "yellow";
                if(yellow.isPaused())
                    cat = "paused";
                build(yellow,"Traveling");
                ib_g.setEnabled(false);
            }
        });
        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // setCurr(pink,ib_pk);
                v.startAnimation(animScale);
                pink.start();
                cat = "pink";
                if(pink.isPaused())
                    cat = "paused";
                build(pink,"Relaxing");
                ib_g.setEnabled(false);

            }
        });



    }



    private void build(myTimer timer,String name) {


        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.remote_view_test);
        remoteViews.setChronometer(R.id.remoteChrono,timer.getTimer().getBase(),timer.getTimer().getFormat(),true);
        remoteViews.setTextViewText(R.id.remoteText,name + " \t");
        //remoteViews.setTextColor(R.id.remoteText,);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channelId")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Title")
             //   .setContentText(orange.getTimer().getText().toString())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(false)
                .setCustomBigContentView(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent =  PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        //builder.setProgress(0, 0, true);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(timer.isPaused())
            manager.cancelAll();
        else
            manager.notify(0,builder.build());
}

    public void saveTimerValue(SharedPreferences.Editor editor){

        /*switch (cat){
            case "orange":

        }*/
        if(cat.equals("orange") )
            editor.putLong("orange",orange.getTimer().getBase());
        else
            editor.putLong("orange",orange.getTextValueMilli());

        if(cat.equals("red") )
            editor.putLong("red",red.getTimer().getBase());
        else
            editor.putLong("red",red.getTextValueMilli());

        if(cat.equals("purple") )
            editor.putLong("purple",purple.getTimer().getBase());
        else
            editor.putLong("purple",purple.getTextValueMilli());

        if(cat.equals("blue") )
            editor.putLong("blue",blue.getTimer().getBase());
        else
            editor.putLong("blue",blue.getTextValueMilli());

        if(cat.equals("grey") )
            editor.putLong("grey",grey.getTimer().getBase());
        else
            editor.putLong("grey",grey.getTextValueMilli());

        if(cat.equals("cyan") )
            editor.putLong("cyan",cyan.getTimer().getBase());
        else
            editor.putLong("cyan",cyan.getTextValueMilli());

        if(cat.equals("yellow") )
            editor.putLong("yellow",yellow.getTimer().getBase());
        else
            editor.putLong("yellow",yellow.getTextValueMilli());

        if(cat.equals("pink") )
            editor.putLong("pink",pink.getTimer().getBase());
        else
            editor.putLong("pink",pink.getTextValueMilli());

    }
    public void saveClose(){
        Log.d("mainActivity", "saveClose: entered");
        final SharedPreferences preferences = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = preferences.edit();
        Log.d("mainActivity", "saveClose: preferences and editor");
        saveTimerValue(editor);
        editor.putLong("green",myTimer.getWakeUpBase());


        editor.putString("running",cat);

        Log.d("mainActivity", "saveClose: before applied");
        editor.apply();
        Log.d("mainActivity", "saveClose: applied");
        Toast.makeText(this," saveClose!! ",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onResume(){
        Log.d("mainActivity", "onResume: entered");
        Toast.makeText(this," Resuming!! ",Toast.LENGTH_SHORT).show();
        /*if(!comingFromActivity){
            Log.d("mainActivity", "onResume: !comingFromActivity");
            final SharedPreferences pref = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
            final SharedPreferences.Editor editor = pref.edit();
            Log.d("mainActivity", "onResume: pref");
            orange.getTimer().setBase(pref.getLong("orange",0));
            Log.d("mainActivity", "onResume: orange");
            red.getTimer().setBase(pref.getLong("red",0));
            purple.getTimer().setBase(pref.getLong("purple",0));
            blue.getTimer().setBase(pref.getLong("blue",0));
            grey.getTimer().setBase(pref.getLong("grey",0));
            cyan.getTimer().setBase(pref.getLong("cyan",0));
            yellow.getTimer().setBase(pref.getLong("yellow",0));
            pink.getTimer().setBase(pref.getLong("pink",0));
          //  green.getTimer().setBase(pref.getLong("green",0));
            Log.d("mainActivity", "onResume: received");
        }*/
        super.onResume();
    }
    @Override
    public void onStart(){
        Toast.makeText(this,"Starting!",Toast.LENGTH_SHORT).show();
       // comingFromActivity = true;
        super.onStart();
    }
    @Override
    public void onStop(){
        Toast.makeText(this,"Stopping!",Toast.LENGTH_SHORT).show();
        saveClose();
        super.onStop();
    }
/*    @Override
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