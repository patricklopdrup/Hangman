package com.example.galgespil.Highscore;

import androidx.annotation.NonNull;

public class HighscoreObject implements Comparable<HighscoreObject> {
    private long time;
    private int guesses;
    private String word;

    public HighscoreObject(long time, int guesses, String word) {
        this.time = time;
        this.guesses = guesses;
        this.word = word;
    }
    public HighscoreObject() {}

    @NonNull
    @Override
    public String toString() {
        return word + " guessed with " + guesses + "guesses in " + (float)(time/1000) + " sec";
    }

    public long getTime() {
        return time;
    }
    public int getGuesses() {
        return guesses;
    }
    public String getWord() {
        return word;
    }

    @Override
    public int compareTo(HighscoreObject highscoreObject) {
        return (int)(this.time - highscoreObject.time);
    }
}
