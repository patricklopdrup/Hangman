package com.example.galgespil;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EndGame extends AppCompatActivity {
    private boolean winner;
    private long timePassed;
    private int guesses = 0;

    public EndGame() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent intent = getIntent();

        //getting values from the Game class when the game ended
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
        Winner_loser winner_loser = new Winner_loser();
        winner_loser.setArguments(bundle);
        System.out.println("min bungle: "+bundle.toString());

        //go to the fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.endGameFramelayout, new Winner_loser(winner))
                .addToBackStack(null)
                .commit();
    }
}
