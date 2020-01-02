package com.example.galgespil.Challenges;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.galgespil.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ChallengePage extends AppCompatActivity implements View.OnClickListener {

    mListAdapter mListAdapter = new mListAdapter();
    Button chooseSkinButton;
    ArrayList<Integer> skins;

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

    private void saveSkinsToSharedPrefs(Context context) {
        skins = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

    }

    @Override
    public void onClick(View v) {
        if(v == chooseSkinButton) {

        }
    }
}
