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
     * myTimer constructor for wakeup timer
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

    /**
     * myTimer constructor used in main for the rest of myTimers.
     * @param c is a chronometer
     * @param val is a long that hold value to determine the base for myTimer
     * @param running is a bool, hold the value of if timer was running before the activity
     *                was destroyed .
     */
    public myTimer(Chronometer c, long val,boolean running){
        timer = c;
        lastPause = 0;
        Log.d(TAG, "myTimer: create myTimer");
        if(val!=0) {
            if(!running) {
                timer.setBase(timer.getBase() - val);
                lastPause = SystemClock.elapsedRealtime();
                Log.d(TAG, "myTimer: timer was not running");
            }
            else {
                timer.setBase(val);
                lastPause = SystemClock.elapsedRealtime();
                Log.d(TAG, "myTimer: timer was running");
            }
        }
    }

    /**
     * checks if timer is paused
     */
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
     * @param b are we affecting the wakeup timer
     */
    public void add(long l, boolean b){
    if(lastTimer == this ) {
        if (!paused) {
            timer.setBase(timer.getBase() + l);//add the result
            Log.d(TAG, "add: timer is currently running");
        }
        else{
            Log.d(TAG, "add: timer has been paused");
            timer.setBase(timer.getBase()+SystemClock.elapsedRealtime()-lastPause + l);
            lastPause = SystemClock.elapsedRealtime();
        }
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
    if(b) {
        if (wakeUp.isEnabled()) {//if we have activated wakeUp before
            wakeUp(SystemClock.elapsedRealtime() + l);// add the result to wakeUp
            Log.d(TAG, "add: adjust wakeUp");
        } else {
            wakeUp(wakeUp.getBase() + l);
            Log.d(TAG, "add: adjust wakeUp");
        }
    }

    }


    /**
     * start wakeUp timer and then disable it
     */
    public static void wakeUp(long l){
        Log.d(TAG, "wakeUp: ");
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
            //lastTimer.start();
            lastTimer.setLastPause(SystemClock.elapsedRealtime());
            lastTimer.getTimer().stop();
        }

        //wakeUp.stop();// stop wakeUp timer
        //wakeUp.setEnabled(true);


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
        Log.d(TAG, "getTextValueMilli: ");
        String a[];
        a = timer.getText().toString().split(":");
        if(a.length == 2)
            return  ( (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000));
        else
            return ( (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000));

    }
    public static int getWakeUpTextInt(){
        Log.d(TAG, "getWakeUpText: ");
        String a[];
        a = wakeUp.getText().toString().split(":");
        if(a.length == 2)
            return  ( (Integer.parseInt(a[0])*60*1000) + (Integer.parseInt(a[1])*1000));
        else
            return ( (Integer.parseInt(a[0])*60*60*1000) + (Integer.parseInt(a[1])*60*1000) + (Integer.parseInt(a[2])*1000));

    }
    public static String getWakeUpTextString(){
        return wakeUp.getText().toString();
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
    public static void setWakeUpEnabled(){
        wakeUp.setEnabled(false);
        wakeUp.stop();

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
    public static boolean isWakeUpEnabled(){return wakeUp.isEnabled();}

}
