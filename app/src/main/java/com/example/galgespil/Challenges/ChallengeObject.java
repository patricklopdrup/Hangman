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

    private final String SKIN_KEY = "mySkinKey";

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

    public int[] getChosenSkinList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(SKIN_KEY, "empty");
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

    public String getSKIN_KEY() {
        return SKIN_KEY;
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
