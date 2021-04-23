package com.example.boxingbell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

    int threeMinutesInMilliseconds = 180000;
    int oneMinuteInMilliseconds = 60000;
    int oneSecondInMilliseconds = 1000;
    int minuteInSeconds = 60;

    CountDownTimer threeMinutesCountdown = null;
    CountDownTimer oneMinuteCountdown = null;

    TextView timerTextView, clockTextView;
    Button startGiveUpButton;
    ConstraintLayout layout;
    ViewTreeObserver vto;

    private final Handler clockHandler = new Handler();
    private final Runnable clockRunnable = new Runnable() {
        public void run() {
            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", Calendar.getInstance().getTime().getHours(), Calendar.getInstance().getTime().getMinutes(), Calendar.getInstance().getTime().getSeconds());

            clockTextView.setText(time);

            clockHandler.postDelayed(this, 1000);
        }
    };

    MediaPlayer mediaPlayer;

    protected void printTime(long millisecondsUntilFinished){
        int minutes = (int) (millisecondsUntilFinished / oneMinuteInMilliseconds);
        int seconds = (int) ((millisecondsUntilFinished / oneSecondInMilliseconds) - (minuteInSeconds * minutes));
        String time = String.format(Locale.getDefault(), "%d:%02d",  minutes, seconds);
        timerTextView.setText(time);
    }

    protected void threeMinutesCountdownStop(){
        mediaPlayer.start();
        threeMinutesCountdown.cancel();
        threeMinutesCountdown = null;
        oneMinuteCountdownInitialization();
    }
    protected void oneMinuteCountdownStop(){
        mediaPlayer.start();
        oneMinuteCountdown.cancel();
        oneMinuteCountdown = null;
        threeMinutesCountdownInitialization();
    }

    protected void viewsInitialization(){
        layout = findViewById(R.id.mainLayout);
        timerTextView = findViewById(R.id.timer);
        clockTextView = findViewById(R.id.clock);
        startGiveUpButton = findViewById(R.id.startGiveUpButton);

        vto = layout.getViewTreeObserver();
    }
    protected void threeMinutesCountdownInitialization(){
        threeMinutesCountdown = new CountDownTimer(threeMinutesInMilliseconds, oneSecondInMilliseconds) {
            public void onTick(long millisecondsUntilFinished) { printTime(millisecondsUntilFinished); }
            public void onFinish() { threeMinutesCountdownStop(); }
        }.start();
    }
    protected void oneMinuteCountdownInitialization(){
        oneMinuteCountdown = new CountDownTimer(oneMinuteInMilliseconds, oneSecondInMilliseconds) {
            public void onTick(long millisecondsUntilFinished) { printTime(millisecondsUntilFinished); }
            public void onFinish() { oneMinuteCountdownStop(); }
        }.start();
    }

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
    protected void timerActions(){
        if(startGiveUpButton.getText().equals("Start")) startCountdown();
        else if(startGiveUpButton.getText().equals("Give Up")) resetCountdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        viewsInitialization();

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
        clockRunnable.run();
        mediaPlayer = MediaPlayer.create(this, R.raw.boxing_sound);
        startGiveUpButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v) { timerActions(); }});
    }
}