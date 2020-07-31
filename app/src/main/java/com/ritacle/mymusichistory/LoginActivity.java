package com.ritacle.mymusichistory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.network.GetDataService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    SharedPreferences sharedPreferences;
    EditText mailField;
    EditText passwordField;
    TextView signUpButton;
    private static final int REQUEST_SIGNUP = 0;
    private final static String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        passwordField = findViewById(R.id.login_password);
        mailField = findViewById(R.id.login_email);
        signUpButton = findViewById(R.id.link_signUp);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        MMHApplication application = (MMHApplication) getApplication();
        if (application.isLoggedIn()) {
            goToMainActivity();
        }

        loginButton.setOnClickListener(v -> {

            if (!validate()) {
                loginFailed();
                return;
            }

            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> callUser = service.getUser(mailField.getText().toString());
            callUser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.body() != null) {
                        User user = response.body();
                        Log.d(TAG, "Found user: " + user.toString());
                        sharedPreferences.edit().putBoolean("logged", true).putString("userName", user.getUserName()).putString("mail", user.getMail()).putLong("user_id", user.getId()).apply();
                        goToMainActivity();
                    } else {
                        Log.d(TAG, "User doesn't exist");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Log.d(TAG, "failed to process");
                    loginFailed();
                }
            });

        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
        });
    }

    private void loginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String mail = mailField.getText().toString();
        if (StringUtils.isAllBlank(mail)) {
            mailField.setError("Mail field can't be empty");
            valid = false;
        } else {
            mailField.setError(null);
        }

        return valid;
    }

    public void goToMainActivity() {
        MMHApplication application = (MMHApplication) getApplication();
        application.setLoggedIn();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}