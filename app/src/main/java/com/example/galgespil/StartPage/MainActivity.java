package com.example.galgespil.StartPage;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.galgespil.Challenges.ChallengePage;
import com.example.galgespil.Game.Game;
import com.example.galgespil.GameStatistic.Statistics_act;
import com.example.galgespil.Help;
import com.example.galgespil.Highscore.Highscore;
import com.example.galgespil.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame, highscore, challenge, statistics, help;
    private ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.startGame);
        highscore = findViewById(R.id.highscore);
        challenge = findViewById(R.id.challenge);
        statistics = findViewById(R.id.statistics);
        help = findViewById(R.id.help);
        settings = findViewById(R.id.settings);

        startGame.setOnClickListener(this);
        highscore.setOnClickListener(this);
        challenge.setOnClickListener(this);
        statistics.setOnClickListener(this);
        help.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == startGame) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        } else if(v == highscore) {
            Intent i = new Intent(this, Highscore.class);
            startActivity(i);
        } else if(v == challenge) {
            Intent i = new Intent(this, ChallengePage.class);
            startActivity(i);
        } else if(v == statistics) {
            Intent i = new Intent(this, Statistics_act.class);
            startActivity(i);
        } else if(v == help) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
        } else if(v == settings) {
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.show(getSupportFragmentManager(), "settings");
        }
    }
}
