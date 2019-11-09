package com.example.galgespil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class HighscoreLogic {

    private String highscoreKey = "highscore";
    private HighscoreObject highscoreObject = new HighscoreObject();

    public void addScore(long time, int guesses, String word, String key, Context context) {
        List<HighscoreObject> temp = getSavedHighscore(key, context);
        temp.add(new HighscoreObject(time, guesses, word));
        saveHighscore(temp, key, context);
    }

    public List<HighscoreObject> getSortedHighscoreList(String key, Context context) {
        List<HighscoreObject> listToReturn = getSavedHighscore(key, context);
        Collections.sort(listToReturn);
        return listToReturn;
    }

    private void saveHighscore(List<HighscoreObject> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println("Her er min json: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private List<HighscoreObject> getSavedHighscore(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, "empty");
        System.out.println("json = " + json);
        Type type = new TypeToken<List<HighscoreObject>>(){}.getType();
        //check if array is empty and return empty List
        if(json.equals("empty"))
            return new ArrayList<>();
        return gson.fromJson(json, type);
    }

    public String getHighscoreKey() {
        return highscoreKey;
    }





    //for testing only. To overwrite the highscorelist with nothing. This empty the list completely
    private boolean iWantToEmptyHighscoreList = false;

    public void emptyHighscoreList(String key, Context context) {
        if(iWantToEmptyHighscoreList) {
            List<HighscoreObject> emptyList = new ArrayList<>();
            saveHighscore(emptyList, key, context);
        } else {
            System.out.println("Private boolean has to be true");
        }

    }

}