package com.example.galgespil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends Fragment implements View.OnClickListener {
    private static final String TAG = "Email";
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLogInButton;
    private TextView mlink;

    private FirebaseAuth mAuth;
    private boolean newUser;

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("her er jeg");
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_log_in, container, false);

        mEmailField = layout.findViewById(R.id.email_logIn);
        mPasswordField = layout.findViewById(R.id.password_logIn);
        mLogInButton = layout.findViewById(R.id.logInButton);
        mlink = layout.findViewById(R.id.no_user_text);

        mLogInButton.setOnClickListener(this);
        mlink.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return layout;
    }

    private void logIn(String email, String password) {
        Log.d(TAG, "logIn: " + email);

        if(!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            long creation = user.getMetadata().getCreationTimestamp();
                            long lastSignIn = user.getMetadata().getLastSignInTimestamp();
                            System.out.println("creation: " + creation);
                            System.out.println("lastSignIn: " + lastSignIn);
                            newUser = (creation == lastSignIn);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Log ind fejlede.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            if(user.isEmailVerified()) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("newUser", newUser);
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "Bekræft email.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        //check for empty email
        String email = mEmailField.getText().toString();
        if(TextUtils.isEmpty(email)) {
            mEmailField.setError("Skal udfyldes.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        //check for empty password
        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(password)) {
            mPasswordField.setError("Skal udfyldes.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        //check for length
        if(password.length() < 6) {
            mPasswordField.setError("Skal være 6 eller flere tegn.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;

    }

    @Override
    public void onClick(View v) {
        if(v == mLogInButton) {
            logIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if(v == mlink) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.logIn_framelayout, new CreateUser())
                    .commit();
        }
    }
}
