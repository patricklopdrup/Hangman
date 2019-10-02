package com.example.galgespil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startGame;
    long timeMillisec;
    TextView showTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.startGame);

        startGame.setOnClickListener(this);

        showTime = findViewById(R.id.test);
        timeMillisec = getIntent().getLongExtra("time", 0);


    }

    @Override
    public void onClick(View v) {
        if(v == startGame) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        }
    }
}
