package com.example.mealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_register extends AppCompatActivity {

    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstname = findViewById(R.id.firstname);
        final EditText lastname = findViewById(R.id.lastname);
        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText address = findViewById(R.id.address);
        final RadioGroup roleRadio = findViewById(R.id.radioGroupRole);
        final RadioButton lastRadioButton = findViewById(R.id.radioButtonClient);
        final EditText ccard = findViewById(R.id.ccard);
        final EditText description = findViewById(R.id.description);
        final Button register = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final boolean flag = false;

        register.setEnabled(flag);

        firstname.setError("Enter firstname");
        lastname.setError("Enter lastname");
        emailEditText.setError("Not a valid email");
        passwordEditText.setError("Password incorrect");
        address.setError("Enter address");
        ccard.setError("Enter a valid credit card number");


        roleRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioButtonCook:
                        selectedRole = "Cook";
                        ccard.setVisibility(View.VISIBLE);
                        lastRadioButton.setError(null);
                        register.setEnabled(true);
                        break;
                    case R.id.radioButtonClient:
                        selectedRole = "Client";
                        description.setVisibility(View.VISIBLE);
                        lastRadioButton.setError(null);
                        register.setEnabled(true);
                        break;

                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
