package com.example.galgespil.Game;

import android.content.Intent;
import android.os.Bundle;

import com.example.galgespil.R;

import androidx.appcompat.app.AppCompatActivity;

public class GameFinishAct extends AppCompatActivity {
    private boolean winner;
    private long timePassed;
    private int guesses = 0;

    public GameFinishAct() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent intent = getIntent();

        //getting values from the GameAct class when the game ended
        guesses = intent.getIntExtra("guesses", -1);
        winner = intent.getBooleanExtra("winner", false);

        if (winner) {
            timePassed = intent.getLongExtra("time", -1L);
        }

        //putting data in the bundle
        Bundle bundle = new Bundle();
        bundle.putLong("time2", timePassed);
        bundle.putInt("guesses", guesses);

        //passing the data from the bundle to our fragment
        GameWinner_loserAct gameWinner_loserAct = new GameWinner_loserAct();
        gameWinner_loserAct.setArguments(bundle);
        System.out.println("min bundle: "+bundle.toString());

        //go to the fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.endGameFramelayout, new GameWinner_loserAct(winner))
                .commit();
    }
}
