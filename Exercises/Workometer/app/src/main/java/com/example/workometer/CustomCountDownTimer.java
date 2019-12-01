package com.example.workometer;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class CustomCountDownTimer implements View.OnClickListener {

    private ProgressBar progressBarCircle;
    private EditText editText;
    private EditText editTime;
    private TextView textViewTime;
    private ImageView imageViewStartStop;
    private int imageViewId;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private Context context;
    private long timeCountInMilliseconds;
    private long defaultTime;
    private long endTime;
    private byte id;
    private SharedPreferences prefs;

    public CustomCountDownTimer(Context context, int progressBarId, int editTextId, int editTimeId, int textViewTimeId, int imageViewId, int timeCountInmilliseconds, byte id) {
        this.progressBarCircle = ((Activity) context).findViewById(progressBarId);
        this.editText = ((Activity) context).findViewById(editTextId);
        this.editTime = ((Activity) context).findViewById(editTimeId);
        this.textViewTime = ((Activity) context).findViewById(textViewTimeId);
        this.imageViewStartStop = ((Activity) context).findViewById(imageViewId);
        this.imageViewId = imageViewId;
        this.timeCountInMilliseconds = timeCountInmilliseconds;
        this.defaultTime = timeCountInmilliseconds;
        this.context = context;
        this.id = id;
        this.prefs = this.context.getSharedPreferences("prefs", MODE_PRIVATE);
        this.setEditText();
        this.initListeners();
    }

    private void initListeners() {
        this.imageViewStartStop.setOnClickListener(this);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setSelection(editText.getText().length());
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setSelection(editText.getText().length());
            }
        });

        this.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit().putString("autoSave" + id, s.toString()).apply();
            }
        });

        editTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTime.setSelection(editTime.getText().length());
                }
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTime.setSelection(editTime.getText().length());
            }
        });

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
        updateTime(editTime.getText().toString());
        endTime = System.currentTimeMillis() + timeCountInMilliseconds;
        // call to initialize the progress bar values
        setProgressBarValues();
        imageViewStartStop.setImageResource(R.drawable.icon_stop);
        // making edit text not editable
        countDownTimer = new CountDownTimer(timeCountInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeCountInMilliseconds = millisUntilFinished;
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
            timerRunning = false;
            updateView();
        }
    }

    private void setEditText() {
        editText.setText(prefs.getString("autoSave" + id, "Add task"));
    }

    private void updateTime(String timeFormat) {
        if (!timeFormat.contains(":")) {
            try {
                long minutes = Long.parseLong(timeFormat);
                this.timeCountInMilliseconds = minutes * 60000;
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Please enter valid minutes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateCountDownText() {
        int hours = (int) (timeCountInMilliseconds / 1000) / 3600;
        int minutes = (int) ((timeCountInMilliseconds / 1000) % 3600) / 60;
        int seconds = (int) (timeCountInMilliseconds / 1000) % 60;

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
        progressBarCircle.setProgress((int) timeCountInMilliseconds / 1000);
    }

    protected void resetTimer() {
        timeCountInMilliseconds = progressBarCircle.getMax() * 1000;
        setProgressBarValues();
        updateCountDownText();
    }

    protected void setMax(int millis) {
        progressBarCircle.setMax((int) millis / 1000);
    }

    protected void onStop() {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft" + id, timeCountInMilliseconds);
        editor.putBoolean("timerRunning" + id, timerRunning);
        editor.putLong("endTime" + id, endTime);

        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    protected void onStart() {
        timeCountInMilliseconds = prefs.getLong("millisLeft" + id, 60000);
        timerRunning = prefs.getBoolean("timerRunning" + id, false);

        updateCountDownText();
        updateView();

        if (timerRunning) {
            endTime = prefs.getLong("endTime" + id, 0);
            timeCountInMilliseconds = endTime - System.currentTimeMillis();

            if (timeCountInMilliseconds < 0) {
                timeCountInMilliseconds = 0;
                timerRunning = false;
                updateCountDownText();
                updateView();
            } else {
                startTimer();
            }
        }
    }

}
