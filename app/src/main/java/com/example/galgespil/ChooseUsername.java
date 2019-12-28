package com.example.galgespil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChooseUsername extends Fragment implements View.OnClickListener{
    private EditText usernameField;
    private Button chooseButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_username, container, false);

        usernameField = layout.findViewById(R.id.choose_username_field);
        chooseButton = layout.findViewById(R.id.choose_username_button);

        chooseButton.setOnClickListener(this);

        return layout;
    }

    public void chooseUsername(String username) {
        String chosenName = usernameField.getText().toString();
        if(TextUtils.isEmpty(chosenName)) {
            usernameField.setError("Skal udfyldes!");
            return;
        } else {
            usernameField.setError(null);
        }
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("userName", username);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v == chooseButton) {
            chooseUsername(usernameField.getText().toString());
        }
    }
}
