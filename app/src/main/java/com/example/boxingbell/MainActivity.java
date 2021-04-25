package com.example.boxingbell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

// variables
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // constants
    protected final int threeMinutesInMilliseconds = 180000, oneMinuteInMilliseconds = 60000, oneSecondInMilliseconds = 1000, minuteInSeconds = 60;

    // layout
    protected TextView timerTextView, clockTextView;
    protected Button startGiveUpButton;
    protected ConstraintLayout layout;
    protected ViewTreeObserver vto;

    // times
    protected CountDownTimer threeMinutesCountdown = null, oneMinuteCountdown = null;
    private final Handler clockHandler = new Handler();
    private final Runnable clockRunnable = new Runnable() {
        public void run() {
            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", Calendar.getInstance().getTime().getHours(), Calendar.getInstance().getTime().getMinutes(), Calendar.getInstance().getTime().getSeconds());
            clockTextView.setText(time);

            clockHandler.postDelayed(this, 1000);
        }
    };

    // miscellaneous
    protected MediaPlayer mediaPlayer;

// functions
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // initialization
    protected void viewsInitialization(){
    layout = findViewById(R.id.mainLayout);
    timerTextView = findViewById(R.id.timer);
    clockTextView = findViewById(R.id.clock);
    startGiveUpButton = findViewById(R.id.startGiveUpButton);

    vto = layout.getViewTreeObserver();
}
    protected void layoutSetup(){
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final ConstraintLayout layout = findViewById(R.id.mainLayout);

            @Override
            public void onGlobalLayout() {
                this.layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final int layoutWidth  = layout.getMeasuredWidth();
                final int layoutHeight = layout.getMeasuredHeight();

                ViewGroup.LayoutParams startGiveUpButtonParams = startGiveUpButton.getLayoutParams();

                startGiveUpButtonParams.width = (int) (layoutWidth / 3);
                startGiveUpButtonParams.height = (int) (layoutHeight * 0.2);

                startGiveUpButton.setLayoutParams(startGiveUpButtonParams);

                clockTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (layoutHeight * 0.1));
                timerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) (layoutHeight * 0.5));
            }
        });
    }
    protected void oneMinuteCountdownInitialization(){
        oneMinuteCountdown = new CountDownTimer(oneMinuteInMilliseconds, oneSecondInMilliseconds) {
            public void onTick(long millisecondsUntilFinished) { printTime(millisecondsUntilFinished); }
            public void onFinish() { oneMinuteCountdownStop(); }
        }.start();
    }
    protected void threeMinutesCountdownInitialization(){
        threeMinutesCountdown = new CountDownTimer(threeMinutesInMilliseconds, oneSecondInMilliseconds) {
            public void onTick(long millisecondsUntilFinished) { printTime(millisecondsUntilFinished); }
            public void onFinish() { threeMinutesCountdownStop(); }
        }.start();
    }

    // countdown
    protected void startCountdown(){
        threeMinutesCountdownInitialization();
        threeMinutesCountdown.start();
        startGiveUpButton.setText(getString(R.string.giveUpString));
        startGiveUpButton.setBackgroundColor(startGiveUpButton.getContext().getResources().getColor(R.color.pink));
    }
    protected void resetCountdown(){
        if(threeMinutesCountdown != null){
            threeMinutesCountdown.cancel();
            threeMinutesCountdown = null;
        }
        if(oneMinuteCountdown != null){
            oneMinuteCountdown.cancel();
            oneMinuteCountdown = null;
        }

        timerTextView.setText(getString(R.string.timerString));
        startGiveUpButton.setText(getString(R.string.startString));
        startGiveUpButton.setBackgroundColor(startGiveUpButton.getContext().getResources().getColor(R.color.cyan));
    }

    protected void oneMinuteCountdownStop(){
        mediaPlayer.start();
        oneMinuteCountdown.cancel();
        oneMinuteCountdown = null;
        threeMinutesCountdownInitialization();
    }
    protected void threeMinutesCountdownStop(){
        mediaPlayer.start();
        threeMinutesCountdown.cancel();
        threeMinutesCountdown = null;
        oneMinuteCountdownInitialization();
    }

    // button
    protected void timerActions(){
        if(startGiveUpButton.getText().equals("Start")) startCountdown();
        else if(startGiveUpButton.getText().equals("Give Up")) resetCountdown();
    }

    // display values
    protected void printTime(long millisecondsUntilFinished){
        final int minutes = (int) (millisecondsUntilFinished / oneMinuteInMilliseconds);
        final int seconds = (int) ((millisecondsUntilFinished / oneSecondInMilliseconds) - (minuteInSeconds * minutes));
        final String time = String.format(Locale.getDefault(), "%d:%02d",  minutes, seconds);
        timerTextView.setText(time);
    }

// lifecycle
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        viewsInitialization();
        layoutSetup();

        clockRunnable.run();
        mediaPlayer = MediaPlayer.create(this, R.raw.boxing_sound);
        startGiveUpButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v) { timerActions(); }});
    }
}