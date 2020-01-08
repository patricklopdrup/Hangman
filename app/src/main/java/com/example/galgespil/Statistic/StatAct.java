package com.example.galgespil.Statistic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.galgespil.R;

public class StatAct extends AppCompatActivity implements View.OnClickListener {

    private Button moreStats;
    private TextView title;

    private boolean isShowingAllStats = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.statistics_frame, new StatListFrag())
                .commit();

        title = findViewById(R.id.statistics_title);
        //used to getting the title to scroll by it self
        title.setSelected(true);

        moreStats = findViewById(R.id.change_stats_btn);
        moreStats.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == moreStats) {
            if(isShowingAllStats) {
                //show the scroll view with barCharts
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.statistics_frame, new StatPressedKeysFrag())
                        .commit();
                isShowingAllStats = false;
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.statistics_frame, new StatListFrag())
                        .commit();
                isShowingAllStats = true;
            }
        }
    }
}
