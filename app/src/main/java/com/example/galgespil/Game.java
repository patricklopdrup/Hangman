package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.logging.Logger;

public class Game extends AppCompatActivity implements View.OnClickListener {
    private Galgelogik logik = new Galgelogik();
    private Keyboard keyboard = new Keyboard();
    private HighscoreLogic highscoreLogic = new HighscoreLogic();

    private ImageView gameImg;
    private TextView guessedWord;
    private Button[] keys;
    private String visibleWord;
    private String imgName = "forkert";
    private Chronometer timer;
    private boolean firstLetterGuessed = false;
    private long timePassed;

    private ProgressBar progressLeft, progressRight;
    private int highScoreMilisec;

    private String keyboardKey = "keyboard";
    private int keyboardChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // TODO: 04-11-2019 lav loading, når den er ved at hente ord fra dr
        //gets word from dr.dk. Uses the same Galgelogik logik as above (global variable)
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    logik.hentOrdFraDr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return logik.getSynligtOrd();
            }

            @Override
            protected void onPostExecute(Object o) {
                visibleWord = o.toString();
                guessedWord.setText(visibleWord);
                logik.logStatus();
            }
        }.execute();

        keyboardChoice = getKeyboardChoise(keyboardKey, this);

        //if there is a highscore the timer is set to that time
        List<HighscoreObject> temp = highscoreLogic.getSortedHighscoreList(highscoreLogic.getHighscoreKey(), this);
        highScoreMilisec = (!temp.isEmpty()) ? (int)temp.get(0).getTime() : 30000;

        //gets all the key values for the keyboard from the Keyboard.java class
        //and set onClickListener
        keys = new Button[keyboard.getKeys(keyboardChoice).length];
        for (int i = 0; i < keyboard.getKeys(keyboardChoice).length; i++) {
            String buttonToFind = "button" + (i + 1);
            int buttonID = getResources().getIdentifier(buttonToFind, "id", getPackageName());
            String key = keyboard.getKeys(keyboardChoice)[i];
            keys[i] = findViewById(buttonID);
            keys[i].setText(key);
            keys[i].setOnClickListener(this);
        }

        guessedWord = findViewById(R.id.guessWord);
        guessedWord.setLetterSpacing((float) 0.5);

        timer = findViewById(R.id.timer);

        progressLeft = findViewById(R.id.progressBarLeft);
        progressRight = findViewById(R.id.progressBarRight);

        gameImg = findViewById(R.id.galgeImage);
    }

    @Override
    public void onClick(View view) {
        for (Button btn : keys) {
            if (btn.getId() == view.getId()) {
                //starting the timer the first time, and only the first time, a key is pressed
                if (!firstLetterGuessed) {
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    firstLetterGuessed = true;
                    progressBarThread();
                }

                logik.gætBogstav(btn.getText().toString());
                guessedWord.setText(logik.getSynligtOrd());

                logik.logStatus();

                //if the user guess a wrong letter we show next img
                if (!logik.erSidsteBogstavKorrekt()) {
                    String imgToShow = imgName + logik.getAntalForkerteBogstaver();
                    //gets the exact id for the img
                    int resID = getResources().getIdentifier(imgToShow, "drawable", getPackageName());
                    gameImg.setImageResource(resID);
                    btn.setTextColor(Color.RED);
                    btn.setClickable(false);
                }
                if (logik.erSidsteBogstavKorrekt()) {
                    btn.setTextColor(Color.parseColor("#08A026"));
                    btn.setClickable(false);
                }
                if (logik.erSpilletTabt()) {
                    gameEnded(false);
                    guessedWord.setText(showWordAfterLoss(guessedWord.toString(), logik.getOrdet()));
                }
                if (logik.erSpilletVundet()) {
                    timePassed = SystemClock.elapsedRealtime() - timer.getBase();
                    System.out.println("her er tiden: " + timePassed);
                    highscoreLogic.addScore(timePassed, logik.getBrugteBogstaver().size(), logik.getOrdet(), highscoreLogic.getHighscoreKey(), this);
                    gameEnded(true);
                }
            }
        }
    }

    // TODO: 02-10-2019 fix denne metode
    public String showWordAfterLoss(String lossWord, String correctWord) {
        SpannableString ss = new SpannableString(correctWord);
        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        for (int i = 0; i < correctWord.length(); i++) {
            if (lossWord.charAt(i) == '*') {
                ss.setSpan(red, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss.toString();
    }

    public void gameEnded(boolean winner) {
        timer.stop();
        for (Button b : keys) {
            b.setClickable(false);
        }
        Intent i = new Intent(this, EndGame.class);
        i.putExtra("winner", winner);
        i.putExtra("guesses", logik.getBrugteBogstaver().size());
        if(winner) {
            i.putExtra("time", timePassed);
        }
        System.out.println("winner er: " + winner);
        startActivity(i);
    }

    public void progressBarThread() {
        //progressBars in another thread
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            int secToRun = (highScoreMilisec / 100);
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

    public void saveKeyboardChoise(String key, int keyboardChoice, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, keyboardChoice);
        editor.commit();
    }

    public int getKeyboardChoise(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, -1);
    }

    public String getKeyboardKey() {
        return keyboardKey;
    }
}