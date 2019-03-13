package com.example.workometer;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private CustomCountDownTimer timer1;
    private CustomCountDownTimer timer2;
    private CustomCountDownTimer timer3;
    private CustomCountDownTimer timer4;
    private CustomCountDownTimer timer5;
    private CustomCountDownTimer timer6;
    private CustomCountDownTimer timer7;
    private CustomCountDownTimer timer8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        initViews();
    }


    private void initViews() {
        this.timer1 = new CustomCountDownTimer(this, R.id.progressBarCircle1,
                R.id.textViewTime1, R.id.imageViewStartStop1, 25200000, (byte) 1);
        this.timer1.setMax(25200000);
        this.timer2 = new CustomCountDownTimer(this, R.id.progressBarCircle2,
                R.id.textViewTime2, R.id.imageViewStartStop2, 7200000, (byte) 2);
        this.timer2.setMax(7200000);
        this.timer3 = new CustomCountDownTimer(this, R.id.progressBarCircle3,
                R.id.textViewTime3, R.id.imageViewStartStop3, 7200000, (byte) 3);
        this.timer3.setMax(7200000);
        this.timer4 = new CustomCountDownTimer(this, R.id.progressBarCircle4,
                R.id.textViewTime4, R.id.imageViewStartStop4, 7200000, (byte) 4);
        this.timer4.setMax(7200000);
        this.timer5 = new CustomCountDownTimer(this, R.id.progressBarCircle5,
                R.id.textViewTime5, R.id.imageViewStartStop5, 600000, (byte) 5);
        this.timer5.setMax(600000);
        this.timer6 = new CustomCountDownTimer(this, R.id.progressBarCircle6,
                R.id.textViewTime6, R.id.imageViewStartStop6, 600000, (byte) 6);
        this.timer6.setMax(600000);
        this.timer7 = new CustomCountDownTimer(this, R.id.progressBarCircle7,
                R.id.textViewTime7, R.id.imageViewStartStop7, 600000, (byte) 7);
        this.timer7.setMax(600000);
        this.timer8 = new CustomCountDownTimer(this, R.id.progressBarCircle8,
                R.id.textViewTime8, R.id.imageViewStartStop8, 600000, (byte) 8);
        this.timer8.setMax(600000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences("main", MODE_PRIVATE).edit().putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer1.onStop();
        timer2.onStop();
        timer3.onStop();
        timer4.onStop();
        timer5.onStop();
        timer6.onStop();
        timer7.onStop();
        timer8.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getSharedPreferences("main", MODE_PRIVATE).getInt("day", 0) != Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            this.timer1.resetTimer();
            this.timer2.resetTimer();
            this.timer3.resetTimer();
            this.timer4.resetTimer();
            this.timer5.resetTimer();
            this.timer6.resetTimer();
            this.timer7.resetTimer();
            this.timer8.resetTimer();
            getSharedPreferences("main", MODE_PRIVATE).edit().putInt("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).apply();
        } else {
            timer1.onStart();
            timer2.onStart();
            timer3.onStart();
            timer4.onStart();
            timer5.onStart();
            timer6.onStart();
            timer7.onStart();
            timer8.onStart();
        }

    }
}
