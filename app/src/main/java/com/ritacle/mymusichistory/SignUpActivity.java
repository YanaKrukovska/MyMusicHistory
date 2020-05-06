package com.ritacle.mymusichistory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText nameField;
    EditText emailField;
    EditText passwordField;
    EditText repeatPasswordField;
    Button signupButton;
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = (EditText) findViewById(R.id.input_name);
        emailField = (EditText) findViewById(R.id.input_email);
        passwordField = (EditText) findViewById(R.id.input_password);
        repeatPasswordField = (EditText) findViewById(R.id.input_reEnterPassword);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signUp() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signupButton.setEnabled(false);


        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String reEnterPassword = repeatPasswordField.getText().toString();

        // TODO: Implement  signUp logic here.

          onSignUpSuccess();


    }


    public void onSignUpSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String reEnterPassword = repeatPasswordField.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameField.setError("at least 3 characters");
            valid = false;
        } else {
            nameField.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordField.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            repeatPasswordField.setError("Password Do not match");
            valid = false;
        } else {
            repeatPasswordField.setError(null);
        }

        return valid;
    }
}