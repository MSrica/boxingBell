package com.example.boxingbell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    int threeMinutesInMilliseconds = 180000;
    int oneMinuteInMilliseconds = 60000;
    int oneSecondInMilliseconds = 1000;
    int minuteInSeconds = 60;

    CountDownTimer threeMinutesCountdown = null;
    CountDownTimer oneMinuteCountdown = null;

    TextView timerTextView;
    Button startGiveUpButton;

    protected void printTime(long millisecondsUntilFinished){
        int minutes = (int) (millisecondsUntilFinished / oneMinuteInMilliseconds);
        int seconds = (int) ((millisecondsUntilFinished / oneSecondInMilliseconds) - (minuteInSeconds * minutes));
        String time = String.format(Locale.getDefault(), "%d:%02d",  minutes, seconds);
        timerTextView.setText(time);
    }

    protected void threeMinutesCountdownStop(){
        threeMinutesCountdown.cancel();
        threeMinutesCountdown = null;
        oneMinuteCountdownInitialization();
    }
    protected void oneMinuteCountdownStop(){
        oneMinuteCountdown.cancel();
        oneMinuteCountdown = null;
        threeMinutesCountdownInitialization();
    }

    protected void viewsInitialization(){
        timerTextView = findViewById(R.id.timer);
        startGiveUpButton = findViewById(R.id.startGiveUpButton);
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

        timerTextView.setText(getString(R.string.initTimerString));
        startGiveUpButton.setText(getString(R.string.startString));
    }
    protected void timerActions(){
        if(startGiveUpButton.getText().equals("Start")) startCountdown();
        else if(startGiveUpButton.getText().equals("Give Up")) resetCountdown();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewsInitialization();

        startGiveUpButton.setOnClickListener(new View.OnClickListener() {public void onClick(View v) { timerActions(); }});
    }
}