package com.example.galgespil.GameStatistic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GameStatLogic {
    GameStatObject stats = new GameStatObject();
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

    public double winLossRatio(Context context) {
        GameStatObject obj = getGameStats(context);
        double wins = (double)obj.getWins();
        double losses = (double)obj.getLosses();
        return wins / losses;
    }

    public int totalGuesses(Context context) {
        GameStatObject obj = getGameStats(context);
        return obj.getRightGuesses() + obj.getWrongGuesses();
    }

    public long totalGameTime(Context context) {
        GameStatObject obj = getGameStats(context);
        return obj.getGameTime();
    }

    public double rightWrongRatio(Context context) {
        GameStatObject obj = getGameStats(context);
        double right = (double)obj.getRightGuesses();
        double wrong = (double)obj.getWrongGuesses();
        return right / wrong;
    }

    public double avgRightGuesses(Context context) {
        GameStatObject obj = getGameStats(context);
        double right = (double)obj.getRightGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        return right / games;
    }

    public double avgWrongGuesses(Context context) {
        GameStatObject obj = getGameStats(context);
        double wrong = (double)obj.getWrongGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        return wrong / games;
    }

    public String getGAME_OBJECT_KEY() {
        return this.GAME_OBJECT_KEY;
    }

}
