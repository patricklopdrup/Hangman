package com.example.galgespil;

public class Keyboard {
    String[] keys = {"q","w","e","r","t","y","u","i","o","p","å",
                   "a","s","d","f","g","h","j","k","l","æ","ø",
                           "z","x","c","v","b","n","m"};

    public String[] abc() {
        String[] temp = new String[29];
        int startingLetter = 97;
        for(int i = 0; i < temp.length-3; i++) {
            temp[i] = Character.toString((char)(startingLetter + i));
        }
        temp[26] = "æ";
        temp[27] = "ø";
        temp[28] = "å";

        return temp;
    }

    public String[] getKeys(int i) {
        if(i == 0) {
            return keys;
        } else if(i == 1) {
            return abc();
        }
        return keys;
    }
}
