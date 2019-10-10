package com.example.galgespil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.TypedArrayUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Highscore extends AppCompatActivity {
    HighscoreList highscoreList = new HighscoreList();
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //for this to work the "template" made in "list_element.xml" has to be "android:layout_height = "wrap_content"
    //"match_parent" does not work!

    List<Long> highscore;
    SharedPreferences prefs;
    Gson gson;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);



        String json = prefs.getString("highscore", "");
        highscore = gson.fromJson(json, new TypeToken<ArrayList<Long>>(){}.getType());
        System.out.println();


        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setContentView(recyclerView);
    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.list_element, parent, false);
            return new RecyclerView.ViewHolder(itemView) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView title = holder.itemView.findViewById(R.id.title);
            TextView desc = holder.itemView.findViewById(R.id.description);
            ImageView image = holder.itemView.findViewById(R.id.image);

            title.setText(""+(position + 1));
            desc.setText(""+highscore.get(position));
            image.setImageResource(android.R.drawable.sym_action_chat);
        }

        @Override
        public int getItemCount() {
            return highscore.size();
        }
    };

    private void saveList(List<Long> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        prefs.edit().putString(key, json);
        prefs.edit().commit();
    }

    private List<Long> getSavedList(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<List<Long>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
