package com.example.galgespil.Statistic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.galgespil.R;

public class StatAct extends AppCompatActivity implements View.OnClickListener {

    private Button moreStats;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        StatListFrag fragment = new StatListFrag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.statistics_frame, fragment);
        fragmentTransaction.commit();

        title = findViewById(R.id.statistics_title);
        //used to getting the title to scroll by it self
        title.setSelected(true);

        moreStats = findViewById(R.id.change_stats_btn);
        moreStats.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == moreStats) {

        }
    }
}
