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

import java.util.ArrayList;
import java.util.List;

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

    //two progressionbars. The right one is rotated 180 degrees
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

            // TODO: 11-11-2019 lav en cache til ordene, så den ikke skal hente hver gang
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    logik.hentOrdFraDr();
                    filterWordFromDr(5);
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
                for(Button btn : keys) btn.setClickable(true);
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
            //start with keys unclickable
            keys[i].setClickable(false);
        }

        //more spacing between letters in the word that's about to be guessed
        guessedWord = findViewById(R.id.guessWord);
        guessedWord.setLetterSpacing((float) 0.5);

        //this is the clock in the upper right corner
        timer = findViewById(R.id.timer);

        progressLeft = findViewById(R.id.progressBarLeft);
        progressRight = findViewById(R.id.progressBarRight);

        gameImg = findViewById(R.id.galgeImage);
    }

    /**
     * This filter the list of words in "muligeOrd" to be larger than minimumSize
     * @param minimumSize the minimum length of a word
     */
    private void filterWordFromDr(int minimumSize) {
        int arrSize = logik.muligeOrd.size();
        //need to be called from the last element to the first because the size getting smaller when we remove
        for(int i = arrSize-1; i >= 0; i--) {
            if(logik.muligeOrd.get(i).length() < minimumSize) {
                logik.muligeOrd.remove(i);
            }
        }
        //we call "nulstil()" to get a word from our filtered list
        logik.nulstil();
        System.out.println("start str: " + arrSize + "\nsorteret str: " + logik.muligeOrd.size());
        System.out.println("filtreret muligeord= " + logik.muligeOrd);
    }

    @Override
    public void onClick(View view) {
        //looping all buttons to see which one is pressed
        for (Button btn : keys) {
            if (btn.getId() == view.getId()) {
                //starting the timer the first time, and only the first time, a key is pressed
                if (!firstLetterGuessed) {
                    timer.setBase(SystemClock.elapsedRealtime());
                    timer.start();
                    firstLetterGuessed = true;
                    //starting the progressbar in another thread
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
                    //setting the button to color: red and make it unclickable
                    btn.setTextColor(Color.RED);
                    btn.setClickable(false);
                }
                if (logik.erSidsteBogstavKorrekt()) {
                    //makes the button green and unclickable
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
                    //adding the score to sharedPrefs manager
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
        //send extra data over to intent
        i.putExtra("winner", winner);
        i.putExtra("guesses", logik.getBrugteBogstaver().size());
        //we only send the time if the game were won and the word if lost
        if(winner) i.putExtra("time", timePassed);
        else i.putExtra("word", logik.getOrdet());
        System.out.println("winner er: " + winner);
        startActivity(i);
        //when we go to winner/loser activity we delete the game from backstack
        finish();
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

    // TODO: 10-11-2019 add til Keyboard klasse
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