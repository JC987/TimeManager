package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
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
        final SharedPreferences timerPref = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        // cat holds the last category that was running when app was previously killed
        cat = timerPref.getString("running","");

        //assign myTimers
        green = new myTimer(cm_g,true);//wake up timer
        //myTimer mytimer = new myTimer(Chronometer,previous timer value, was this timer running last)
        orange = new myTimer(cm_o,timerPref.getLong("orange",0),cat.equals("orange"));
        red = new myTimer(cm_r,timerPref.getLong("red",0),cat.equals("red"));
        purple = new myTimer(cm_p,timerPref.getLong("purple",0),cat.equals("purple"));
        blue = new myTimer(cm_b,timerPref.getLong("blue",0),cat.equals("blue"));
        grey = new myTimer(cm_gr,timerPref.getLong("grey",0),cat.equals("grey"));
        cyan = new myTimer(cm_c,timerPref.getLong("cyan",0),cat.equals("cyan"));
        yellow = new myTimer(cm_y,timerPref.getLong("yellow",0),cat.equals("yellow"));
        pink = new myTimer(cm_pk,timerPref.getLong("pink",0),cat.equals("pink"));
        textViewTime.setText(timerPref.getString("textTime",""));


        Log.d(TAG, "onCreate: Instantiating myTimers");

        switch (cat) {
            /*
                if this timer was running last
                    start timer
                    adjust the wake up timer
                    disable wake up button
             */
            case "orange":
                orange.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                Log.d(TAG, "onCreate: resuming, start orange");
                break;

            case "red":
                Log.d(TAG, "onCreate: resuming, start red");
                red.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "purple":
                Log.d(TAG, "onCreate: resuming, start purple");
                purple.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "blue":
                blue.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                Log.d(TAG, "onCreate: resuming, start blue");
                ib_g.setEnabled(false);
                break;

            case "grey":
                Log.d(TAG, "onCreate: resuming, start grey");
                grey.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "cyan":
                Log.d(TAG, "onCreate: resuming, start cyan");
                cyan.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "yellow":
                Log.d(TAG, "onCreate: resuming, start yellow");
                yellow.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "pink":
                Log.d(TAG, "onCreate: resuming, start pink");
                pink.start();
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "green":
                Log.d(TAG, "onCreate: resuming, start green");
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;
            case "reflect":
                Log.d(TAG, "onCreate: case reflect");
                myTimer.wakeUp(myTimer.getWakeUpBase() - timerPref.getLong("green",0));
                myTimer.stopAll();
                setEnableBtns(false);
                break;
            case "paused":
                Log.d(TAG, "onCreate: resuming, timers paused");
                myTimer.wakeUp(timerPref.getLong("green",0));
                ib_g.setEnabled(false);
                break;

            default:
                Log.d(TAG, "onCreate: default");
        }

        //change activities animation
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);


        /**
         * When adjust pressed start adjustTimer activity for a result
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

        /**
         * When endDay pressed calls a function that shows a dialog box
         */
        endDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                v.startAnimation(animScale);
                Log.d(TAG, "endDay: onClick: ");
                showAlert("Do you want to end the day",true);

            }
        });

        /**
         * When reflect pressed calls a function that shows a dialog box
         */
        reflect.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                Log.d(TAG, "reflect: onClick: ");
                if(!myTimer.isWakeUpEnabled())
                    showAlert("Do you want to reflect on the day",false);


            }
        }));

        /**
         * When stat pressed change activity to the stats activity
         */
        stat.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "stat: onClick: goto statsTabbed");
                Intent i = new Intent(MainActivity.this,statsTabbed.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }));


        /*ImageButton pressed events
            On image btn pressed
                start wake up timer and disable wakeup btn
                set the textView
                set this timer as last started

        */

        ib_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                myTimer.wakeUp(SystemClock.elapsedRealtime());//start the wakeUp timer
                setTextViewTime();
                ib_g.setEnabled(false);
                cat = "green";
                Log.d(TAG, "ib_g: onClick: starting green");

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
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textViewTime.getText().toString().equals(""))
                    showTextTimePicker();
            }
        });




      /*  Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

*/

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
                            //Display second msg
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

                          //  myTimer.getLastTimer().start();
                            cat = "reflect";
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancelAll();

                            setTextViewTime();
                            myTimer.stopAll();
                            myTimer.setWakeUpEnabled();
                        }
                    }
                })

                .create();
        alert.show();

    }

    /**
     * creates a dialog that has a time picker, the value from the time picker gets set
     * to the text view
     */

    public void  showTextTimePicker(){
        final AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.time_picker_dialog, null);
        d.setTitle("Set start day start value");
        d.setView(dialogView);
        final TimePicker timePicker = dialogView.findViewById(R.id.timePicker);

        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String tmp = timePicker.getCurrentMinute().toString();
                if(Integer.parseInt(timePicker.getCurrentMinute().toString()) < 10)
                {
                     tmp = "0"+timePicker.getCurrentMinute().toString();
                }

                String out2 = timePicker.getCurrentHour()+" : "+tmp+ " : " + "00";
                out2 = convertNormalTime(out2);
                textViewTime.setText(out2+" - ");
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
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
        adjust.setEnabled(val);
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

        // result is the value that will be removed from a timer
        long result = data.getLongExtra("sub", 0);
        //true if we affecting wake up timer as well
        boolean affect = data.getBooleanExtra("affect",true);
        Log.d(TAG, "onActivityResult: retrieve data from adjustTimers");

        if (resultCode == Activity.RESULT_OK) {
            if(myTimer.isWakeUpEnabled() && reflect.isEnabled())
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
                    if (myTimer.getWakeUpTextInt() - result < 0) {
                        Toast.makeText(MainActivity.this, "Can not remove more time than you have", Toast.LENGTH_LONG).show();
                        break;
                    }

                    if(!myTimer.isWakeUpEnabled() ) {
                        myTimer.wakeUp(result + myTimer.getWakeUpBase());
                    }
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

        float o,r,p,b,gr,c,y,pk,sum,g;

        //gets the value from the text of each chronometer
        o = (float) orange.getTextValueMilli();
        r = (float) red.getTextValueMilli();
        p =(float) purple.getTextValueMilli();
        b = (float) blue.getTextValueMilli();
        gr =(float) grey.getTextValueMilli();
        c = (float) cyan.getTextValueMilli();
        y = (float) yellow.getTextValueMilli();
        pk = (float) pink.getTextValueMilli();
        g = (float) myTimer.getWakeUpTextInt();

        //use the sum of all the timers instead of text from the wakeUp timer.
        sum = o+r+p+b+gr+c+y+pk;

        if(g>sum)
            sum = g;

        //day is a counter for my current day which can not be more than 30
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

    /**
     * set the text view value
     */
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

    /**
     * Converts a string that represent time in military time and convert it to standard format
     * @param time is a string that represents a time in military i.e. "20:30:15"
     * @return a string that reprsents time in normal format i.e. "10:30:15PM"
     */
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

    /**
     * Create a custom notification that display which category is currently running and it's
     * timer value
     * @param timer myTimer that is currently running
     * @param name name of the category
     */
    private void build(myTimer timer,String name) {
        //set up remote view
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.remote_view_test);
        remoteViews.setChronometer(R.id.remoteChrono,timer.getTimer().getBase(),timer.getTimer().getFormat(),true);
        remoteViews.setTextViewText(R.id.remoteText,name + " \t");

        //build noti
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channelId")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Title")
                .setContent(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent =  PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        //this is the intent that is supposed to be called when the
        //button on the notification is clicked
        Intent switchIntent = new Intent(this, switchButtonListener.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
                switchIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.remoteButton,
                pendingSwitchIntent);

        if(timer.isPaused()) {
            manager.cancelAll();
            Log.d(TAG, "build: close noti");
        }
        else {
            manager.notify(0, builder.build());
            Log.d(TAG, "build: build noti");
        }
}
    public static class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Here", "I am here");

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
        }
    }

    /**
     * save timers value if timer is running save its base
     else save timer text value.
     * @param editor goes to shared preferences "timerValues"
     */
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
            editor.putLong("green",myTimer.getWakeUpTextInt());
        else
            editor.putLong("green",myTimer.getWakeUpBase());
    }

    /**
     * call saveTimerValue and save which category is currently running and apply changes
     */
    public void saveClose(){
        final SharedPreferences timerPref = this.getSharedPreferences("timerValues", 0); // 0 - for private mode
        final SharedPreferences.Editor timerEditor = timerPref.edit();

        saveTimerValue(timerEditor);

        timerEditor.putString("running",cat);
        timerEditor.putString("textTime",textViewTime.getText().toString());

        timerEditor.apply();
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