package com.example.galgespil.StartPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.galgespil.R;

public class Help extends AppCompatActivity {

    private TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpText = findViewById(R.id.helpText);

        //setting the text from string.xml
        helpText.setText(R.string.showHelp_danish);
    }
}
