package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    Button endDay,reflect,adjust;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //set myTimers, which requier a chronometer and a boolean value(true only for wakeUp)
        green = new myTimer(cm_g,true);
        orange = new myTimer(cm_o,false);//lp_o);
        red = new myTimer(cm_r,false);//lp_r);
        purple = new myTimer(cm_p,false);//lp_p);
        blue = new myTimer(cm_b,false);//lp_b);
        grey = new myTimer(cm_gr,false);//lp_gr);
        cyan = new myTimer(cm_c,false);//lp_c);
        yellow = new myTimer(cm_y,false);//0);
        pink = new myTimer(cm_pk,false);//0);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);

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
       // animScale.setAnimationListener();

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
}