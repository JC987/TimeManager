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

import java.sql.Time;
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


    
    Chronometer cm_o, cm_r, cm_p, cm_g, cm_b, cm_gr, cm_c, cm_y, cm_pk;
    public static final int FINAL_INT = 0;
    ImageButton ib_o, ib_r, ib_p, ib_g, ib_b, ib_gr, ib_c, ib_y, ib_pk;
    myTimer orange, red, purple, green, blue, grey, cyan, yellow, pink;
    Button endDay, reflect, adjust, stat;
    String cat="";
    private static final String TAG = "mainActivity";
    TextView textViewTime;
    //Button curr;
    //Chronometer curr_c;
    //ScrollView mScrollView;
    //  Boolean comingFromActivity = false;
 
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
        Log.d(TAG, "onCreate: Instantiating views");

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
        textViewTime = findViewById(R.id.textViewTime);

        //set buttons to corresponding buttons on layout
        endDay = findViewById(R.id.btnEndDay);
        reflect = findViewById(R.id.btnReset);
        adjust = findViewById(R.id.btnAdjust);
        stat = findViewById(R.id.btnStat);


        final SharedPreferences pref = this.getSharedPreferences("MyPref", 0); // 0 - for private mode
        final SharedPreferences pref2 = this.getSharedPreferences("timerValues", 0); // 0 - for private mode

        cat = pref2.getString("running","");

        green = new myTimer(cm_g,true);
        orange = new myTimer(cm_o,pref2.getLong("orange",0),cat.equals("orange"));
        red = new myTimer(cm_r,pref2.getLong("red",0),cat.equals("red"));
        purple = new myTimer(cm_p,pref2.getLong("purple",0),cat.equals("purple"));
        blue = new myTimer(cm_b,pref2.getLong("blue",0),cat.equals("blue"));
        grey = new myTimer(cm_gr,pref2.getLong("grey",0),cat.equals("grey"));
        cyan = new myTimer(cm_c,pref2.getLong("cyan",0),cat.equals("cyan"));
        yellow = new myTimer(cm_y,pref2.getLong("yellow",0),cat.equals("yellow"));
        pink = new myTimer(cm_pk,pref2.getLong("pink",0),cat.equals("pink"));
        textViewTime.setText(pref2.getString("textTime",""));


        Log.d(TAG, "onCreate: Instantiating myTimers");

        switch (cat) {
            case "orange":
                orange.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                Log.d(TAG, "onCreate: resuming, start orange");
                break;

            case "red":
                Log.d(TAG, "onCreate: resuming, start red");
                red.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "purple":
                Log.d(TAG, "onCreate: resuming, start purple");
                purple.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "blue":
                blue.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                Log.d(TAG, "onCreate: resuming, start blue");
                ib_g.setEnabled(false);
                break;

            case "grey":
                Log.d(TAG, "onCreate: resuming, start grey");
                grey.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "cyan":
                Log.d(TAG, "onCreate: resuming, start cyan");
                cyan.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "yellow":
                Log.d(TAG, "onCreate: resuming, start yellow");
                yellow.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "pink":
                Log.d(TAG, "onCreate: resuming, start pink");
                pink.start();
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "green":
                Log.d(TAG, "onCreate: resuming, start green");
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "reflect":
                Log.d(TAG, "onCreate: ref");
                myTimer.wakeUp(myTimer.getWakeUpBase() - pref2.getLong("green",0));
                myTimer.stopAll();
                setEnableBtns(false);
                break;
            case "paused":
                Log.d(TAG, "onCreate: resuming, timers paused");
                myTimer.wakeUp(pref2.getLong("green",0));
                ib_g.setEnabled(false);
                break;

            default:
                Log.d(TAG, "onCreate: def");
        }

        if(textViewTime.getText().toString().endsWith("M")){
       //     setEnableBtns(false);

            //myTimer.stopAll();
        }

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);




        /*Button testButton = findViewById(R.id.testButton);
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
                Log.d(TAG, "adjust: onClick: goto adjustTimers");
                v.startAnimation(animScale);
                Intent i = new Intent(MainActivity.this,adjustTimers.class);
                startActivityForResult(i, FINAL_INT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        endDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                v.startAnimation(animScale);
                Log.d(TAG, "endDay: onClick: ");
                showAlert("Do you want to end the day",true);

            }
        });

        reflect.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                Log.d(TAG, "reflect: onClick: ");
                if(!myTimer.isWakeUpEnabled())
                    showAlert("Do you want to reflect on the day",false);


            }
        }));

        stat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "stat: onClick: goto statsTabbed");
                Intent i = new Intent(MainActivity.this,statsTabbed.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));



        //ImageButton pressed events

        ib_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                myTimer.wakeUp(SystemClock.elapsedRealtime());//start the wakeUp timer
                setTextViewTime();
                ib_g.setEnabled(false);
                cat = "green";
                Log.d(TAG, "ib_g: onClick: starting green");

              /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateAndTime = sdf.format(new Date());
                String split[] = currentDateAndTime.split("_");

                String out="";
                for(int i = 0; i<split[1].length(); i+=2){

                    out+=split[1].substring(i,i+2);
                    if(i<3)
                        out += " : ";
                }
                out += "  -  ";
                textViewTime.setText(out);
                */

            }
        });
        ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                orange.start();
                Log.d(TAG, "ib_o: onClick: starting orange");
                cat = "orange";
                setTextViewTime();
                if(orange.isPaused())
                    cat = "paused";

                build(orange,"Leisure");
                ib_g.setEnabled(false);

            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                red.start();
                Log.d(TAG, "ib_r: onClick: starting red");
                cat = "red";
                if(red.isPaused())
                    cat = "paused";
                build(red,"Exercise");
                setTextViewTime();
                ib_g.setEnabled(false);
            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                purple.start();
                Log.d(TAG, "ib_p: onClick: starting purple");
                cat = "purple";
                if(purple.isPaused())
                    cat = "paused";
                build(purple,"Education");
                setTextViewTime();
                ib_g.setEnabled(false);
            }
        });
        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                blue.start();
                Log.d(TAG, "ib_b: onClick: starting blue");
                cat = "blue";
                if(blue.isPaused())
                    cat = "paused";
                build(blue,"Work");
                setTextViewTime();
                ib_g.setEnabled(false);

            }
        });
        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                grey.start();
                Log.d(TAG, "ib_gr: onClick: starting grey");
                cat = "grey";
                if(grey.isPaused())
                    cat = "paused";
                build(grey,"Other");
                setTextViewTime();
                ib_g.setEnabled(false);
            }
        });
        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                cyan.start();
                Log.d(TAG, "ib_c: onClick: starting cyan");
                cat = "cyan";
                if(cyan.isPaused())
                    cat = "paused";
                build(cyan,"Preparation");
                setTextViewTime();
                ib_g.setEnabled(false);
            }
        });
        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                yellow.start();
                Log.d(TAG, "ib_y: onClick: starting yellow");
                cat = "yellow";
                if(yellow.isPaused())
                    cat = "paused";
                build(yellow,"Traveling");
                setTextViewTime();
                ib_g.setEnabled(false);
            }
        });
        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animScale);
                pink.start();
                Log.d(TAG, "ib_p: onClick: starting pink");
                cat = "pink";
                if(pink.isPaused())
                    cat = "paused";
                build(pink,"Relaxing");
                setTextViewTime();
                ib_g.setEnabled(false);

            }
        });



    }

    /**
     * -Display a dialog box that ask the users if they want to end the day
     * or ask the user if they want to reflect on the day.
     *
     * @param msg   the message to be displayed
     * @param value to decide if we are ending the day or reflecting, and then
     *              pass value to setEnableBtns
     */
    public void showAlert(String msg, final Boolean value) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //Display msg
        alert.setMessage(msg)

                .setNegativeButton("Not yet!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Log.d(TAG, "showAlert: negativeButton: dismiss");
                    }
                })
                .setPositiveButton("I'm ready!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        Log.d(TAG, "showAlert: positiveButton: create new dialog");
                        if (value) { //Ending day

                            AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                            //Display msg
                            alert2.setMessage("Do you want to save the day?")
                                    .setPositiveButton("Yes, Save Data", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Log.d(TAG, "showAlert: save day");
                                                    dialogInterface.dismiss();
                                                    saveDay();
                                                    reset();
                                                    Toast.makeText(MainActivity.this,
                                                            "Day is saved!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                    .setNegativeButton("No don't save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Log.d(TAG, "showAlert: don't save day");
                                            reset();
                                            Toast.makeText(MainActivity.this,
                                                    "Day has ended!", Toast.LENGTH_SHORT).show();
                                        }
                                    })

                            .create();
                            alert2.show();


                            final SharedPreferences preferences = MainActivity.this.getSharedPreferences("timerValues", 0); // 0 - for private mode
                            final SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();
                            cat="";
                            //Activate all buttons and reset

                            setEnableBtns(value);
                            Log.d(TAG, "showAlert: ending day: clear editor and notifications");

                        }
                        else {//Reflecting on day
                            Toast.makeText(MainActivity.this,
                                    "Reflect on the day!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "showAlert: reflecting: clear notification");
                            //Deactivate all buttons and stops all timers
                            setEnableBtns(value);
                            myTimer.stopAll();
                          //  myTimer.getLastTimer().start();
                            cat = "reflect";
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();

                            setTextViewTime();

                            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String currentDateAndTime = sdf.format(new Date());
                            String split[] = currentDateAndTime.split("_");

                            String out = textViewTime.getText().toString();
                            for(int z = 0; z<split[1].length(); z+=2){

                                out+=split[1].substring(z,z+2);
                                if(z<3)
                                    out += " : ";
                            }

                            textViewTime.setText(out);*/
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
        textViewTime.setText("");
        Log.d(TAG, "reset: reset myTimers");

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
        Log.d(TAG, "setEnableBtns: enable/disable buttons");

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

        long result = data.getLongExtra("sub", 0);
        boolean affect = data.getBooleanExtra("affect",true);
        Log.d(TAG, "onActivityResult: retrieve data from adjustTimers");
        //int x;
        if (resultCode == Activity.RESULT_OK) {
            if(myTimer.isWakeUpEnabled())
                setTextViewTime();
            switch (data.getStringExtra("cat")) {
                case "Leisure":
                    Log.d(TAG, "onActivityResult: adjust orange");
                    if (orange.getTextValueMilli() - result > 0)
                        orange.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    if(myTimer.getLastTimer() == orange)
                        build(orange,"Leisure");
                    break;

                case "Exercise":
                    Log.d(TAG, "onActivityResult: adjust red");
                    if (red.getTextValueMilli() - result > 0)
                        red.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();
                    if(myTimer.getLastTimer() == red)
                        build(red,"Exercise");
                    break;

                case "Education":
                    Log.d(TAG, "onActivityResult: adjust purple");
                    if (purple.getTextValueMilli() - result > 0)
                        purple.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == purple)
                        build(purple,"Education");
                    break;

                case "Work":
                    Log.d(TAG, "onActivityResult: adjust blue");
                    if (blue.getTextValueMilli() - result > 0)
                        blue.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == blue)
                        build(blue,"Work");
                    break;

                case "Other":
                    Log.d(TAG, "onActivityResult: adjust grey");
                    if (grey.getTextValueMilli() - result > 0)
                        grey.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == grey)
                        build(grey,"Other");
                    break;

                case "Preparation":
                    Log.d(TAG, "onActivityResult: adjust cyan");
                    if (cyan.getTextValueMilli() - result > 0)
                        cyan.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == cyan)
                        build(cyan,"Preparation");
                    break;

                case "Traveling":
                    Log.d(TAG, "onActivityResult: adjust yellow");
                    if (yellow.getTextValueMilli() - result > 0)
                        yellow.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == yellow)
                        build(yellow,"Traveling");
                    break;

                case "Relaxing":
                    Log.d(TAG, "onActivityResult: adjust pink");
                    if (pink.getTextValueMilli() - result > 0)
                        pink.add(result,affect);
                    else
                        Toast.makeText(MainActivity.this,"Can not remove more time than you have",Toast.LENGTH_LONG).show();

                    if(myTimer.getLastTimer() == pink)
                        build(pink,"Relaxing");
                    break;

                case "Wake Up!":
                    Log.d(TAG, "onActivityResult: adjust green");
                    if (myTimer.getWakeUpText() - result < 0) {
                        Toast.makeText(MainActivity.this, "Can not remove more time than you have", Toast.LENGTH_LONG).show();
                        break;
                    }
                    if(!myTimer.isWakeUpEnabled())
                        myTimer.wakeUp(result + myTimer.getWakeUpBase());
                    else
                        myTimer.wakeUp(result + SystemClock.elapsedRealtime());
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


        editor.putInt("Day", (day + 1));
        Log.i("map","Day is "+day);


        //currentDateandTime is going to hold our key
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());
        if(day>=30) {
            TreeMap<String, ?> keys = new TreeMap<String, Object>(pref.getAll());
            String firstEntry = keys.firstKey();
            editor.remove(firstEntry);
        }
        //add to savedPreferences
        //key looks like "20180601_123430"
        //value looks like "1000,2000,3000,4000,5000,6000,7000,8000"
        editor.putString(currentDateAndTime, str);
        editor.apply();
        Log.d(TAG, "saveDay: saving data");
    }

    private void setTextViewTime(){

        if(!ib_g.isEnabled() && reflect.isEnabled())
            return ;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());
        String split[] = currentDateAndTime.split("_");

        String out="";
        for(int i = 0; i<split[1].length(); i+=2){

            out+=split[1].substring(i,i+2);
            if(i<3)
                out += " : ";
        }
        out = convertNormalTime(out);
        if(textViewTime.getText().toString().equals(""))
            out += "  -  ";
        textViewTime.append(out);

    }

    private String convertNormalTime(String time){
        String split[] = time.split(" : ");
        String end = " AM", out="";
        if(Integer.parseInt(split[0]) > 11)
            end = " PM";
        Log.d(TAG, "convertNormalTime: "+Integer.parseInt(split[0]) % 12+":" + split[1] + ":" + split[2] + end);
        if(Integer.parseInt(split[0]) % 12 != 0)
            out+=Integer.parseInt(split[0]) % 12+":" + split[1] + ":" + split[2] + end;
        else
            out+="12:" + split[1] + ":" + split[2] + end;
        return out;

    }
    private void build(myTimer timer,String name) {


        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.remote_view_test);
        remoteViews.setChronometer(R.id.remoteChrono,timer.getTimer().getBase(),timer.getTimer().getFormat(),true);
        remoteViews.setTextViewText(R.id.remoteText,name + " \t");
        //remoteViews.setTextColor(R.id.remoteText,);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channelId")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Title")
                .setContent(remoteViews)
             //   .setContentText(orange.getTimer().getText().toString())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(false)
               // .setCustomContentView(remoteViews);
//                .setCustomBigContentView(remoteViews)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent =  PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(timer.isPaused()) {
            manager.cancelAll();
            Log.d(TAG, "build: close noti");
        }
        else {
            manager.notify(0, builder.build());
            Log.d(TAG, "build: build noti");
        }
}

    public void saveTimerValue(SharedPreferences.Editor editor){

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
        if(cat.equals("reflect"))
            editor.putLong("green",myTimer.getWakeUpText());
        else
            editor.putLong("green",myTimer.getWakeUpBase());
    }

    public void saveClose(){

        final SharedPreferences preferences = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = preferences.edit();

        saveTimerValue(editor);

        editor.putString("running",cat);
        editor.putString("textTime",textViewTime.getText().toString());

        editor.apply();
        Log.d(TAG, "saveClose: applied");

    }


    @Override
    public void onStop(){
        Log.d(TAG,"onStop: call saveClose()");
        saveClose();
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: moved to back");
        moveTaskToBack(true);
    }



}