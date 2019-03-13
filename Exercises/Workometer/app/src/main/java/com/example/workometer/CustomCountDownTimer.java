package com.example.workometer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CustomCountDownTimer implements View.OnClickListener {

    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewStartStop;
    private int imageViewId;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private Context context;
    private long timeCountInMilliSeconds;
    private long defaultTime;
    private long endTime;
    private byte id;


    public CustomCountDownTimer(Context context, int progressBarId, int textViewTimeId, int imageViewId, int timeCountInMilliSeconds, byte id) {
        this.progressBarCircle = ((Activity) context).findViewById(progressBarId);
        this.textViewTime = ((Activity) context).findViewById(textViewTimeId);
        this.imageViewStartStop = ((Activity) context).findViewById(imageViewId);
        this.imageViewId = imageViewId;
        this.timeCountInMilliSeconds = timeCountInMilliSeconds;
        this.defaultTime = timeCountInMilliSeconds;
        this.context = context;
        this.id = id;
        this.initListener();
    }

    private void initListener() {
        this.imageViewStartStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == imageViewId) {
            if (timerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        }
    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeCountInMilliSeconds;
        // call to initialize the progress bar values
        setProgressBarValues();
        imageViewStartStop.setImageResource(R.drawable.icon_stop);
        // making edit text not editable
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCountInMilliSeconds = millisUntilFinished;
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                updateView();
            }
        }.start();
        timerRunning = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        updateView();
    }

    private void updateCountDownText() {
        int hours = (int) (timeCountInMilliSeconds / 1000) / 3600;
        int minutes = (int) ((timeCountInMilliSeconds / 1000) % 3600) / 60;
        int seconds = (int) (timeCountInMilliSeconds / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        textViewTime.setText(timeLeftFormatted);
    }

    private void updateView() {
        updateCountDownText();
        // call to initialize the progress bar values
        setProgressBarValues();
        // changing stop icon to start icon
        imageViewStartStop.setImageResource(R.drawable.icon_start);
        // making edit text editable
    }

    private void setProgressBarValues() {
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }

    protected void resetTimer() {
        timeCountInMilliSeconds = progressBarCircle.getMax() * 1000;
        setProgressBarValues();
        updateCountDownText();
    }

    protected void setMax(int millis) {
        progressBarCircle.setMax((int) millis / 1000);
    }

    protected void onStop() {
        SharedPreferences prefs = this.context.getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft" + id, timeCountInMilliSeconds);
        editor.putBoolean("timerRunning" + id, timerRunning);
        editor.putLong("endTime" + id, endTime);

        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    protected void onStart() {
        SharedPreferences prefs = this.context.getSharedPreferences("prefs", MODE_PRIVATE);

        timeCountInMilliSeconds = prefs.getLong("millisLeft" + id, 60000);
        timerRunning = prefs.getBoolean("timerRunning" + id, false);

        updateCountDownText();
        updateView();

        if (timerRunning) {
            endTime = prefs.getLong("endTime" + id, 0);
            timeCountInMilliSeconds = endTime - System.currentTimeMillis();

            if (timeCountInMilliSeconds < 0) {
                timeCountInMilliSeconds = 0;
                timerRunning = false;
                updateCountDownText();
                updateView();
            } else {
                startTimer();
            }
        }
    }

}
