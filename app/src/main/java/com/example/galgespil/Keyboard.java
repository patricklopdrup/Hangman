package com.example.galgespil;

public class Keyboard {
    public Keyboard() {}

    public String[] qwerty = {"q","w","e","r","t","y","u","i","o","p","å",
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

    public String[] getKeys(int keyboardChoice) {
        if(keyboardChoice == 0)
            return this.qwerty;
        return this.abc();
    }
}
