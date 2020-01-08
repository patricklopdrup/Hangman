package com.example.galgespil.StartPage;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.galgespil.Challenges.ChallengeAct;
import com.example.galgespil.Game.GameAct;
import com.example.galgespil.Statistic.StatLogic;
import com.example.galgespil.Statistic.StatAct;
import com.example.galgespil.Help;
import com.example.galgespil.Highscore.HighscoreAct;
import com.example.galgespil.MyKeyboard;
import com.example.galgespil.R;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame, highscore, challenge, statistics, help;
    private ImageView settings;

    private StatLogic statLogic = new StatLogic();
    private MyKeyboard myKeyboard = new MyKeyboard();

    private final boolean IS_DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(IS_DEBUG) {
            int wins = 5;
            int loses = 3;
            int rightGuess = 20;
            int wrongGuess = 25;
            long time = 120000;
            int[] letters = new int[myKeyboard.qwerty.length];
            for(int i = 0; i < letters.length; i++) {
                letters[i] = i+5;
            }
            System.out.println("Debug array: " + Arrays.toString(letters));
            System.out.println("debug array længde: " + letters.length);
            statLogic.updateStats(statLogic.getGameStats(this), statLogic.getGAME_OBJECT_KEY(), wins, loses, rightGuess, wrongGuess, time, letters, this);
        }

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
            Intent i = new Intent(this, GameAct.class);
            startActivity(i);
        } else if(v == highscore) {
            Intent i = new Intent(this, HighscoreAct.class);
            startActivity(i);
        } else if(v == challenge) {
            Intent i = new Intent(this, ChallengeAct.class);
            startActivity(i);
        } else if(v == statistics) {
            Intent i = new Intent(this, StatAct.class);
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
