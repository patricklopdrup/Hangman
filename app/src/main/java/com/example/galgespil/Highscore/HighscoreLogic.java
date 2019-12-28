package com.example.galgespil.Highscore;

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

    private String highscoreKey = "highscore";
    private HighscoreObject highscoreObject = new HighscoreObject();

    /**
     * This add a score and call "saveHighscore" with a List
     * @param time time in millisec
     * @param guesses amount of guesses
     * @param word the specific word that were guessed
     * @param key key to get the highscore from shared preference manager
     * @param context the context for the state of the program. Simply write "this" or "getContext()"
     */
    public void addScore(long time, int guesses, String word, String key, Context context) {
        List<HighscoreObject> temp = getSavedHighscore(key, context);
        temp.add(new HighscoreObject(time, guesses, word));
        saveHighscore(temp, key, context);
    }

    /**
     * This sorts the highscoreList with the compareTo method in HighscoreObject.java
     * @param key the key to get the highscore from shared preference manager
     * @param context the context for the state of the program. Simply write "this" or "getContext()"
     * @return A List of HighscoreObjects that is sorted
     */
    public List<HighscoreObject> getSortedHighscoreList(String key, Context context) {
        List<HighscoreObject> listToReturn = getSavedHighscore(key, context);
        Collections.sort(listToReturn);
        return listToReturn;
    }

    /**
     * This saves the highscoreList in shared preference manager. It is being called from "addScore"
     * @param list the highscorelist it is going to save
     * @param key the key it stores it with
     * @param context the context for the state of the program
     */
    private void saveHighscore(List<HighscoreObject> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println("Her er min json: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    /**
     * This gets the highscore from shared preference manager and returns it (not sorted!)
     * @param key the key the highscore is saved with
     * @param context the context for the state of the program
     * @return a list of HighscoreObject
     */
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