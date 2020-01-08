package com.example.galgespil.Challenges;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChallengeLogic {

     //In the future this can be taking from a Google sheets
    /**
     * wins under 20
     */
    private final int WINS_UNDER_20_LIMIT = 5;
    private final String WINS_UNDER_20_NAME = WINS_UNDER_20_LIMIT + " hurtige";
    private final String WINS_UNDER_20_DESC = "Gæt ordet på under 20 sekunder " + WINS_UNDER_20_LIMIT + " gange";
    private final String WINS_UNDER_20_SKIN_DESC = "Lilla knapper";
    private final String WINS_UNDER_20_SKIN = "purple";
    ChallengeObject winsUnder20 = new ChallengeObject(WINS_UNDER_20_NAME, WINS_UNDER_20_DESC, WINS_UNDER_20_LIMIT, WINS_UNDER_20_SKIN_DESC, WINS_UNDER_20_SKIN, ChallengeObject.SkinGroup.KEYBOARD_SKIN);

    public int checkWinsUnder20(Context context, long time, boolean gameWon, int challengeLimit) {
        int winsUnder20 = getChallengeProgression(context).getWinsUnder20();
        if(winsUnder20 >= challengeLimit) return challengeLimit;
        //has to be a win
        if(time < 20000 && gameWon) return winsUnder20 + 1;
        else return winsUnder20;
    }

    /**
     * in a row
     */
    private final int IN_A_ROW_LIMIT = 3;
    private final String IN_A_ROW_NAME = IN_A_ROW_LIMIT + " på stripe";
    private final String IN_A_ROW_DESC = "Vind " + IN_A_ROW_LIMIT + " spil i træk";
    private final String IN_A_ROW_SKIN_DESC = "Regnbue knapper";
    private final String IN_A_ROW_SKIN = "rainbow";
    ChallengeObject inARow = new ChallengeObject(IN_A_ROW_NAME, IN_A_ROW_DESC, IN_A_ROW_LIMIT, IN_A_ROW_SKIN_DESC, IN_A_ROW_SKIN, ChallengeObject.SkinGroup.KEYBOARD_SKIN);

    public int checkInARow(Context context, boolean gameWon, int challengeLimit) {
        int inARow = getChallengeProgression(context).getInARow();
        if(inARow >= challengeLimit) return challengeLimit;
        //if game won we add on to counter otherwise reset it
        if(gameWon) return inARow + 1;
        else return 0;
    }

    /**
     * no mistake
     */
    private final int NO_MISTAKE_LIMIT = 1;
    private final String NO_MISTAKE_NAME = "Fejlfri";
    private final String NO_MISTAKE_DESC = "Gæt " + NO_MISTAKE_LIMIT + " ord uden fejl";
    private final String NO_MISTAKE_SKIN_DESC = "Grøn mand";
    private final String NO_MISTAKE_SKIN = "green";
    ChallengeObject noMistake = new ChallengeObject(NO_MISTAKE_NAME, NO_MISTAKE_DESC, NO_MISTAKE_LIMIT, NO_MISTAKE_SKIN_DESC, NO_MISTAKE_SKIN, ChallengeObject.SkinGroup.MAN_SKIN);

    public int checkNoMistake(Context context, int wrongGuesses, int challengeLimit) {
        int noMistake = getChallengeProgression(context).getNoMistake();
        if(noMistake >= challengeLimit) return challengeLimit;
        if(wrongGuesses == 0) return noMistake + 1;
        else return noMistake;
    }

    /**
     * first letter
     */
    private final int FIRST_LETTER_CORRECT_LIMIT = 20;
    private final String FIRST_LETTER_CORRECT_NAME = "Første gang er lykkens gang";
    private final String FIRST_LETTER_CORRECT_DESC = "Gæt det første bogstav rigtigt " + FIRST_LETTER_CORRECT_LIMIT + " gange";
    private final String FIRST_LETTER_CORRECT_SKIN_DESC = "Lilla mand";
    private final String FIRST_LETTER_CORRECT_SKIN = "purple";
    ChallengeObject firstLetter = new ChallengeObject(FIRST_LETTER_CORRECT_NAME, FIRST_LETTER_CORRECT_DESC, FIRST_LETTER_CORRECT_LIMIT, FIRST_LETTER_CORRECT_SKIN_DESC, FIRST_LETTER_CORRECT_SKIN, ChallengeObject.SkinGroup.MAN_SKIN);

    public int checkFirstLetterCorrect(Context context, boolean isFirstLetterCorrect, int challengeLimit) {
        int firstLetterCount = getChallengeProgression(context).getFirstLetterCorrect();
        if(firstLetterCount >= challengeLimit) return challengeLimit;
        //don't have to be a win
        if(isFirstLetterCorrect) return firstLetterCount + 1;
        else return firstLetterCount;
    }

    //array for holding challenges
    private ArrayList<ChallengeObject> challengeArray;

    public ArrayList<ChallengeObject> getChallenges() {
        challengeArray = new ArrayList<>();
        challengeArray.add(winsUnder20);
        challengeArray.add(inARow);
        challengeArray.add(noMistake);
        challengeArray.add(firstLetter);

        return challengeArray;
    }

    private final String CHALLENGE_KEY = "myChallengeKey";

    public ChallengeProgressCount getChallengeProgression(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(CHALLENGE_KEY, "empty");
        System.out.println("challenge json: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<ChallengeProgressCount>(){}.getType();
        if(json.equals("empty"))
            return new ChallengeProgressCount();
        return gson.fromJson(json, type);
    }

    public void updateChallengeProgression(ChallengeProgressCount challengeProgressCount, String key, int winsUnder20, int inARow, int noMistake, int firstLetterCorrect, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        setChallengeProgression(challengeProgressCount, winsUnder20, inARow, noMistake, firstLetterCorrect);
        Gson gson = new Gson();
        String json = gson.toJson(challengeProgressCount);
        System.out.println("challenge gem: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private void setChallengeProgression(ChallengeProgressCount challObj, int winsUnder20, int inARow, int noMistake, int firstLetterCorrect) {
        challObj.setWinsUnder20(winsUnder20);
        challObj.setInARow(inARow);
        challObj.setNoMistake(noMistake);
        challObj.setFirstLetterCorrect(firstLetterCorrect);
    }

    public ArrayList<Integer> getProgressionList(Context context) {
        ArrayList<Integer> progressionList = new ArrayList<>();
        ChallengeProgressCount obj = getChallengeProgression(context);
        progressionList.add(obj.getWinsUnder20());
        progressionList.add(obj.getInARow());
        progressionList.add(obj.getNoMistake());
        progressionList.add(obj.getFirstLetterCorrect());
        return progressionList;
    }

    /**
     * to save and get skin list from sharedprefs
     */
    private final String SKIN_KEY = "mySkinKey";

    public int[] getChosenSkinList(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, "empty");
        System.out.println("skin json: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<int[]>(){}.getType();
        if(json.equals("empty"))
            return new int[0];
        return gson.fromJson(json, type);
    }

    public void saveSkinsToSharedPrefs(Context context, String key, int[] skinList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(skinList);
        System.out.println("Skin gem: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    //getters for keys
    public String getSKIN_KEY() {
        return SKIN_KEY;
    }
    public String getCHALLENGE_KEY() {
        return CHALLENGE_KEY;
    }
    //getters
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
