package com.example.galgespil.GameStatistic;


public class StatDisplayObject {
    String statName;
    double statInfo;
    String statHelpInfo;

    public StatDisplayObject(String statName, double statInfo, String statHelpInfo) {
        this.statName = statName;
        this.statInfo = statInfo;
        this.statHelpInfo = statHelpInfo;
    }

    public StatDisplayObject(String statName) {
        this.statName = statName;
    }

    public StatDisplayObject(double statInfo, String statHelpInfo) {
        this.statInfo = statInfo;
        this.statHelpInfo = statHelpInfo;
    }
}
