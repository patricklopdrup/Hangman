package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Help extends AppCompatActivity {

    private TextView helpText;

    private String text = "Click on \"START GAME\" and try to guess the word hidden in asterisk(stars)." +
            "\n\nSee if you can beat the timer in the bottom of your screen and become number 1!!" +
            "\n\nYou can also see your highscores in \"HIGHSCORE\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpText = findViewById(R.id.helpText);

        helpText.setText(R.string.showHelp_danish);
    }
}
