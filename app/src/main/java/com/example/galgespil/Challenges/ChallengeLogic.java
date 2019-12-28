package com.example.galgespil.Challenges;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ChallengeLogic {
    private final String CHALLENGE_KEY = "myChallengeKey";
    private final int WINS_UNDER_20_LIMIT = 5;
    private final int IN_A_ROW_LIMIT = 3;
    private final int NO_MISTAKE_LIMIT = 3;
    private final int FIRST_LETTER_CORRECT_LIMIT = 10;

    public ChallengeObject getChallengeProgression(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(CHALLENGE_KEY, "empty");
        System.out.println("challenge json: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<ChallengeObject>(){}.getType();
        if(json.equals("empty"))
            return new ChallengeObject();
        return gson.fromJson(json, type);
    }

    public void updateChallengeProgression(ChallengeObject challengeObject, String key, int winsUnder20, int inARow, int noMistake, int firstLetterCorrect, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        setChallengeProgression(challengeObject, winsUnder20, inARow, noMistake, firstLetterCorrect);
        Gson gson = new Gson();
        String json = gson.toJson(challengeObject);
        System.out.println("challenge gem: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private void setChallengeProgression(ChallengeObject challObj, int winsUnder20, int inARow, int noMistake, int firstLetterCorrect) {
        challObj.setWinsUnder20(winsUnder20);
        challObj.setInARow(inARow);
        challObj.setNoMistake(noMistake);
        challObj.setFirstLetterCorrect(firstLetterCorrect);
    }

    public int checkWinsUnder20(Context context, long time, boolean gameWon, int challengeLimit) {
        int winsUnder20 = getChallengeProgression(context).getWinsUnder20();
        if(winsUnder20 >= challengeLimit) return challengeLimit;
        if(time < 20000 && gameWon) return winsUnder20 + 1;
        else return winsUnder20;
    }

    public int checkInARow(Context context, boolean gameWon, int challengeLimit) {
        int inARow = getChallengeProgression(context).getInARow();
        if(inARow >= challengeLimit) return challengeLimit;
        if(gameWon) return inARow + 1;
        else return 0;
    }

    public int checkNoMistake(Context context, int wrongGuesses, int challengeLimit) {
        int noMistake = getChallengeProgression(context).getNoMistake();
        if(noMistake >= challengeLimit) return challengeLimit;
        if(wrongGuesses == 0) return noMistake + 1;
        else return noMistake;
    }

    public int checkFirstLetterCorrect(Context context, boolean isFirstLetterCorrect, boolean gameWon, int challengeLimit) {
        int firstLetterCount = getChallengeProgression(context).getFirstLetterCorrect();
        if(firstLetterCount >= challengeLimit) return challengeLimit;
        if(isFirstLetterCorrect && gameWon) return firstLetterCount + 1;
        else return firstLetterCount;
    }

    public String getCHALLENGE_KEY() {
        return CHALLENGE_KEY;
    }
    public int getWINS_UNDER_20_LIMIT() {
        return WINS_UNDER_20_LIMIT;
    }
    public int getIN_A_ROW_LIMIT() {
        return IN_A_ROW_LIMIT;
    }
    public int getNO_MISTAKE_LIMIT() {
        return NO_MISTAKE_LIMIT;
    }
    public int getFIRST_LETTER_CORRECT_LIMIT() {
        return FIRST_LETTER_CORRECT_LIMIT;
    }
}
