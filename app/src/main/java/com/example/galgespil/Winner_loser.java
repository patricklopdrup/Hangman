package com.example.galgespil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Winner_loser extends Fragment {
    private boolean winner;
    private ImageView img;
    private TextView title, time, guesses;

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
        amountOfGuesses = getActivity().getIntent().getIntExtra("guesses", -1);
        //timeInMs = getArguments().getLong("time", 1000L);
        timeInMs = getActivity().getIntent().getLongExtra("time", 1000L);


        img = layout.findViewById(R.id.img_endgame);
        title = layout.findViewById(R.id.title_endgame);
        time = layout.findViewById(R.id.time_endgame);
        guesses = layout.findViewById(R.id.guesses_endgame);

        if(winner) {
            img.setImageResource(R.drawable.winner);
            title.setText(getString(R.string.winner));
            guesses.setText(getString(R.string.display_end_guesses, amountOfGuesses));
            float timeInSec = ((float)timeInMs / 1000);
            time.setText(getString(R.string.display_end_time, timeInSec));
        } else {
            img.setImageResource(R.drawable.loser);
            title.setText(getString(R.string.loser));
            guesses.setText(getString(R.string.display_end_guesses, amountOfGuesses));
            time.setVisibility(View.GONE);
        }

        return layout;
    }
}
