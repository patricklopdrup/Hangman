package com.example.galgespil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button startGame, highscore, help, logOut;
    private ImageView settings;
    private boolean newUser;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        this.userName = sharedPref.getString(getString(R.string.userName_key), "");
        this.newUser = getIntent().getBooleanExtra("newUser", false);
        System.out.println("ny bruger i main: " + this.newUser);

        if(this.newUser) {
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userName);
            user.put("wins", 0);
            user.put("loses", 0);
            user.put("rightGuesses", 0);
            user.put("wrongGuesses", 0);
            user.put("gameTime", 0L);
            user.put("usedLetters", Arrays.asList(0));
            user.put("multiplayerGames", 0);

            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error adding document: " + e);
                        }
                    });

            Map<String, Object> skins = new HashMap<>();
            skins.put("defaultKeys", true);
            skins.put("redKeys", false);
            skins.put("blueKeys", false);

            db.collection("users").document().set(skins);
        }

        startGame = findViewById(R.id.startGame);
        highscore = findViewById(R.id.highscore);
        help = findViewById(R.id.help);
        settings = findViewById(R.id.settings);
        logOut = findViewById(R.id.log_out);


        startGame.setOnClickListener(this);
        highscore.setOnClickListener(this);
        help.setOnClickListener(this);
        settings.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == startGame) {
            Intent i = new Intent(this, Game.class);
            startActivity(i);
        } else if(v == highscore) {
            Intent i = new Intent(this, Highscore.class);
            startActivity(i);
        } else if(v == help) {
            Intent i = new Intent(this, Help.class);
            startActivity(i);
        } else if(v == settings) {
            SettingsDialog settingsDialog = new SettingsDialog();
            settingsDialog.show(getSupportFragmentManager(), "settings");
        } else if(v == logOut) {
            signOut();
        }
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Logget ud.",
                Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, UserValidation.class);
        startActivity(i);
    }
}
