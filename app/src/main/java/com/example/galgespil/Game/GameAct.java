package com.example.galgespil.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.galgespil.Challenges.ChallengeLogic;
import com.example.galgespil.Challenges.ChallengeObject;
import com.example.galgespil.MyKeyboard;
import com.example.galgespil.Statistic.StatLogic;
import com.example.galgespil.Highscore.HighscoreLogic;
import com.example.galgespil.Highscore.HighscoreObject;
import com.example.galgespil.R;

import java.util.Arrays;
import java.util.List;

public class GameAct extends AppCompatActivity implements View.OnClickListener {
    private Galgelogik logik = new Galgelogik();
    private MyKeyboard myKeyboard = new MyKeyboard();
    private HighscoreLogic highscoreLogic = new HighscoreLogic();
    private StatLogic statLogic = new StatLogic();
    private ChallengeLogic challengeLogic = new ChallengeLogic();

    //views on screen
    private ImageView gameImg;
    private TextView guessedWord;
    private Button[] keys;
    private String visibleWord;
    private Chronometer timer;
    //two progressionbars. The right one is rotated 180 degrees
    private ProgressBar progressLeft, progressRight;

    //for game to run
    private final String IMG_NAME = "forkert";
    private boolean firstLetterGuessed = false;
    //telling how fast the progressbars has to run
    private int highScoreMilisec;

    //for statistics
    private long timePassed;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    private boolean isFirstLetterCorrect;
    private boolean isGameWon = false;
    private int[] guessedLetters = new int[myKeyboard.qwerty.length];
    private boolean isGameCanceled = true;

    //skins
    private String manSkin = "";
    private int[] skinList;

    //for which keyboard is chosen
    private int keyboardChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //load word from the web via AsyncTask
        getWordsFromDr();

        keyboardChoice = myKeyboard.getKeyboardChoise(myKeyboard.getKEYBOARD_KEY(), this);

        skinList = challengeLogic.getChosenSkinList(this, challengeLogic.getSKIN_KEY());
        if (skinList.length == 0) {
            //getting amount of skins from enum in ChallengeObject
            int amountOfSkins = ChallengeObject.SkinGroup.values().length;

            skinList = new int[amountOfSkins];
            //setting default value of -1 at every index if it's the first time getting the list
            for (int i = 0; i < amountOfSkins; i++) {
                skinList[i] = -1;
            }
        }
        System.out.println("her er listen: " + Arrays.toString(skinList));
        manSkin = loadManSkin(skinList);

        //if there is a highscore the timer is set to that time otherwise sat to 30sec
        List<HighscoreObject> temp = highscoreLogic.getSortedHighscoreList(highscoreLogic.getHighscoreKey(), this);
        highScoreMilisec = (!temp.isEmpty()) ? (int) temp.get(0).getTime() : 30000;

        //gets all the key values for the myKeyboard from the MyKeyboard.java class
        //and set onClickListener
        initKeyboard();

        //more spacing between letters in the word that's about to be guessed
        guessedWord = findViewById(R.id.guessWord);
        guessedWord.setLetterSpacing((float) 0.5);

        //this is the clock in the upper right corner
        timer = findViewById(R.id.timer);

        progressLeft = findViewById(R.id.progressBarLeft);
        progressRight = findViewById(R.id.progressBarRight);

        //the hangman image
        gameImg = findViewById(R.id.galgeImage);
    }

    private void initKeyboard() {
        keys = new Button[myKeyboard.getKeys(keyboardChoice).length];
        for (int i = 0; i < myKeyboard.getKeys(keyboardChoice).length; i++) {
            String buttonToFind = "button" + (i + 1);
            int buttonID = getResources().getIdentifier(buttonToFind, "id", getPackageName());
            String key = myKeyboard.getKeys(keyboardChoice)[i];
            keys[i] = findViewById(buttonID);
            keys[i].setText(key);
            keys[i].setOnClickListener(this);
            //start with keys unclickable
            keys[i].setClickable(false);

            //setting the backgroundcolor (background tint)
            loadKeyboardSkin(skinList, keys[i]);
        }
    }

    @Override
    public void onClick(View view) {
        //looping all buttons to see which one is pressed
        for (Button btn : keys) {
            if (btn.getId() == view.getId()) {

                //guessing the letter clicked
                String letter = btn.getText().toString();
                logik.gætBogstav(letter);

                //starting the timer the first time, and only the first time, a key is pressed
                if (!firstLetterGuessed) {
                    firstTimeClicked();
                }
                //counting each time a letter is clicked
                countLetters(letter);

                guessedWord.setText(logik.getSynligtOrd());

                //logging in the console
                logik.logStatus();

                //if the user guess a wrong letter we show next img
                if (!logik.erSidsteBogstavKorrekt()) {
                    wrongGuess(btn);
                }
                //if the guess is correct
                if (logik.erSidsteBogstavKorrekt()) {
                    correctGuess(btn);
                }
                //if the game is lost
                if (logik.erSpilletTabt()) {
                    timePassed = SystemClock.elapsedRealtime() - timer.getBase();
                    isGameWon = false;
                    //calling method for endgame
                    gameEnded();
                }
                //if the game is won
                if (logik.erSpilletVundet()) {
                    timePassed = SystemClock.elapsedRealtime() - timer.getBase();
                    System.out.println("her er tiden: " + timePassed);
                    //adding the score to sharedPrefs manager
                    highscoreLogic.addScore(timePassed, logik.getBrugteBogstaver().size(), logik.getOrdet(), highscoreLogic.getHighscoreKey(), this);
                    isGameWon = true;
                    //calling method for endgame
                    gameEnded();
                }
            }
        }
    }

    public void firstTimeClicked() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        //check if first letter is correct
        isFirstLetterCorrect = logik.erSidsteBogstavKorrekt();
        System.out.println("first letter: " + isFirstLetterCorrect);
        firstLetterGuessed = true;
        //starting the progressbar in another thread
        progressBarThread();
    }

    public void wrongGuess(Button btn) {
        int resID = loadImg(IMG_NAME, logik.getAntalForkerteBogstaver(), manSkin);
        gameImg.setImageResource(resID);
        //setting the button to red and make it unclickable
        wrongGuesses++;
        btn.setTextColor(getColor(R.color.wrong_guess_btn));
        btn.setClickable(false);
    }

    public void correctGuess(Button btn) {
        //makes the button green and unclickable
        rightGuesses++;
        btn.setTextColor(getColor(R.color.correct_guess_btn));
        btn.setClickable(false);
    }

    public void loadKeyboardSkin(int[] skinList, Button key) {
        //find index in enum "array"
        int keyboardSkinLookup = skinList[ChallengeObject.SkinGroup.KEYBOARD_SKIN.ordinal()];
        //take the skin out of the specific challenge if it is -1, we set is as "default.
        String keyboardSkinExtention = keyboardSkinLookup == -1 ? "default" : challengeLogic.getChallenges().get(keyboardSkinLookup).getSkin();
        //if it's an img we have to set or just a color
        if (keyboardSkinExtention.equals("rainbow")) {
            loadKeyboardImg(key, keyboardSkinExtention);
        } else {
            loadKeyboardColor(key, keyboardSkinExtention);
        }
    }

    public void loadKeyboardColor(Button key, String skinExtension) {
        String keyboardSkin = skinExtension + "_" + "btn";
        System.out.println("keyboardskin: " + keyboardSkin);
        //finding the int for the specific keyboardSkin
        int color = getResources().getIdentifier(keyboardSkin, "color", getPackageName());
        key.setBackgroundTintList(ColorStateList.valueOf(getColor(color)));
    }

    public void loadKeyboardImg(Button key, String imgToFind) {
        key.setBackgroundResource(getResources().getIdentifier(imgToFind, "drawable", getPackageName()));
    }

    public String loadManSkin(int[] skinList) {
        //find index in enum "array"
        int manSkinLookup = skinList[ChallengeObject.SkinGroup.MAN_SKIN.ordinal()];
        System.out.println("manskinlookup: " + manSkinLookup);
        if (manSkinLookup == -1) {
            return "default";
        }
        //take the skin out of the specific challenge
        System.out.println("få challenges: " + challengeLogic.getChallenges().get(manSkinLookup));
        System.out.println("loadman: " + challengeLogic.getChallenges().get(manSkinLookup).getSkin());
        return challengeLogic.getChallenges().get(manSkinLookup).getSkin();
    }

    private int loadImg(String imgName, int imgNum, String skin) {
        String imgToShow = imgName + imgNum + "_" + skin;
        //gets the exact id for the img
        return getResources().getIdentifier(imgToShow, "drawable", getPackageName());
    }

    /**
     * This filter the list of words in "muligeOrd" to be larger than minimumSize and less than maximumSize
     *
     * @param minimumSize the minimum length of a word
     * @param maximumSize the maximum length of a word
     */
    private void filterWordFromDr(int minimumSize, int maximumSize) {
        int arrSize = logik.muligeOrd.size();
        //need to be called from the last element to the first because the size getting smaller when we remove
        for (int i = arrSize - 1; i >= 0; i--) {
            if (logik.muligeOrd.get(i).length() < minimumSize || logik.muligeOrd.get(i).length() > maximumSize) {
                logik.muligeOrd.remove(i);
            }
        }
        //we call "nulstil()" to get a word from our filtered list
        logik.nulstil();
        System.out.println("start str: " + arrSize + "\nsorteret str: " + logik.muligeOrd.size());
        System.out.println("filtreret muligeord= " + logik.muligeOrd);
    }

    /**
     * counting up letters and putting them in an array. "a" is index 0 and "å" is index 28 (29 letters in total)
     * @param letter the letter being clicked on
     */
    public void countLetters(String letter) {
        switch (letter) {
            case "æ":
                guessedLetters[26] += 1;
                break;
            case "ø":
                guessedLetters[27] += 1;
                break;
            case "å":
                guessedLetters[28] += 1;
                break;
            default:
                //97 is the offset from the ascii table. "a" is 97.
                int letterInArray = (int) (letter.charAt(0)) - 97;
                guessedLetters[letterInArray] += 1;
        }
        System.out.println("letterArray: " + Arrays.toString(guessedLetters));
        System.out.println("længde i game: " + this.guessedLetters.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //check if the user cancel the game. This is sat to false in "gameEnded" (which is the proper way to end the game)
        if (isGameCanceled) {
            if (firstLetterGuessed) {
                timePassed = SystemClock.elapsedRealtime() - timer.getBase();
                int wins = 0;
                int losses = 1;

                timer.stop();
                System.out.println("on pause. Tid er: " + timePassed);

                statLogic.updateStats(statLogic.getGameStats(this), statLogic.getGAME_OBJECT_KEY(), wins, losses, rightGuesses, wrongGuesses, timePassed, guessedLetters, this);

                checkChallenges(this);
            }
        }
    }

    public void gameEnded() {
        timer.stop();
        isGameCanceled = false;
        for (Button b : keys) {
            b.setClickable(false);
        }

        int wins = 0;
        int losses = 0;

        if (isGameWon) wins++;
        else losses++;
        statLogic.updateStats(statLogic.getGameStats(this), statLogic.getGAME_OBJECT_KEY(), wins, losses, rightGuesses, wrongGuesses, timePassed, guessedLetters, this);

        checkChallenges(this);

        Intent i = new Intent(this, GameFinishAct.class);
        //send extra data over to intent
        i.putExtra("winner", isGameWon);
        i.putExtra("guesses", logik.getBrugteBogstaver().size());
        i.putExtra("word", logik.getOrdet());
        //we only send the time if the game were won
        if (isGameWon) i.putExtra("time", timePassed);
        System.out.println("winner er: " + isGameWon);
        startActivity(i);
        //when we go to winner/loser activity we delete the game from backstack
        finish();
    }

    private void checkChallenges(Context context) {
        int winsUnder20 = challengeLogic.checkWinsUnder20(context, timePassed, isGameWon, challengeLogic.getWINS_UNDER_20_LIMIT());
        int inARow = challengeLogic.checkInARow(context, isGameWon, challengeLogic.getIN_A_ROW_LIMIT());
        int noMistake = challengeLogic.checkNoMistake(context, wrongGuesses, challengeLogic.getNO_MISTAKE_LIMIT());
        int firstLetterCorrect = challengeLogic.checkFirstLetterCorrect(context, isFirstLetterCorrect, challengeLogic.getFIRST_LETTER_CORRECT_LIMIT());

        challengeLogic.updateChallengeProgression(challengeLogic.getChallengeProgression(context), challengeLogic.getCHALLENGE_KEY(), winsUnder20, inARow, noMistake, firstLetterCorrect, context);
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

    public void getWordsFromDr() {
        //gets word from dr.dk. Uses the same Galgelogik logik as above (global variable)
        new AsyncTask() {

            // TODO: 11-11-2019 lav en cache til ordene, så den ikke skal hente hver gang
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    logik.hentOrdFraDr();
                    filterWordFromDr(5, 12);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return logik.getSynligtOrd();
            }

            @Override
            protected void onPostExecute(Object o) {
                //using the "getSynligtOrd" from return above
                visibleWord = o.toString();
                guessedWord.setText(visibleWord);
                logik.logStatus();
                //after the words are loaded, buttons are sat to clickable
                for (Button btn : keys) btn.setClickable(true);
            }
        }.execute();
    }






}