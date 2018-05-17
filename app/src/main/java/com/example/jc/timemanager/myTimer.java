package com.example.jc.timemanager;

import android.os.SystemClock;
import android.widget.Chronometer;

/**
 * Created by JC on 4/29/2018.
 * This class will handle the chronometers on the activity
 *
 */

public class myTimer {
    /**
     * -timer is the chronometer on the activity
     * -lastPause holds the value of the last time the user paused / switched to another timer
     * -lastTimer holds the last myTimer the user started
     * -wakeUp belongs to the class and will run independently from the other
     * timers on the activity
     */
    private Chronometer timer;
    private long lastPause;
    private static myTimer lastTimer;
    private static Chronometer wakeUp;

    /**
     * myTimer empty constructor
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

    public  myTimer(Chronometer c, long l){
        timer = c;
        lastPause = l;
    }

    /**
     * start the myTimer
     */
    public void start(){
        if(wakeUp.isEnabled()) {//if wakeUp has not been started call wakeUp()
            wakeUp();

        }
        if(lastTimer!=null) {//if we have started another timer
            lastTimer.setLastPause(SystemClock.elapsedRealtime());//set lastPause
            lastTimer.getTimer().stop();//pause the last timer pressed
        }
        if (lastPause != 0)//Check to see if we have paused this timer before
            timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - lastPause);//Yes, set the base to our current time plus Realtime minus the time from last pause
        else
            timer.setBase(SystemClock.elapsedRealtime());//start counting up from 00:00
        timer.start();//start timer
        lastTimer = this;//set the last timer to this

    }

    /**
     * Adjust time of a myTimer
     * @param l the value(result from adjustTimers) to add to the myTimer
     */
    public void add(long l){

    //if the timer we are adjusting is currently running
    if(lastTimer == this){
        timer.setBase(timer.getBase()+ l);//add the result

    }
    else if(lastPause == 0) {//if we have never started the timer we are adjusting
        timer.setBase(SystemClock.elapsedRealtime()+ l);//add the result and count up from realtime
        lastPause = SystemClock.elapsedRealtime();//set the last time we paused
    }
    else{//We have started this timer before and it is not running
        timer.setBase(timer.getBase()+SystemClock.elapsedRealtime()-lastPause + l);
        lastPause = SystemClock.elapsedRealtime();
    }
    if(lastTimer!=null)//if we have activated wakeUp before
        wakeUp.setBase(wakeUp.getBase()+l);// add the result to wakeUp
    else {
        wakeUp();//start wakeUp
        wakeUp.setBase(wakeUp.getBase() + l);//add the result
    }

    }

    /**
     * stop all of the timers
     */
    public static void stopAll(){
        if(lastTimer!=null) {//if we have started a timer before
            lastTimer.setLastPause(SystemClock.elapsedRealtime());
            lastTimer.getTimer().stop();
        }

        wakeUp.stop();// stop wakeUp timer
        wakeUp.setEnabled(true);


    }

    /**
     * start wakeUp timer and then disable it
     */
    public static void wakeUp(){

            wakeUp.setBase(SystemClock.elapsedRealtime());
            wakeUp.start();
            wakeUp.setEnabled(false);

    }

    /**
     * reset all timers including wakeUP
     */
    public void reset(){
        lastPause=0;
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        lastTimer=null;wakeUp.setEnabled(true);
        wakeUp.setBase(SystemClock.elapsedRealtime());
        wakeUp.stop();

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
    public void setLastPause(long l){
        lastPause = l;
    }

}
