package com.example.galgespil;

import java.util.Arrays;

public class GameStatObject {
    private int wins = 0;
    private int losses = 0;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    private long gameTime = 0;
    private int[] guessedLetters = new int[29];

    public GameStatObject() {}

    public GameStatObject(int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime, int[] guessedLetters) {
        this.wins = wins;
        this.losses = losses;
        this.rightGuesses = rightGuesses;
        this.wrongGuesses = wrongGuesses;
        this.gameTime = gameTime;
        this.guessedLetters = guessedLetters;
    }

    public void updateGuessedLetters(int[] guessedLetters) {
        System.out.println("letters før: " + Arrays.toString(this.guessedLetters));
        System.out.println("længde i object: " + this.guessedLetters.length);
        for(int i = 0; i < this.guessedLetters.length; i++) {
           this.guessedLetters[i] += guessedLetters[i];
        }
        System.out.println("letters efter: " + Arrays.toString(this.guessedLetters));
    }

    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }
    public int getLosses() {
        return losses;
    }
    public void setLosses(int losses) {
        this.losses = losses;
    }
    public int getRightGuesses() {
        return rightGuesses;
    }
    public void setRightGuesses(int rightGuesses) {
        this.rightGuesses = rightGuesses;
    }
    public int getWrongGuesses() {
        return wrongGuesses;
    }
    public void setWrongGuesses(int wrongGuesses) {
        this.wrongGuesses = wrongGuesses;
    }
    public long getGameTime() {
        return gameTime;
    }
    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }
    public int[] getGuessedLetters() {
        return guessedLetters;
    }
    public void setGuessedLetters(int[] guessedLetters) {
        this.guessedLetters = guessedLetters;
    }
}
