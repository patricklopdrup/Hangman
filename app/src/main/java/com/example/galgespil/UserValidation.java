package com.example.galgespil;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class UserValidation extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        System.out.println("hej med dig");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.logIn_framelayout, new LogIn())
                .commit();
    }
}
