package com.example.galgespil.Challenges;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.galgespil.R;
import com.example.galgespil.StartPage.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class ChallengePage extends AppCompatActivity implements View.OnClickListener {

    mListAdapter mListAdapter = new mListAdapter();
    ChallengeObject challengeObject = new ChallengeObject();
    Button chooseSkinButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page);

        ListFragment fragment = new ListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.challenge_framelayout, fragment);
        fragmentTransaction.commit();

        chooseSkinButton = findViewById(R.id.challenge_save_button);
        chooseSkinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == chooseSkinButton) {
            // TODO: 02-01-2020  vil godt kun gemme én gang. Når man trykker på knappen, men kan ikke få det til at virke. Nu gemmer den hver gang man trykker i mListAdapter
            //mListAdapter.saveSkinsToSharedPrefs(this, challengeObject.getSKIN_KEY());
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }



}
