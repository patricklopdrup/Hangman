package com.example.galgespil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button startGame, highscore, help, logOut;
    private TextView welcomeUsername;
    private ImageView settings;
    private boolean newUser;
    private String userName;
    private String userNameFromdb;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DocumentSnapshot myData;
    private Object objectToReturn;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        this.userName = getIntent().getStringExtra("userName");
        this.newUser = getIntent().getBooleanExtra("newUser", false);
        System.out.println("ny bruger i main: " + this.newUser);

        //if(this.newUser) {
            Map<String, Object> user = new HashMap<>();
            user.put("userName", userName);
            user.put("wins", 0);
            user.put("loses", 0);
            user.put("rightGuesses", 0);
            user.put("wrongGuesses", 0);
            user.put("gameTime", 0L);
            user.put("usedLetters", Arrays.asList(0));
            user.put("multiplayerGames", 0);

            Map<String, Object> skins = new HashMap<>();
            skins.put("defaultKeys", true);
            skins.put("redKeys", false);
            skins.put("blueKeys", false);

            user.put("skins", skins);

            db.collection("users")
                    .document(currentUser.getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("DocumentSnapshot added successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error adding document: " + e);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    System.out.println("hallo: " + getData("userName"));
                    setUserName();
                }
            });
       // }

        startGame = findViewById(R.id.startGame);
        highscore = findViewById(R.id.highscore);
        help = findViewById(R.id.help);
        settings = findViewById(R.id.settings);
        logOut = findViewById(R.id.log_out);
        welcomeUsername = findViewById(R.id.welcome_username);

        //displaying username
        //handler.postDelayed(setUsername, 3000);

        startGame.setOnClickListener(this);
        highscore.setOnClickListener(this);
        help.setOnClickListener(this);
        settings.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    public Object getData(String field) {
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    myData = task.getResult();
                    objectToReturn = myData.get(field);
                    System.out.println("data: "+ myData.getData());
                    System.out.println("i field: " + objectToReturn);
                } else {
                    System.out.println("det virkede ikke");
                }
            }
        });

        System.out.println("inden return: "+objectToReturn);
        return objectToReturn;
    }

    public void setUserName() {
        welcomeUsername.setText(getString(R.string.setUsername, getData("userName")));
        System.out.println("kan vi det: " + getString(R.string.setUsername, getData("userName")));
    }

    Runnable setUsername = () -> {
        try {
            //String username = getData().getString("userName");
            Object hej = getData("userName");
            System.out.println("her er username: "+hej);
            welcomeUsername.setText("Velkommen: " + hej);
        } catch (Exception e) {
            System.out.println("der skete en fejl: " + e);
        }
    };

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
