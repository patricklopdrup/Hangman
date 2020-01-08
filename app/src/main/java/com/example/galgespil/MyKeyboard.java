package com.example.galgespil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyKeyboard {
    public MyKeyboard() {}

    public String[] qwerty = {"q","w","e","r","t","y","u","i","o","p","å",
                       "a","s","d","f","g","h","j","k","l","æ","ø",
                               "z","x","c","v","b","n","m"};

    public String[] abc() {
        String[] temp = new String[29];
        int startingLetter = 97; //'a' is 97 in ascii table
        for(int i = 0; i < temp.length-3; i++) {
            temp[i] = Character.toString((char)(startingLetter + i));
        }
        temp[26] = "æ";
        temp[27] = "ø";
        temp[28] = "å";
        return temp;
    }

    private final String KEYBOARD_KEY = "myKeyboard";

    public String[] getKeys(int keyboardChoice) {
        if(keyboardChoice == 0)
            return this.qwerty;
        return this.abc();
    }

    public void saveKeyboardChoise(String key, int keyboardChoice, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, keyboardChoice);
        editor.commit();
    }

    public int getKeyboardChoise(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public String getKEYBOARD_KEY() {
        return KEYBOARD_KEY;
    }
}
