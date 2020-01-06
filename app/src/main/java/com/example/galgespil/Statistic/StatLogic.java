package com.example.galgespil.Statistic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StatLogic {
    private List<StatDisplayObject> statObjectList = new ArrayList<>();
    private final String GAME_OBJECT_KEY = "myGameStatKey";

    public StatObject getGameStats(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(GAME_OBJECT_KEY, "empty");
        System.out.println("json hentet: " + json);
        Gson gson = new Gson();
        Type type = new TypeToken<StatObject>(){}.getType();
        if(json.equals("empty"))
            return new StatObject();
        return gson.fromJson(json, type);
    }

    public void updateStats(StatObject statObject, String key, int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime, int[] guessedLetters, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        setStats(statObject, wins, losses, rightGuesses, wrongGuesses, gameTime, guessedLetters);
        Gson gson = new Gson();
        String json = gson.toJson(statObject);
        System.out.println("json gem: " + json);
        editor.putString(key, json);
        editor.commit();
    }

    private void setStats(StatObject gso, int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime, int[] guessedLetters) {
        gso.setWins(gso.getWins() + wins);
        gso.setLosses(gso.getLosses() + losses);
        gso.setRightGuesses(gso.getRightGuesses() + rightGuesses);
        gso.setWrongGuesses(gso.getWrongGuesses() + wrongGuesses);
        gso.setGameTime(gso.getGameTime() + gameTime);
        gso.updateGuessedLetters(guessedLetters);
    }

    public List<StatDisplayObject> getStatNames() {
        statObjectList.add(new StatDisplayObject("Tid spillet"));
        statObjectList.add(new StatDisplayObject("Antal spil spillet"));
        statObjectList.add(new StatDisplayObject("Spil vundet og tabt"));
        statObjectList.add(new StatDisplayObject("Vind/tab forhold"));
        statObjectList.add(new StatDisplayObject("3 mest brugte bogstaver"));
        statObjectList.add(new StatDisplayObject("Antal gæt i alt"));
        statObjectList.add(new StatDisplayObject("Rigtige og forkerte gæt"));
        statObjectList.add(new StatDisplayObject("Rigtig/forkert gæt forhold"));
        statObjectList.add(new StatDisplayObject("Gennemsnitlige rigtige gæt"));
        statObjectList.add(new StatDisplayObject("Gennemsnitlige forkerte gæt"));
        statObjectList.add(new StatDisplayObject("Spil pr. minut"));
        statObjectList.add(new StatDisplayObject("Gæt pr. minut"));


        return statObjectList;
    }

    private List<StatDisplayObject> statValues = new ArrayList<>();
    public List<StatDisplayObject> getStatValues(Context context) {
        StatObject obj = getGameStats(context);

        statValues.add(new StatDisplayObject(-1.0, totalGameTime(obj))); //tid spillet
        statValues.add(new StatDisplayObject(-1.0, totalGames(obj))); //antal spil
        statValues.add(new StatDisplayObject(-1.0, winsAndLoses(obj))); //spil vundet/tabt
        statValues.add(new StatDisplayObject(winLossRatio(obj), null)); //vind/tab forhold
        statValues.add(new StatDisplayObject(-1.0, mostUsedLetters(obj, 3))); //3 mest brugte
        statValues.add(new StatDisplayObject(-1.0, totalGuesses(obj))); //gæt i alt
        statValues.add(new StatDisplayObject(-1.0, rightAndWrongGuesses(obj))); //rigtige/forkerte gæt
        statValues.add(new StatDisplayObject(rightWrongRatio(obj), null)); //rigtige/forkerte gæt forhold
        statValues.add(new StatDisplayObject(avgRightGuesses(obj), "pr. spil")); //avg rigtige
        statValues.add(new StatDisplayObject(avgWrongGuesses(obj), "pr. spil")); //avg forkerte
        statValues.add(new StatDisplayObject(gamePerMin(obj), null)); //spil pr min
        statValues.add(new StatDisplayObject(guessesPrMin(obj), null)); //gæt pr min

        return statValues;
    }

    public String mostUsedLetters(StatObject obj, int numOfLetters) {
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
            char letter;
            System.out.println("tempIndex er: " + tempIndex);
            switch (tempIndex) {
                case 28: letter = 'å'; break;
                case 27: letter = 'ø'; break;
                case 26: letter = 'æ'; break;
                default: letter = (char)(tempIndex + asciiOffset);
            }
            numList.append(letter + ": " + temp);
        }
        return numList.toString();
    }

    public String rightAndWrongGuesses(StatObject obj) {
        return obj.getRightGuesses() + " rigtige og " + obj.getWrongGuesses() + " forkerte";
    }

    public String winsAndLoses(StatObject obj) {
        return String.format("%d vundet og %d tabt", obj.getWins(), obj.getLosses());
    }

    public double guessesPrMin(StatObject obj) {
        long time = obj.getGameTime();
        int guesses = obj.getRightGuesses() + obj.getWrongGuesses();
        double timeInMin = (double)time / 1000 / 60;

        double ratio = (double)guesses / timeInMin;
        return round2Decimal(ratio);
    }

    public double gamePerMin(StatObject obj) {
        long time = obj.getGameTime();
        int games = obj.getWins() + obj.getLosses();
        double timeInMin = (double)time / 1000 / 60;

        double ratio = (double)games / timeInMin;
        return round2Decimal(ratio);
    }

    public String totalGames(StatObject obj) {
        return (obj.getWins() + obj.getLosses()) + " spil";
    }

    public double winLossRatio(StatObject obj) {
        double wins = (double)obj.getWins();
        double losses = (double)obj.getLosses();
        double ratio = wins / losses;

        return round2Decimal(ratio);
    }

    //rounding to 2 decimal places and returning
    private double round2Decimal(double num) {
        num *= 100;
        num = Math.round(num);
        return num / 100;
    }

    public String totalGuesses(StatObject obj) {
        return (obj.getRightGuesses() + obj.getWrongGuesses()) + " gæt";
    }

    public String totalGameTime(StatObject obj) {
        long milliseconds = obj.getGameTime();
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);

        return hours + " timer " + minutes + " min " + seconds + " sek";
    }

    public double rightWrongRatio(StatObject obj) {
        double right = (double)obj.getRightGuesses();
        double wrong = (double)obj.getWrongGuesses();
        double ratio = right / wrong;
        return round2Decimal(ratio);
    }

    public double avgRightGuesses(StatObject obj) {
        double right = (double)obj.getRightGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        double avg = right / games;
        return round2Decimal(avg);
    }

    public double avgWrongGuesses(StatObject obj) {
        double wrong = (double)obj.getWrongGuesses();
        double games = (double)(obj.getWins() + obj.getLosses());
        double avg = wrong / games;
        return round2Decimal(avg);
    }

    public String getGAME_OBJECT_KEY() {
        return this.GAME_OBJECT_KEY;
    }

}
