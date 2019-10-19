package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startGame, highscore, help;
    private ImageView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.startGame);
        highscore = findViewById(R.id.highscore);
        help = findViewById(R.id.help);
        settings = findViewById(R.id.settings);


        startGame.setOnClickListener(this);
        highscore.setOnClickListener(this);
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
        } else if(v == help) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
        } else if(v == settings) {
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.show(getSupportFragmentManager(), "settings");
        }
    }
}
