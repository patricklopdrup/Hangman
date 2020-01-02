package com.example.galgespil.Challenges;

public class ChallengeObject {
    private String name;
    private String desc;
    private int limit;
    private String skinDesc;
    private String skin;
    private boolean isClicked;
    private boolean isSkinChosen;
    private SkinGroup skinGroup;

    public enum SkinGroup {
        KEYBOARD_SKIN, MAN_SKIN
    }

    public ChallengeObject(String name, String desc, int limit, String skinDesc, String skin, SkinGroup skinGroup) {
        this.name = name;
        this.desc = desc;
        this.limit = limit;
        this.skinDesc = skinDesc;
        this.skin = skin;
        this.skinGroup = skinGroup;
        this.isClicked = false;
        this.isSkinChosen = false;
    }

    public ChallengeObject() {}

    public String getName() {
        return name;
    }
    public String getDesc() {
        return desc;
    }
    public int getLimit() {
        return limit;
    }
    public String getSkinDesc() {
        return skinDesc;
    }
    public String getSkin() {
        return skin;
    }
    public boolean isClicked() {
        return isClicked;
    }
    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
    public boolean isSkinChosen() {
        return isSkinChosen;
    }
    public void setSkinChosen(boolean skinChosen) {
        isSkinChosen = skinChosen;
    }
}
