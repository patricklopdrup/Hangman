package com.example.galgespil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Highscore extends AppCompatActivity {
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //for this to work the "template" made in "list_element.xml" has to be "android:layout_height = "wrap_content"
    //"match_parent" does not work!

    String[] landeArray = {"Danmark", "Norge", "Sverige", "Island", "Færøerne", "Finland",
            "Tyskland", "Østrig", "Belgien", "Holland", "Italien", "Grækenland",
            "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand"};

    ArrayList<String> lande = new ArrayList<>(Arrays.asList(landeArray));

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

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

            title.setText(lande.get(position));
            desc.setText("Landet starter med: " + lande.get(position).charAt(0));
            image.setImageResource(android.R.drawable.sym_action_chat);
        }

        @Override
        public int getItemCount() {
            return lande.size();
        }
    };
}
