package com.example.jc.timemanager;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

/**
 * Created by JC on 4/29/2018.
 * This class will handle the chronometers on the activity
 *
 */

public class myTimer {
    /**
     * -timer, is the chronometer on the activity
     * -lastPause, holds the value of the last time the user paused
     *      - or switched to another timer
     * -lastTimer, holds the last myTimer the user started
     * -wakeUp, belongs to the class and will run independently from the other
     *      - timers on the activity
     */
    private Chronometer timer;
    private long lastPause;
    private static myTimer lastTimer;
    private static Chronometer wakeUp;
    private static String TAG = "myTimer";
    private boolean paused = false;

    /**
     * myTimer default constructor
     */
    public myTimer(){
        timer=null;
        lastPause=0;

    }

    /**
     * myTimer constructor
     * @param c a chronometer
     * @param isWakeUp is this myTimer the wakeUp timer
     */
    public myTimer(Chronometer c,boolean isWakeUp){
        if(isWakeUp) {
            wakeUp = c;
            lastPause = SystemClock.elapsedRealtime();
        }
        else{
            timer = c;
            lastPause = 0;
        }
    }
    public myTimer(Chronometer c, long l,boolean b){
        timer = c;
        lastPause = 0;
        if(l!=0) {
            if(!b) {
                timer.setBase(timer.getBase() - l);
                lastPause = SystemClock.elapsedRealtime();
            }
            else {
                timer.setBase(l);
                lastPause = SystemClock.elapsedRealtime();
            }
        }
    }

    private void pausingHandler(){
        if(lastTimer!=null) {
            Log.d(TAG, "pausingHandler: we have previously started a timer");
            if(!lastTimer.isPaused()) {
                lastTimer.setLastPause(SystemClock.elapsedRealtime());
                lastTimer.getTimer().stop();
                Log.d(TAG, "pausingHandler: lastTimer saved and stopped");
            }
            if(!paused && lastTimer == this) {
                paused = true;
                Log.d(TAG, "pausingHandler: pausing current timer");
            }
            else {
                paused = false;
                Log.d(TAG, "pausingHandler: unpausing current timer");
            }
        }
        lastTimer = this;
    }


    /**
     * start the myTimer
     */
    public void start(){
        Log.d(TAG, "start: entered start");
        if(wakeUp.isEnabled()) {//if wakeUp has not been started call wakeUp()
            wakeUp(SystemClock.elapsedRealtime());
            Log.d(TAG, "start: wakeUp Enabled");
        }

        pausingHandler();

        if(!paused) {
            if (lastPause != 0) {
                Log.d(TAG, "start: continuing timer");

                timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - lastPause);//formula for setting base to display appropriate time
            } else {
                Log.d(TAG, "start: starting timer");
                timer.setBase(SystemClock.elapsedRealtime());//start counting up from 00:00
            }
            timer.start();
        }

    }

    /**
     * Adjust time of a myTimer
     * @param l the value(result from adjustTimers) to add to the myTimer
     */
    public void add(long l){
    if(lastTimer == this){
        timer.setBase(timer.getBase()+ l);//add the result
        Log.d(TAG, "add: timer is currently running");
    }
    else if(lastPause == 0) {
        Log.d(TAG, "add: timer has not been started");
        timer.setBase(SystemClock.elapsedRealtime()+ l);
        lastPause = SystemClock.elapsedRealtime();
        if(lastTimer == null) {
            timer.start();
            lastTimer = this;
        }
    }
    else{
        Log.d(TAG, "add: timer has been started");
        timer.setBase(timer.getBase()+SystemClock.elapsedRealtime()-lastPause + l);
        lastPause = SystemClock.elapsedRealtime();
    }
    if(wakeUp.isEnabled()) {//if we have activated wakeUp before
        wakeUp(SystemClock.elapsedRealtime() + l);// add the result to wakeUp
        Log.d(TAG, "add: adjust wakeUp");
    }
    else {
        wakeUp(wakeUp.getBase() + l);
        Log.d(TAG, "add: adjust wakeUp");
    }

    }


    /**
     * start wakeUp timer and then disable it
     */
    public static void wakeUp(long l){

        wakeUp.setBase(l);
        wakeUp.start();
        wakeUp.setEnabled(false);

    }


    /**
     * stop all of the timers
     */
    public static void stopAll(){
        Log.d(TAG, "stopAll: ");
        if(lastTimer!=null) {//if we have started a timer before
            lastTimer.setLastPause(SystemClock.elapsedRealtime());
            lastTimer.getTimer().stop();
        }

        wakeUp.stop();// stop wakeUp timer
        wakeUp.setEnabled(true);


    }


    /**
     * reset all timers including wakeUP
     */
    public void reset(){
        Log.d(TAG, "reset: ");
        lastPause=0;
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        lastTimer=null;wakeUp.setEnabled(true);
        wakeUp.setBase(SystemClock.elapsedRealtime());
        wakeUp.stop();

    }

    public int getTextValueMilli(){
        String a[];
        a = timer.getText().toString().split(":");
        if(a.length == 2)
            return  ( (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000));
        else
            return ( (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000));

    }



    public  void setDefaults(String key, /*String value,*/ Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
       // editor.putString(key, value);
        editor.putLong(key,timer.getBase());
        editor.apply();
    }

    public void getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        timer.setBase(preferences.getLong(key, timer.getBase()));
        timer.stop();

        ;

    }


    // Getters and Setters
    public static myTimer getLastTimer(){
        return lastTimer;
    }
    public static void setLastTimer(myTimer t){
        lastTimer = t;
    }
    public Chronometer getTimer(){
        return timer;
    }
    public void setTimer(Chronometer c){
        timer = c;
    }
    public long getLastPause(){
        return lastPause;
    }
    public boolean isPaused(){
        return paused;
    }
    public void setLastPause(long l){
        lastPause = l;
    }
    public void setPaused(boolean b){
        paused = b;
    }
    public static long getWakeUpBase(){return  wakeUp.getBase();}
    public void getPref(){

    }


    private void build(Context context) {
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

            }
        });
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Title")
                .setContentText(this.getTimer().getText().toString())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent contentIntent =  PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = 0;
        //builder.setProgress(0, 0, true);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}
