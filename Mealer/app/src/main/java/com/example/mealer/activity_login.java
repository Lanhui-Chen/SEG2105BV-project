package com.example.mealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button moveRegisterButton = findViewById(R.id.moveRegister);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final boolean flag = false;

        loginButton.setEnabled(flag);

        emailEditText.setError("Not a valid email");
        passwordEditText.setError("Password incorrect");

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmailValid(emailEditText.toString()) == isPasswordValid(passwordEditText.toString()));
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginVerification();
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginVerification();
            }
        });


        moveRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_login.this, activity_register.class));
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() >= 5;
    }

    public void loginVerification(){

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loadingProgressBar.setVisibility(View.VISIBLE);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        final String username;

        //Checks whether the imputed username and password are valid in the firebase database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean found = false;
                String tmpUsername = "";
                Iterator<DataSnapshot> children = snapshot.getChildren().iterator();
                while(!found && children.hasNext()){

                    tmpUsername = children.next().getKey();
                    if(tmpUsername.toLowerCase().equals(usernameEditText.getText().toString().toLowerCase())){

                        found = true;

                    }

                }
                if(found){

                    passwordVerification(tmpUsername);
                }

                else{

                    usernameEditText.setError(getString(R.string.Nonexistent_email));
                    passwordEditText.setError(null);
                    loadingProgressBar.setVisibility(View.INVISIBLE);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void passwordVerification(String username){

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final String userName = username;

        DatabaseReference userPassRef = FirebaseDatabase.getInstance().getReference("users/" + username + "/password");

        userPassRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(passwordEditText.getText().toString().equals(snapshot.getValue(String.class))){
                    Intent intent = new Intent(activity_login.this, activity_login_successful.class);
                    intent.putExtra("EXTRA_USERNAME", userName);
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    startActivity(intent);
                } else {
                    passwordEditText.setError(getString(R.string.wrong_password));
                    usernameEditText.setError(null);
                }
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
}