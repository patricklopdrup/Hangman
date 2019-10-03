package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Game extends AppCompatActivity implements View.OnClickListener {
    Galgelogik logik = new Galgelogik();

    ImageView gameImg;
    TextView guessedWord;
    EditText guessedLetter;
    Button playButton, restartButton, homeButton;
    String visibleWord = logik.getSynligtOrd();
    Editable letter;
    String imgName = "forkert";
    Chronometer timer;
    boolean firstLetterGuessed = false;
    long timePassed;

    ProgressBar progressLeft, progressRight;
    int highScoreMilisec = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        guessedWord = findViewById(R.id.guessWord);
        guessedWord.setText(visibleWord);
        guessedWord.setLetterSpacing((float)0.5);

        timer = findViewById(R.id.timer);

        progressLeft = findViewById(R.id.progressBarLeft);
        progressRight = findViewById(R.id.progressBarRight);

        logik.logStatus();

        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
        //starting 'restart-' and 'homebutton' hidden(GONE)
        restartButton = findViewById(R.id.playAgain);
        restartButton.setVisibility(View.GONE);
        restartButton.setOnClickListener(this);
        homeButton = findViewById(R.id.home);
        homeButton.setVisibility(View.GONE);
        homeButton.setOnClickListener(this);

        gameImg = findViewById(R.id.galgeImage);
        guessedLetter = findViewById(R.id.guessedLetter);
        letter = guessedLetter.getText();
    }

    @Override
    public void onClick(View view) {
        if(view == playButton) {
            //starting the timer the first time, and only the first time, playButton(gæt bogstav) is clicked
            if(!firstLetterGuessed) {
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
                firstLetterGuessed = true;
                progressBarThread();
            }

            logik.gætBogstav(letter.toString());

            guessedWord.setText(logik.getSynligtOrd());
            guessedLetter.getText().clear();

            logik.logStatus();

            //if the user guess a wrong letter we show next img
            if(!logik.erSidsteBogstavKorrekt()) {
                String imgToShow = imgName + logik.getAntalForkerteBogstaver();
                //gets the exact id for the img
                int resID = getResources().getIdentifier(imgToShow, "drawable", getPackageName());
                gameImg.setImageResource(resID);
            }
            if(logik.erSpilletTabt()) {
                gameEnded();
                guessedWord.setText(showWordAfterLoss(guessedWord.toString(), logik.getOrdet()));
            }
            if(logik.erSpilletVundet()) {
                timePassed = SystemClock.elapsedRealtime() - timer.getBase();
                System.out.println(timePassed);
                gameEnded();
            }

        } else if(view == homeButton) {
            //sending the time via intent
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("time", timePassed);
            startActivity(i);
        } else if(view == restartButton) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        }

        // TODO: 02-10-2019 lav progressbar der tæller ned fra highscore, så man kan se om man kan nå 1. pladsen
    }

    // TODO: 02-10-2019 fix denne metode
    public String showWordAfterLoss(String lossWord, String correctWord) {
        SpannableString ss = new SpannableString(correctWord);
        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        for(int i = 0; i < correctWord.length(); i++) {
            if(lossWord.charAt(i) == '*') {
                ss.setSpan(red, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss.toString();
    }

    public void gameEnded() {
        timer.stop();
        restartButton.setVisibility(View.VISIBLE);
        homeButton.setVisibility(View.VISIBLE);
    }

    public void progressBarThread() {
        //progressBars in another thread
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            int secToRun = highScoreMilisec / 100;
            int pgStatus = 0;
            @Override
            public void run() {
                while (pgStatus < 100) {
                    pgStatus++;
                    try {
                        Thread.sleep(secToRun);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //update processBars
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressLeft.setProgress(pgStatus);
                            progressRight.setProgress(pgStatus);
                        }
                    });
                }
            }
        }).start();
    }

}
