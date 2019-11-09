package com.example.galgespil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Winner_loser extends Fragment implements View.OnClickListener {
    private boolean winner;
    private ImageView img;
    private TextView title, time, guesses;
    private Button restart, home;

    private long timeInMs;
    private int amountOfGuesses;

    public Winner_loser() {}
    public Winner_loser(boolean winner) {
        this.winner = winner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_winner, container, false);


        System.out.println("winner i frag er: " + winner);
        //disse tager ligenu det som bliver sendt afsted fra Game.java og ikke fra EndGame,
        //som jeg gerne vil. Jeg vil gerne overfører dataen via fragmenter og ikke aktiviteter
        amountOfGuesses = getActivity().getIntent().getIntExtra("guesses", -1);
        //timeInMs = getArguments().getLong("time", 1000L);
        timeInMs = getActivity().getIntent().getLongExtra("time", 1000L);


        img = layout.findViewById(R.id.img_endgame);
        title = layout.findViewById(R.id.title_endgame);
        time = layout.findViewById(R.id.time_endgame);
        guesses = layout.findViewById(R.id.guesses_endgame);
        restart = layout.findViewById(R.id.restart);
        home = layout.findViewById(R.id.home);

        restart.setOnClickListener(this);
        home.setOnClickListener(this);

        if(winner) {
            img.setImageResource(R.drawable.winner);
            title.setText(getString(R.string.winner));
            guesses.setText(getString(R.string.display_end_guesses, amountOfGuesses));
            float timeInSec = ((float)timeInMs / 1000);
            if(timeInSec >= 60.0) {
                time.setText(getString(R.string.display_end_time_in_min, (int)(timeInSec)/60, (int)(timeInSec)%60));
            } else {
                time.setText(getString(R.string.display_end_time_in_sec, timeInSec));
            }

        } else {
            img.setImageResource(R.drawable.loser);
            title.setText(getString(R.string.loser));
            guesses.setText(getString(R.string.display_end_guesses, amountOfGuesses));
            time.setVisibility(View.GONE);
        }

        return layout;
    }

    @Override
    public void onClick(View view) {
        if(view == restart) {
            Intent i = new Intent(getActivity(), Game.class);
            startActivity(i);
        } else if(view == home) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }
    }
}
