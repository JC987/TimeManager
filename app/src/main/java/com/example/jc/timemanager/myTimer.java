package com.example.jc.timemanager;

import android.os.SystemClock;
import android.widget.Chronometer;

/**
 * Created by JC on 4/29/2018.
 */

public class myTimer {
    private Chronometer timer;
    private long lastPause;
    private static myTimer lastTimer;
    private static Chronometer wakeUp;

    public myTimer(){
        timer=null;
        lastPause=0;

    }
    public myTimer(Chronometer c){
        wakeUp = c;
        lastPause = SystemClock.elapsedRealtime();
    }

    public  myTimer(Chronometer c, long l){
        timer = c;
        lastPause = l;
    }

    public void start(){
        if(wakeUp.isEnabled()) {
            wakeUp();

        }
        if(lastTimer!=null) {
            lastTimer.setLastPause(SystemClock.elapsedRealtime());
            lastTimer.getTimer().stop();
        }
        if (lastPause != 0)//Check to see if we have paused this timer before
            timer.setBase(timer.getBase() + SystemClock.elapsedRealtime() - lastPause);//Yes, set the base to our current time plus Realtime minus the time from last pause
        else
            timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        lastTimer = this;

    }
    public void add(long l){


    if(lastTimer == this){
        timer.setBase(timer.getBase()+ l);

    }
    else if(lastPause == 0) {
        timer.setBase(SystemClock.elapsedRealtime()+ l);
        lastPause = SystemClock.elapsedRealtime();
    }
    else{
        timer.setBase(timer.getBase()+SystemClock.elapsedRealtime()-lastPause + l);
    }
    if(lastTimer!=null)
        wakeUp.setBase(wakeUp.getBase()+l);
    else {
        wakeUp();
        wakeUp.setBase(wakeUp.getBase() + l);
    }

    }

    public static void stopAll(){
        if(lastTimer!=null) {
            lastTimer.setLastPause(SystemClock.elapsedRealtime());
            lastTimer.getTimer().stop();
        }

        wakeUp.stop();
        wakeUp.setEnabled(true);


    }
    public static void wakeUp(){

            wakeUp.setBase(SystemClock.elapsedRealtime());
            wakeUp.start();
            wakeUp.setEnabled(false);

    }
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
