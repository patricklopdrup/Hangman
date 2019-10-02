package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    Boolean firstLetterGuessed = false;
    long timePassed;

    TextView ged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        guessedWord = findViewById(R.id.guessWord);
        guessedWord.setText(visibleWord);
        guessedWord.setLetterSpacing((float)0.5);

        timer = findViewById(R.id.timer);

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
            }

            logik.gætBogstav(letter.toString());

            guessedWord.setText(logik.getSynligtOrd());
            guessedLetter.getText().clear();

            logik.logStatus();
            //playButton.setVisibility(View.GONE);

            //if the user guess a wrong letter we show next img
            if(!logik.erSidsteBogstavKorrekt()) {
                String imgToShow = imgName + logik.getAntalForkerteBogstaver();
                //gets the exact id for the img
                int resID = getResources().getIdentifier(imgToShow, "drawable", getPackageName());
                gameImg.setImageResource(resID);
            }
            if(logik.erSpilletTabt()) {
                timer.stop();
                restartButton.setVisibility(View.VISIBLE);
                homeButton.setVisibility(View.VISIBLE);
                guessedWord.setText(showWordAfterLoss(guessedWord.toString(), logik.getOrdet()));
            }
            if(logik.erSpilletVundet()) {
                timer.stop();
                restartButton.setVisibility(View.VISIBLE);
                homeButton.setVisibility(View.VISIBLE);
            }

        } else if(view == homeButton) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if(view == restartButton) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        }
    }

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

}
