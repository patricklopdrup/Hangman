package com.example.galgespil.Challenges;

public class ChallengeObject {
    private int winsUnder20;
    private int inARow;
    private int noMistake;
    private int firstLetterCorrect;

    public ChallengeObject() {}

    public ChallengeObject(int winsUnder20, int inARow, int noMistake, int firstLetterCorrect, boolean isWinsUnder20, boolean isInARow, boolean isNoMistake, boolean isFirstLetterCorrect) {
        this.winsUnder20 = winsUnder20;
        this.inARow = inARow;
        this.noMistake = noMistake;
        this.firstLetterCorrect = firstLetterCorrect;
    }

    public int getWinsUnder20() {
        return winsUnder20;
    }
    public void setWinsUnder20(int winsUnder20) {
        this.winsUnder20 = winsUnder20;
    }
    public int getInARow() {
        return inARow;
    }
    public void setInARow(int inARow) {
        this.inARow = inARow;
    }
    public int getNoMistake() {
        return noMistake;
    }
    public void setNoMistake(int noMistake) {
        this.noMistake = noMistake;
    }
    public int getFirstLetterCorrect() {
        return firstLetterCorrect;
    }
    public void setFirstLetterCorrect(int firstLetterCorrect) {
        this.firstLetterCorrect = firstLetterCorrect;
    }
}
