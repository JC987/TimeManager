package com.example.jc.timemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public void showAlert(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to end the day?")

                .setNegativeButton("Not yet!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .setPositiveButton("All Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();

                       // showDayStats();
                        reset();
                    }
                })
                .create();
        alert.show();
    }



    Button button ,adjust;
    Button reset;
    TextView text;
    ConstraintLayout cl;
    Chronometer cm_g,cm_o,cm_r,cm_p,cm_b,cm_c,cm_y,cm_gr,cm_pk;
    ImageButton ib_g,ib_o,ib_r,ib_p,ib_b,ib_c,ib_y,ib_gr,ib_pk;
    TextView tv;
    long lastPause= 0,lastPause_o = 0,lastPause_r = 0,lastPause_p = 0,lastPause_pk = 0,lastPause_b= 0,lastPause_c= 0,lastPause_y= 0,lastPause_gr= 0;
    String lastBtn = "";

    public void reset(){
        lastPause= 0;lastPause_o = 0;lastPause_r = 0;lastPause_p = 0;lastPause_pk = 0;lastPause_b= 0;lastPause_c= 0;lastPause_y= 0;lastPause_gr= 0;
        cm_g.stop();cm_b.stop();cm_r.stop();cm_p.stop();cm_pk.stop();cm_o.stop();cm_c.stop();cm_y.stop();cm_gr.stop();
        cm_g.setBase(SystemClock.elapsedRealtime());cm_o.setBase(SystemClock.elapsedRealtime());cm_pk.setBase(SystemClock.elapsedRealtime());cm_p.setBase(SystemClock.elapsedRealtime());cm_r.setBase(SystemClock.elapsedRealtime());cm_b.setBase(SystemClock.elapsedRealtime());
        cm_c.setBase(SystemClock.elapsedRealtime());cm_y.setBase(SystemClock.elapsedRealtime());cm_gr.setBase(SystemClock.elapsedRealtime());
        lastBtn="";
        ib_g.setEnabled(true);
        ib_b.setEnabled(true);
        ib_c.setEnabled(true);
        ib_gr.setEnabled(true);
        ib_o.setEnabled(true);
        ib_r.setEnabled(true);
        ib_p.setEnabled(true);
        ib_pk.setEnabled(true);
        ib_y.setEnabled(true);
    }
    public void pause(){
        if(lastBtn.equals("p")) {//If the last btn pressed was the purple one
            lastPause_p = SystemClock.elapsedRealtime();//Set the last time we paused to Realtime
            cm_p.stop();//Stop the purple timer
        }

        if(lastBtn.equals("r")) {
            lastPause_r = SystemClock.elapsedRealtime();
            cm_r.stop();
            //}
        }

        if(lastBtn.equals("b")){
            lastPause_b=SystemClock.elapsedRealtime();
            cm_b.stop();
        }
        if(lastBtn.equals("c")){
            lastPause_c=SystemClock.elapsedRealtime();
            cm_c.stop();
        }
        if(lastBtn.equals("y")){
            lastPause_y=SystemClock.elapsedRealtime();
            cm_y.stop();
        }
        if(lastBtn.equals("gr")){
            lastPause_gr=SystemClock.elapsedRealtime();
            cm_gr.stop();
        }
        if(lastBtn.equals("o")){
            lastPause_o=SystemClock.elapsedRealtime();
            cm_o.stop();
        }
        if(lastBtn.equals("pk")){
            lastPause_pk=SystemClock.elapsedRealtime();
            cm_pk.stop();
        }
        cm_g.stop();
        ib_b.setEnabled(false);
        ib_c.setEnabled(false);
        ib_gr.setEnabled(false);
        ib_o.setEnabled(false);
        ib_r.setEnabled(false);
        ib_p.setEnabled(false);
        ib_y.setEnabled(false);
        //ib_c.setEnabled(false);
    }

    public void showDayStats() {
       /* AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //Read the value form the chronometer (such as 00:00) and split it at the :
        String[] rtmp = cm_r.getText().toString().split(":");
        String[] gtmp = cm_g.getText().toString().split(":");
        String[] otmp = cm_o.getText().toString().split(":");
        String[] ptmp = cm_p.getText().toString().split(":");
        String[] btmp = cm_b.getText().toString().split(":");
        String[] grtmp = cm_gr.getText().toString().split(":");
        String[] ctmp = cm_c.getText().toString().split(":");
        String[] ytmp = cm_y.getText().toString().split(":");
        /* g is for the green timer which is the wake up timer
            g will be the base we use to get the percent for the other values
         */

      /*  double g = Integer.parseInt(gtmp[0]) * 60//multiply by 60 to convert the minutes to second
                + Integer.parseInt(gtmp[1]) ;// add the seconds
        double r =Integer.parseInt(rtmp[0]) * 60
                + Integer.parseInt(rtmp[1]) ;
        double p =Integer.parseInt(ptmp[0]) * 60
                + Integer.parseInt(ptmp[1]) ;
        double o =Integer.parseInt(otmp[0]) * 60
                + Integer.parseInt(otmp[1]) ;
        double b =Integer.parseInt(btmp[0]) * 60
                + Integer.parseInt(btmp[1]) ;
        double gr =Integer.parseInt(grtmp[0]) * 60
                + Integer.parseInt(grtmp[1]) ;
        double c =Integer.parseInt(ctmp[0]) * 60
                + Integer.parseInt(ctmp[1]) ;
        double y =Integer.parseInt(ytmp[0]) * 60
                + Integer.parseInt(ytmp[1]) ;


        //Send a message to the user with the data and a ok button to exit
        alert.setMessage("Your day was "+ cm_g.getText()+ " long!"+
                "\n"+"You spent " + Math.ceil((o/g)*10000)/100  +"% of your day on Leisure"+
                "\n"+"You spent " + Math.ceil((r/g)*10000)/100  +"% of your day on Exercise"+
                "\n"+"You spent " + Math.ceil((p/g)*10000)/100  +"% of your day Education"+
                "\n"+"You spent " +Math.ceil((b/g)*10000)/100  +"% of your day on Work"+
                "\n"+"You spent " +Math.ceil((gr/g)*10000)/100  +"% of your day on Other" +
                "\n"+"You spent "+ Math.ceil((c/g)*10000)/100  +"% of your day on Preparation"+
                "\n"+"You spent "+Math.ceil((y/g)*10000)/100  +"% of your day on Traveling")

                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        reset();
                    }
                })
                .create();
        alert.show();*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text =  findViewById(R.id.textView);
        button =  findViewById(R.id.button);
        adjust = findViewById(R.id.adjust);
        reset = findViewById(R.id.btn_reset);
        Boolean etOpen=false;
        ib_g =  findViewById(R.id.imageButton);
        cm_g =  findViewById(R.id.chronometer2);

        ib_o =  findViewById(R.id.imgBtnOrange);
        cm_o =  findViewById(R.id.chronometer_orange);

        ib_r =  findViewById(R.id.imgBtnRed);
        cm_r =  findViewById(R.id.chronometer_red);

        ib_p =  findViewById(R.id.imgBtnPurple);
        cm_p =  findViewById(R.id.chronometer_purple);

        ib_b =  findViewById(R.id.imgBtnBlue);
        cm_b =  findViewById(R.id.chronometer_blue);

        ib_c =  findViewById(R.id.imgBtnC);
        cm_c =  findViewById(R.id.chronometer_cyan);

        ib_y =  findViewById(R.id.imgBtnYellow);
        cm_y =  findViewById(R.id.chronometer_yellow);

        ib_gr =  findViewById(R.id.imgBtnGrey);
        cm_gr =  findViewById(R.id.chronometer_grey);

        ib_pk =  findViewById(R.id.imgBtnPink);
        cm_pk =  findViewById(R.id.chronometer_pink);

        tv= findViewById(R.id.txtViewGrey);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setText("");
                 showAlert(v);
            }
        });
        adjust.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setText("");
                showAlert(v);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause();
            }
        });

        ib_g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cm_g.setBase(SystemClock.elapsedRealtime());
                cm_g.start();
                ib_g.setEnabled(false);


            }
        });
      /*  TimerImgBtn ex_o = new TimerImgBtn(ib_o,cm_o,lastPause_o,"o",false);
        TimerImgBtn ex_p = new TimerImgBtn(ib_p,cm_p,lastPause_p,"p",false);
        TimerImgBtn ex_r = new TimerImgBtn(ib_r,cm_r,lastPause_r,"r",false);
        ex_o.pauseAll(ex_p);
        ex_o.setOnClick();
        ex_p.pauseAll(ex_o);
        ex_p.setOnClick();
        ex_r.pauseAll(ex_o);
        ex_r.setOnClick();*/
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // etOpen = true;
                tv.setEnabled(true);
                if (v.getId() == tv.getId())
                {
                    tv.setCursorVisible(true);
                }
            }
        });
        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                tv.setCursorVisible(false);
                if (event != null && ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (event.getKeyCode() == KeyEvent.KEYCODE_BACK))) {

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(tv.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return false;
            }

        });


                ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("p")) {//If the last btn pressed was the purple one
                    lastPause_p = SystemClock.elapsedRealtime();//Set the last time we paused to Realtime
                    cm_p.stop();//Stop the purple timer
                }

                if(lastBtn.equals("r")) {
                    lastPause_r = SystemClock.elapsedRealtime();
                    cm_r.stop();
                    //}
                }

                if(lastBtn.equals("b")){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }

                if(!lastBtn.equals("o")) {
                    if (lastPause_o != 0)//Check to see if we have paused this timer before
                        cm_o.setBase(cm_o.getBase() + SystemClock.elapsedRealtime() - lastPause_o);//Yes, set the base to our current time plus Realtime minus the time from last pause
                    else
                        cm_o.setBase(SystemClock.elapsedRealtime());//No, Start counting up from Realtime
                    cm_o.start();//Start
                    lastBtn = "o";//The last timer started in now the orange one
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                }
                else {
                   ;/* lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                    lastBtn="";*/
                }



            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(lastBtn.equals("o")) {
                    lastPause_o = SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("r")) {
                    lastPause_r = SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn.equals("b")) {
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }

                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }
                if(!lastBtn.equals("p")) {
                    if (lastPause_p != 0)
                        cm_p.setBase(cm_p.getBase() + SystemClock.elapsedRealtime() - lastPause_p);
                    else
                        cm_p.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_p.start();
                    lastBtn = "p";
                }
                else {
                   ;/* lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                    lastBtn="";*/
                }

            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("o")){
                    lastPause_o = SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("p")) {
                    lastPause_p = SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn.equals("b")) {
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }

                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }
                if(!lastBtn.equals("r")) {
                    if (lastPause_r != 0)
                        cm_r.setBase(cm_r.getBase() + SystemClock.elapsedRealtime() - lastPause_r);
                    else
                        cm_r.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_r.start();
                    lastBtn = "r";
                }
                else {
                   ;/* lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                    lastBtn="";*/
                }
            }
        });

        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("o")) {
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("r")) {
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn.equals("p")) {
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }
                if(!lastBtn.equals("b")) {
                    if (lastPause_b != 0)
                        cm_b.setBase(cm_b.getBase() + SystemClock.elapsedRealtime() - lastPause_b);
                    else
                        cm_b.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_b.start();
                    lastBtn = "b";
                }
                else {
                  ;/*  lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                    lastBtn="";*/
                }
            }
        });


        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("o")) {
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("r")) {
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn.equals("p")) {
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn.equals("b")){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }

                if(!lastBtn.equals("c")) {
                    if (lastPause_c != 0)
                        cm_c.setBase(cm_c.getBase() + SystemClock.elapsedRealtime() - lastPause_c);
                    else
                        cm_c.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_c.start();
                    lastBtn = "c";
                }
                else {
                    ;/*lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                    lastBtn="";*/
                }
            }
        });


        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("o")) {
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("r")) {
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn.equals("p")) {
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("b")){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn.equals("gr")){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }

                if(!lastBtn.equals("y")) {
                    if (lastPause_y != 0)
                        cm_y.setBase(cm_y.getBase() + SystemClock.elapsedRealtime() - lastPause_y);
                    else
                        cm_y.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_y.start();
                    lastBtn = "y";
                }
                else {
                    ;/*lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                    lastBtn="";*/
                }
            }
        });


        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn.equals("o")) {
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn.equals("r")) {
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn.equals("p")) {
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn.equals("c")){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn.equals("y")){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn.equals("b")){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn=="pk"){
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                }

                if(!lastBtn.equals("gr")) {
                    if (lastPause_gr != 0)
                        cm_gr.setBase(cm_gr.getBase() + SystemClock.elapsedRealtime() - lastPause_gr);
                    else
                        cm_gr.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_gr.start();
                    lastBtn = "gr";
                }
                else {
                   ;/* lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                    lastBtn="";*/
                }
            }
        });

        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn=="o") {
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn=="r") {
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn=="p") {
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn=="c"){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn=="y"){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn=="b"){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="pk") {
                    if (lastPause_pk != 0)
                        cm_pk.setBase(cm_pk.getBase() + SystemClock.elapsedRealtime() - lastPause_pk);
                    else
                        cm_pk.setBase(SystemClock.elapsedRealtime());
                    if(ib_g.isEnabled()){
                        cm_g.setBase(SystemClock.elapsedRealtime());
                        cm_g.start();
                        ib_g.setEnabled(false);
                    }
                    cm_pk.start();
                    lastBtn = "pk";
                }
                else {
                    lastPause_pk=SystemClock.elapsedRealtime();
                    cm_pk.stop();
                    lastBtn="";
                }
            }
        });
    }



}
