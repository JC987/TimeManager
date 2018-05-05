package com.example.jc.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

public class testActivity extends AppCompatActivity {
    Chronometer cm_o,cm_r,cm_p,cm_g,cm_b,cm_gr,cm_c,cm_y,cm_pk;
    public static final int FINAL_INT=0;
    ImageButton ib_o,ib_r,ib_p,ib_g,ib_b,ib_gr,ib_c,ib_y,ib_pk;
    long lp_o=0,lp_r=0,lp_p=0,lp_b=0,lp_gr=0,lp_c=0,result;
    myTimer orange,red,purple,green,blue,grey,cyan,yellow,pink;
    Button endDay,reflect,adjust;
    public void showAlert(View view, String msg, final Boolean value ){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg)

                .setNegativeButton("Not yet!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
                .setPositiveButton("All Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(testActivity.this, "Day has ended!", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();

                        if(value) {
                            setEnableBtns(value);
                            reset();
                        }
                        else{
                            setEnableBtns(value);
                            myTimer.stopAll();
                        }
                    }
                })

                .create();
        alert.show();

    }

    public void reset(){
        orange.reset();red.reset();purple.reset(); blue.reset();grey.reset();cyan.reset(); yellow.reset();pink.reset();

    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FINAL_INT) : {
                if (resultCode == Activity.RESULT_OK) {
                   result = data.getIntExtra("sub",0);
                   String cat = data.getStringExtra("cat");
                   if(cat.equals("Leisure")){
                       orange.add(result);
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
                    // TODO Update your TextView.
                }
                break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ib_o = findViewById(R.id.imgBtnOrange);
        ib_r = findViewById(R.id.imgBtnRed);
        ib_g = findViewById(R.id.imageButton);
        ib_p = findViewById(R.id.imgBtnPurple);
        ib_b = findViewById(R.id.imgBtnBlue);
        ib_gr = findViewById(R.id.imgBtnGrey);
        ib_c = findViewById(R.id.imgBtnC);
        ib_y = findViewById(R.id.imgBtnYellow);
        ib_pk =findViewById(R.id.imgBtnPink);

        cm_o = findViewById(R.id.chronometer_orange);
        cm_r = findViewById(R.id.chronometer_red);
        cm_p = findViewById(R.id.chronometer_purple);
        cm_g = findViewById(R.id.chronometer_green);
        cm_b = findViewById(R.id.chronometer_blue);
        cm_gr = findViewById(R.id.chronometer_grey);
        cm_c = findViewById(R.id.chronometer_cyan);
        cm_y = findViewById(R.id.chronometer_yellow);
        cm_pk = findViewById(R.id.chronometer_pink);

        endDay = findViewById(R.id.endDay);
        reflect = findViewById(R.id.btn_reset);
        adjust = findViewById(R.id.adjust);

        green = new myTimer(cm_g);
        orange = new myTimer(cm_o,lp_o);
        red = new myTimer(cm_r,lp_r);
        purple = new myTimer(cm_p,lp_p);
        blue = new myTimer(cm_b,lp_b);
        grey = new myTimer(cm_gr,lp_gr);
        cyan = new myTimer(cm_c,lp_c);
        yellow = new myTimer(cm_y,0);
        pink = new myTimer(cm_pk,0);


        adjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(testActivity.this,adjustTimers.class);
                startActivityForResult(i, FINAL_INT);
            }
        });
        endDay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showAlert(v,"Do you want to end the day",true);
             //   setEnableBtns(true);
            }
        });
        reflect.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlert(view,"Do you want to reflect on the day",false);
             //   setEnableBtns(false);
            }
        }));

        ib_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTimer.wakeUp();
                ib_g.setEnabled(false);
            }
        });
        ib_o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                orange.start();
                ib_g.setEnabled(false);
            }
        });
        ib_r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                red.start();
                ib_g.setEnabled(false);
            }
        });
        ib_p.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                purple.start();
                ib_g.setEnabled(false);
            }
        });
        ib_b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                blue.start();
                ib_g.setEnabled(false);
            }
        });
        ib_gr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                grey.start();
                ib_g.setEnabled(false);
            }
        });
        ib_c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cyan.start();
                ib_g.setEnabled(false);
            }
        });
        ib_y.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                yellow.start();
                ib_g.setEnabled(false);
            }
        });
        ib_pk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pink.start();
                ib_g.setEnabled(false);
            }
        });
    }
}
