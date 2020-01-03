package com.example.galgespil.Challenges;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;

public class ChallengeObject {
    private String name;
    private String desc;
    private int limit;
    private String skinDesc;
    private String skin;
    private boolean isClickable;
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
        this.isClickable = false;
    }

    public ChallengeObject() {}

    @NonNull
    @Override
    public String toString() {
        return "name: " + this.name + " skinGroup: " + this.skinGroup + " isClickable: " + this.isClickable;
    }

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
    public boolean isClickable() {
        return isClickable;
    }
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }
    public SkinGroup getSkinGroup() {
        return skinGroup;
    }
}
