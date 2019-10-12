package com.example.galgespil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class HighscoreLogic {

    private List<Long> highscoreList = new ArrayList<>();
    private static boolean isSorted = false;
    private String highscoreKey = "highscore";

    public void addScore(long score, String key, Context context) {
        List<Long> temp = getSavedHighscore(key, context);
        temp.add(score);
        saveHighscore(temp, key, context);
        isSorted = false;
    }

    public List<Long> getSortedHighscoreList(String key, Context context) {
        List<Long> listToReturn = getSavedHighscore(key, context);
        if(!isSorted) {
            Collections.sort(listToReturn);
            isSorted = true;
        }
        return listToReturn;
    }

    private void saveHighscore(List<Long> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println("Her er min json: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private List<Long> getSavedHighscore(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, "not found");
        Type type = new TypeToken<List<Long>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public String getHighscoreKey() {
        return highscoreKey;
    }

    //for testing only. To overwrite the highscorelist with nothing. This empty the list completely
    private boolean iWantToEmptyHighscoreList = true;

    public void emptyHighscoreList(String key, Context context) {
        if(iWantToEmptyHighscoreList) {
            List<Long> emptyList = new ArrayList<>();
            saveHighscore(emptyList, key, context);
        } else {
            System.out.println("Private boolean has to be true");
        }

    }

}
