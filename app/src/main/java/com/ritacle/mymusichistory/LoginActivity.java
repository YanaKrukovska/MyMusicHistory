package com.ritacle.mymusichistory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    SharedPreferences sharedPreferences;
    EditText emailField;
    EditText passwordField;
    TextView signUpButton;
    private static final int REQUEST_SIGNUP = 0;
    private final static String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        passwordField = (EditText) findViewById(R.id.login_password);
        emailField = (EditText) findViewById(R.id.login_email);
        signUpButton = (TextView) findViewById(R.id.link_signUp);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("logged", false)) {
           goToMainActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<User> callUser = service.getUser(emailField.getText().toString());
                callUser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.body() != null) {
                            User user = response.body();
                            Log.d(TAG, "Found user: " + user.toString());
                            sharedPreferences.edit().putBoolean("logged", true).putString("userName", user.getUserName()).putString("mail", user.getMail()).apply();
                            goToMainActivity();
                        } else {
                            Log.d(TAG, "User doesn't exist");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.d(TAG, "failed to process");
                    }
                });

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