package com.example.galgespil.Highscore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galgespil.R;

import java.util.List;

public class HighscoreAct extends AppCompatActivity {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //for this to work the "template" made in "highscore_list_elementst_element.xml" has to be "android:layout_height = "wrap_content"
    //"match_parent" does not work!

    private HighscoreLogic highscoreLogic = new HighscoreLogic();
    private List<HighscoreObject> highscoreSorted;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        //getting my sorted highscoreList
        highscoreSorted = highscoreLogic.getSortedHighscoreList(highscoreLogic.getHighscoreKey(), this);

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setContentView(recyclerView);
    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        //this inflate the "template" for how one element in the list looks like
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.highscore_list_element, parent, false);
            return new RecyclerView.ViewHolder(itemView) {};
        }

        //here we add data to the "template" for every element in our array. We use "int position" for this
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            //getting the views
            TextView timeUsed = holder.itemView.findViewById(R.id.time_used);
            TextView word = holder.itemView.findViewById(R.id.the_word);
            ImageView image = holder.itemView.findViewById(R.id.image);
            TextView pos = holder.itemView.findViewById(R.id.placement);
            TextView guesses = holder.itemView.findViewById(R.id.guesses);

            //setting the background color for every second one
            if(position % 2 == 0) {
                holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.default_btn));
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE);
            }

            //switch for number 1,2 and 3. They have there own medal image.
            switch (position) {
                case 0:
                    image.setVisibility(View.VISIBLE);
                    pos.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.gold);
                    break;
                case 1:
                    image.setVisibility(View.VISIBLE);
                    pos.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.silver);
                    break;
                case 2:
                    image.setVisibility(View.VISIBLE);
                    pos.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.bronze);
                    break;
                default:
                    image.setVisibility(View.GONE);
                    pos.setVisibility(View.VISIBLE);
                    pos.setText(getString(R.string.show_pos, (position+1)));
                    break;
            }
            long timeInMs = highscoreSorted.get(position).getTime();
            float timeInSec = ((float)timeInMs / 1000);
            //if time is more than 60 sec, we write fx "1 min 14 sek"
            if(timeInSec >= 60.0) {
                timeUsed.setText(getString(R.string.show_time_used_min, (int)(timeInSec)/60, (int)(timeInSec)%60));
            } else {
                timeUsed.setText(getString(R.string.show_time_used, timeInSec));
            }
            //setting the specific word and amount of guesses
            word.setText(getString(R.string.show_word, highscoreSorted.get(position).getWord()));
            guesses.setText(getString(R.string.show_guesses, highscoreSorted.get(position).getGuesses()));
        }

        @Override
        public int getItemCount() {
            return highscoreSorted.size();
        }
    };

}
