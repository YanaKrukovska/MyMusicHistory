package com.ritacle.mymusichistory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    SharedPreferences sharedPreferences;
    EditText emailField;
    EditText passwordField;
    TextView signUpButton;
    private static final int REQUEST_SIGNUP = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        passwordField = (EditText) findViewById(R.id.login_password);
        emailField = (EditText) findViewById(R.id.login_email);
        signUpButton = (TextView) findViewById(R.id.link_signUp);


        sharedPreferences = getSharedPreferences("loginButton", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("loginButton", MODE_PRIVATE);


//        if (sharedPreferences.getBoolean("logged", false)) {
//            goToMainActivity();
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
                sharedPreferences.edit().putBoolean("logged", true).apply();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
            }
        });
    }

    public void goToMainActivity() {
        String action = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;
        startActivity(new Intent(action));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}