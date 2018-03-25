package com.example.jc.timemanager;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public void showAlert(View view){
        View v =view;
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

                        showDayStats();
                        reset();
                    }
                })
                .create();
        alert.show();
    }



    Button button;
    Button reset;
    TextView text;
    Chronometer cm_g,cm_o,cm_r,cm_p,cm_b,cm_c,cm_y,cm_gr;
    ImageButton ib_g,ib_o,ib_r,ib_p,ib_b,ib_c,ib_y,ib_gr;
    long lastPause= 0,lastPause_o = 0,lastPause_r = 0,lastPause_p = 0,lastPause_b= 0,lastPause_c= 0,lastPause_y= 0,lastPause_gr= 0;
    String lastBtn = "";
    Double p_g,p_r,p_p;
    public void reset(){
        lastPause= 0;lastPause_o = 0;lastPause_r = 0;lastPause_p = 0;lastPause_b= 0;lastPause_c= 0;lastPause_y= 0;lastPause_gr= 0;
        cm_g.stop();cm_b.stop();cm_r.stop();cm_p.stop();cm_o.stop();cm_c.stop();cm_y.stop();cm_gr.stop();
        cm_g.setBase(SystemClock.elapsedRealtime());cm_o.setBase(SystemClock.elapsedRealtime());cm_p.setBase(SystemClock.elapsedRealtime());cm_r.setBase(SystemClock.elapsedRealtime());cm_b.setBase(SystemClock.elapsedRealtime());
        cm_c.setBase(SystemClock.elapsedRealtime());cm_y.setBase(SystemClock.elapsedRealtime());cm_gr.setBase(SystemClock.elapsedRealtime());
        lastBtn="";
        ib_g.setEnabled(true);
    }
    public void showDayStats() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        CharSequence zx = cm_r.getText();
        String[] rtmp = zx.toString().split(":");
        String[] gtmp = cm_g.getText().toString().split(":");
        String[] otmp = cm_o.getText().toString().split(":");
        String[] ptmp = cm_p.getText().toString().split(":");
        double g = Integer.parseInt(gtmp[0]) * 60
                + Integer.parseInt(gtmp[1]) ;
        double r =Integer.parseInt(rtmp[0]) * 60
                + Integer.parseInt(rtmp[1]) ;
        double p =Integer.parseInt(ptmp[0]) * 60
                + Integer.parseInt(ptmp[1]) ;
        double o =Integer.parseInt(otmp[0]) * 60
                + Integer.parseInt(otmp[1]) ;

        // int zz =Integer.parseInt(zx.toString());

        //r=(Math.ceil((r/g)*10000)/100);

        alert.setMessage("You spent " + Math.ceil((o/g)*10000)/100  +"% of your day on Leisure"+"\n"+"You spent " + Math.ceil((r/g)*10000)/100  +"% of your day on Exercise"+"\n"+"You spent " + Math.ceil((p/g)*10000)/100  +"% of your day Education")

                .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        reset();
                    }
                })
                .create();
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text =  findViewById(R.id.textView);
        button =  findViewById(R.id.button);
        reset = findViewById(R.id.btn_reset);

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



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                text.setText("");
                 showAlert(v);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
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

        ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn=="p") {//If the last btn pressed was the purple one
                    lastPause_p = SystemClock.elapsedRealtime();//Set the last time we paused to Realtime
                    cm_p.stop();//Stop the purple timer
                }

                if(lastBtn=="r") {
                    lastPause_r = SystemClock.elapsedRealtime();
                    cm_r.stop();
                    //}
                }

                if(lastBtn=="b"){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn=="c"){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn=="y"){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="o") {
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
                    lastPause_o=SystemClock.elapsedRealtime();
                    cm_o.stop();
                    lastBtn="";
                }



            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(lastBtn=="o") {
                    lastPause_o = SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn=="r") {
                    lastPause_r = SystemClock.elapsedRealtime();
                    cm_r.stop();
                }

                if(lastBtn=="b") {
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }

                if(lastBtn=="c"){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn=="y"){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }
                if(lastBtn!="p") {
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
                    lastPause_p=SystemClock.elapsedRealtime();
                    cm_p.stop();
                    lastBtn="";
                }

            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(lastBtn=="o") {
                    lastPause_o = SystemClock.elapsedRealtime();
                    cm_o.stop();
                }

                if(lastBtn=="p") {
                    lastPause_p = SystemClock.elapsedRealtime();
                    cm_p.stop();
                }

                if(lastBtn=="b") {
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }

                if(lastBtn=="c"){
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                }
                if(lastBtn=="y"){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="r") {
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
                    lastPause_r=SystemClock.elapsedRealtime();
                    cm_r.stop();
                    lastBtn="";
                }
            }
        });

        ib_b.setOnClickListener(new View.OnClickListener() {
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
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="b") {
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
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                    lastBtn="";
                }
            }
        });


        ib_c.setOnClickListener(new View.OnClickListener() {
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

                if(lastBtn=="b"){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn=="y"){
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="c") {
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
                    lastPause_c=SystemClock.elapsedRealtime();
                    cm_c.stop();
                    lastBtn="";
                }
            }
        });


        ib_y.setOnClickListener(new View.OnClickListener() {
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
                if(lastBtn=="b"){
                    lastPause_b=SystemClock.elapsedRealtime();
                    cm_b.stop();
                }
                if(lastBtn=="gr"){
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                }

                if(lastBtn!="y") {
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
                    lastPause_y=SystemClock.elapsedRealtime();
                    cm_y.stop();
                    lastBtn="";
                }
            }
        });


        ib_gr.setOnClickListener(new View.OnClickListener() {
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

                if(lastBtn!="gr") {
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
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                    lastBtn="";
                }
            }
        });
/*
        ib_gr.setOnClickListener(new View.OnClickListener() {
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

                if(lastBtn!="gr") {
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
                    lastPause_gr=SystemClock.elapsedRealtime();
                    cm_gr.stop();
                    lastBtn="";
                }
            }
        });*/
    }



}
