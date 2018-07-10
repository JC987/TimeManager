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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
/*<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
   android:interpolator="@android:anim/linear_interpolator">
   <alpha
       android:fromAlpha="1.0"
       android:toAlpha="0.1"
       android:duration="500"
       android:repeatCount="1"
       android:repeatMode="reverse" />
</set>*/
