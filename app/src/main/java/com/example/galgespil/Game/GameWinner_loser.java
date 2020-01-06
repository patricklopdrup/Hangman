package com.example.galgespil.Game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.galgespil.R;
import com.example.galgespil.StartPage.MainActivity;
import com.github.jinatonic.confetti.CommonConfetti;

public class GameWinner_loser extends Fragment implements View.OnClickListener {
    private boolean winner;
    private ImageView img;
    private TextView title, time, guesses, word;
    private Button restart, home;

    private long timeInMs;
    private int amountOfGuesses;
    private String theWord;

    private ViewGroup vgContainer;
    private Handler confettiHandler;

    public GameWinner_loser() {}
    public GameWinner_loser(boolean winner) {
        this.winner = winner;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_winner_loser, container, false);

        //disse tager ligenu det som bliver sendt afsted fra GameAct.java og ikke fra GameFinishAct,
        //som jeg gerne vil. Jeg vil gerne overfører dataen via fragmenter og ikke aktiviteter
        amountOfGuesses = getActivity().getIntent().getIntExtra("guesses", -1);
        //timeInMs = getArguments().getLong("time", 1000L);
        timeInMs = getActivity().getIntent().getLongExtra("time", 1000L);

        theWord = getActivity().getIntent().getStringExtra("word");

        img = layout.findViewById(R.id.img_endgame);
        title = layout.findViewById(R.id.title_endgame);
        time = layout.findViewById(R.id.time_endgame);
        guesses = layout.findViewById(R.id.guesses_endgame);
        word = layout.findViewById(R.id.word_endgame);

        restart = layout.findViewById(R.id.restart);
        home = layout.findViewById(R.id.home);

        vgContainer = layout.findViewById(R.id.container);
        confettiHandler = new Handler();

        restart.setOnClickListener(this);
        home.setOnClickListener(this);

        //we use the same layout for both winner and loser
        if(winner) {
            //layout for winner
            time.setVisibility(View.VISIBLE);
            img.setImageResource(R.drawable.winner);
            title.setText(getString(R.string.winner));

            //animations for the winner page
            animateWinner(layout);

            float timeInSec = ((float)timeInMs / 1000);
            //calling showConfetti after everything else is loaded
            confettiHandler.post(run);
            if(timeInSec >= 60.0) {
                time.setText(getString(R.string.display_end_time_in_min, (int)(timeInSec)/60, (int)(timeInSec)%60));
            } else {
                time.setText(getString(R.string.display_end_time_in_sec, timeInSec));
            }
        } else {
            //layout for loser
            time.setVisibility(View.GONE);
            img.setImageResource(R.drawable.loser);
            title.setText(getString(R.string.loser));
            //animation for the loser page
            animateLoser(layout);
        }
        //layout for both
        word.setVisibility(View.VISIBLE);
        word.setText(getString(R.string.showTheWord, theWord));
        guesses.setText(getString(R.string.display_end_guesses, amountOfGuesses));

        return layout;
    }

    //when winner/loser page is not on the screen we finish the activity
    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    private void animateLoser(View layout) {
        YoYo.with(Techniques.FadeIn)
                .duration(3000)
                .repeat(0)
                .playOn(layout.findViewById(R.id.img_endgame));
    }

    //animation using YoYo library
    public void animateWinner(View layout) {
        YoYo.with(Techniques.RubberBand)
                .duration(700)
                .repeat(5)
                .playOn(layout.findViewById(R.id.img_endgame));

        YoYo.with(Techniques.Flash)
                .duration(500)
                .repeat(10)
                .playOn(layout.findViewById(R.id.title_endgame));
    }

    //a runnable for the confetti
    Runnable run = () -> showConfetti();

    private void showConfetti() {
        CommonConfetti.rainingConfetti(vgContainer, new int[] { Color.BLACK })
                .infinite();
    }

    //can go back to main menu or take another game
    @Override
    public void onClick(View view) {
        if(view == restart) {
            Intent i = new Intent(getActivity(), GameAct.class);
            startActivity(i);
        } else if(view == home) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        }
    }
}
