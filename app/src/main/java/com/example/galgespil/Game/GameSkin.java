package com.example.galgespil.Game;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Button;

import com.example.galgespil.Challenges.ChallengeLogic;
import com.example.galgespil.Challenges.ChallengeObject;

public class GameSkin {

    private ChallengeLogic challengeLogic = new ChallengeLogic();

    //skins
    public String manSkin = "";
    public int[] skinList;

    public void loadKeyboardSkin(int[] skinList, Button key, Context context) {
        //find index in enum "array"
        int keyboardSkinLookup = skinList[ChallengeObject.SkinGroup.KEYBOARD_SKIN.ordinal()];
        //take the skin out of the specific challenge if it is -1, we set is as "default.
        String keyboardSkinExtention = keyboardSkinLookup == -1 ? "default" : challengeLogic.getChallenges().get(keyboardSkinLookup).getSkin();
        //if it's an img we have to set or just a color
        if (keyboardSkinExtention.equals("rainbow")) {
            loadKeyboardImg(key, keyboardSkinExtention, context);
        } else {
            loadKeyboardColor(key, keyboardSkinExtention, context);
        }
    }

    public void loadKeyboardColor(Button key, String skinExtension, Context context) {
        String keyboardSkin = skinExtension + "_" + "btn";
        System.out.println("keyboardskin: " + keyboardSkin);
        //finding the int for the specific keyboardSkin
        int color = context.getResources().getIdentifier(keyboardSkin, "color", context.getPackageName());
        key.setBackgroundTintList(ColorStateList.valueOf(context.getColor(color)));
    }

    public void loadKeyboardImg(Button key, String imgToFind, Context context) {
        key.setBackgroundResource(context.getResources().getIdentifier(imgToFind, "drawable", context.getPackageName()));
    }

    public String loadManSkin(int[] skinList) {
        //find index in enum "array"
        int manSkinLookup = skinList[ChallengeObject.SkinGroup.MAN_SKIN.ordinal()];
        System.out.println("manskinlookup: " + manSkinLookup);
        if (manSkinLookup == -1) {
            return "default";
        }
        //take the skin out of the specific challenge
        System.out.println("få challenges: " + challengeLogic.getChallenges().get(manSkinLookup));
        System.out.println("loadman: " + challengeLogic.getChallenges().get(manSkinLookup).getSkin());
        return challengeLogic.getChallenges().get(manSkinLookup).getSkin();
    }

}
