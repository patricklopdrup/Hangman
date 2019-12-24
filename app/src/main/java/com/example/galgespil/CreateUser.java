package com.example.galgespil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class CreateUser extends Fragment implements View.OnClickListener {

    private static final String TAG = "Email";
    private TextView mUserName;
    private EditText mEmailField, mPasswordField, mRepeatPassword;
    private Button mSignInButton;
    private TextView mlink;

    private FirebaseAuth mAuth;
    private boolean newUser;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_sign_in, container, false);

        mUserName = layout.findViewById(R.id.username_signIn);
        mEmailField = layout.findViewById(R.id.email_signIn);
        mPasswordField = layout.findViewById(R.id.password_signIn);
        mRepeatPassword = layout.findViewById(R.id.repeat_password_signIn);
        mSignInButton = layout.findViewById(R.id.create_user_button);
        mlink = layout.findViewById(R.id.already_user_text);

        mSignInButton.setOnClickListener(this);
        mlink.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return layout;
    }

    private void createUser(String email, String password) {
        Log.d(TAG, "createUser: " + email);

        if(!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            newUser = isNew;
                            System.out.println("ny bruger create: " + isNew);
                            //saving userName in sharedpreference
                            SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(getString(R.string.userName_key), mUserName.getText().toString());
                            editor.commit();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            sendEmailVerification();
                        } else {
                            // If sign-up fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Sign-Up fejlede.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Bekræftelsesmail sendt til " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getContext(),
                                    "Kunne ikke sende bekræftelsesmail.",
                                    Toast.LENGTH_SHORT).show();
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

        //check for empty username
        String userName = mUserName.getText().toString();
        if(TextUtils.isEmpty(userName)) {
            mUserName.setError("Skal udfyldes.");
            valid = false;
        } else {
            mUserName.setError(null);
        }

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
        String repeatPassword = mRepeatPassword.getText().toString();
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

        //check for password and repeatpassword is the same
        if(!password.equals(repeatPassword)) {
            mRepeatPassword.setError("Passwords er ikke ens");
            valid = false;
        } else {
            mRepeatPassword.setError(null);
        }

        return valid;

    }

    @Override
    public void onClick(View v) {
        if(v == mSignInButton) {
            createUser(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if(v == mlink){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.logIn_framelayout, new LogIn())
                    .commit();
        }
    }
}
