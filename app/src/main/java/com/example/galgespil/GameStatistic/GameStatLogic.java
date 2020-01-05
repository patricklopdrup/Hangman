package com.example.galgespil.GameStatistic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameStatLogic {
    private List<StatDisplayObject> statObjectList = new ArrayList<>();
    private final String GAME_OBJECT_KEY = "myGameStatKey";

    public GameStatObject getGameStats(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(GAME_OBJECT_KEY, "empty");
        System.out.println("json hentet: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<GameStatObject>(){}.getType();
        if(json.equals("empty"))
            return new GameStatObject();
        return gson.fromJson(json, type);
    }

    public void updateStats(GameStatObject gameStatObject, String key, int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime, int[] guessedLetters, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        setStats(gameStatObject, wins, losses, rightGuesses, wrongGuesses, gameTime, guessedLetters);
        Gson gson = new Gson();
        String json = gson.toJson(gameStatObject);
        System.out.println("json gem: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private void setStats(GameStatObject gso, int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime, int[] guessedLetters) {
        gso.setWins(gso.getWins() + wins);
        gso.setLosses(gso.getLosses() + losses);
        gso.setRightGuesses(gso.getRightGuesses() + rightGuesses);
        gso.setWrongGuesses(gso.getWrongGuesses() + wrongGuesses);
        gso.setGameTime(gso.getGameTime() + gameTime);
        gso.updateGuessedLetters(guessedLetters);
    }

//    public List<StatDisplayObject> getStats(Context context) {
//        GameStatObject obj = getGameStats(context);
//        statObjectList.add(new StatDisplayObject("Tid spillet", totalGameTime(obj)));
//
//        statObjectList.add(new StatDisplayObject("Antal spil spillet", totalGames(obj)));
//        statObjectList.add(new StatDisplayObject("Vind/tab forhold", winLossRatio(obj)));
//        statObjectList.add(new StatDisplayObject("Top 3 mest brugte bogstaver", -1.0, mostUsedLetters(obj, 3)));
//
//        return statObjectList;
//    }

    public List<StatDisplayObject> getStatNames() {
        statObjectList.add(new StatDisplayObject("Tid spillet"));

        statObjectList.add(new StatDisplayObject("Antal spil spillet"));
        statObjectList.add(new StatDisplayObject("Vind/tab forhold"));
        statObjectList.add(new StatDisplayObject("Top 3 mest brugte bogstaver"));

        return statObjectList;
    }

    private List<StatDisplayObject> statValues = new ArrayList<>();
    public List<StatDisplayObject> getStatValues(Context context) {
        GameStatObject obj = getGameStats(context);

        statValues.add(new StatDisplayObject(totalGameTime(obj), null));

        statValues.add(new StatDisplayObject(totalGames(obj), "spil"));
        statValues.add(new StatDisplayObject(winLossRatio(obj), null));
        statValues.add(new StatDisplayObject(-1.0, mostUsedLetters(obj, 3)));

        return statValues;
    }

    public String mostUsedLetters(GameStatObject obj, int numOfLetters) {
        int asciiOffset = 97;
        StringBuilder numList = new StringBuilder();
        int[] letters = obj.getGuessedLetters();
        for(int i = 0; i < numOfLetters; i++) {
            //adding commas so it's readable
            if(i > 0) numList.append(", ");
            //resetting values for the biggest number to find
            int temp = -1;
            int tempIndex = -1;
            //loop through all the letters and finding the biggest value. Saving both value and index
            for(int j = 0; j < letters.length; j++) {
                if(letters[j] > temp) {
                    temp = letters[j];
                    tempIndex = j;
                }
            }
            //setting the letter we found to -1 so we don't find it again
            letters[tempIndex] = -1;
            //making a string of the numbers and values. Fx: "e: 41, r: 38, t: 38"
            numList.append((char)(tempIndex + asciiOffset) + ": " + temp);
        }
        return numList.toString();
    }


    public double totalGames(GameStatObject obj) {
        double wins = (double)obj.getWins();
        double losses = (double)obj.getLosses();
        return wins + losses;
    }

    public double winLossRatio(GameStatObject obj) {
        double wins = (double)obj.getWins();
        double losses = (double)obj.getLosses();
        return wins / losses;
    }

    public int totalGuesses(GameStatObject obj) {
        return obj.getRightGuesses() + obj.getWrongGuesses();
    }

    public long totalGameTime(GameStatObject obj) {
        return obj.getGameTime();

    }


//    long timeInMs = highscoreSorted.get(position).getTime();
//    float timeInSec = ((float)timeInMs / 1000);
//    //if time is more than 60 sec, we write fx "1 min 14 sek"
//            if(timeInSec >= 60.0) {
//        timeUsed.setText(getString(R.string.show_time_used_min, (int)(timeInSec)/60, (int)(timeInSec)%60));

    public double rightWrongRatio(GameStatObject obj) {
        double right = (double)obj.getRightGuesses();
        double wrong = (double)obj.getWrongGuesses();
        return right / wrong;
    }

    public double avgRightGuesses(GameStatObject obj) {
        double right = (double)obj.getRightGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        return right / games;
    }

    public double avgWrongGuesses(GameStatObject obj) {
        double wrong = (double)obj.getWrongGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        return wrong / games;
    }

    public String getGAME_OBJECT_KEY() {
        return this.GAME_OBJECT_KEY;
    }

}
