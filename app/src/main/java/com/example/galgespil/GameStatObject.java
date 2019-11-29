package com.example.galgespil;

public class GameStatObject {
    private int wins = 0;
    private int losses = 0;
    private int rightGuesses = 0;
    private int wrongGuesses = 0;
    private long gameTime = 0;

    public GameStatObject() {}

    public GameStatObject(int wins, int losses, int rightGuesses, int wrongGuesses, long gameTime) {
        this.wins = wins;
        this.losses = losses;
        this.rightGuesses = rightGuesses;
        this.wrongGuesses = wrongGuesses;
        this.gameTime = gameTime;
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
}
