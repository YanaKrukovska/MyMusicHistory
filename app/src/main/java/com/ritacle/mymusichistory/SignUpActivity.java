package com.ritacle.mymusichistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;
import com.ritacle.mymusichistory.network.UserRestService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SIGN UP";

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText repeatPasswordField;
    private EditText nickNameField;
    private EditText genderField;
    private EditText birthday;
    private Button signUpButton;
    private TextView loginLink;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = (EditText) findViewById(R.id.input_name);
        emailField = (EditText) findViewById(R.id.input_email);
        passwordField = (EditText) findViewById(R.id.input_password);
        repeatPasswordField = (EditText) findViewById(R.id.input_reEnterPassword);
        repeatPasswordField = (EditText) findViewById(R.id.input_reEnterPassword);
        genderField = (EditText) findViewById(R.id.input_gender);
        nickNameField = (EditText) findViewById(R.id.input_nickName);
        signUpButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
        birthday = (EditText) findViewById(R.id.Birthday);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signUp() {
        Log.d(TAG, "Starting signing up");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signUpButton.setEnabled(false);

        String name = nameField.getText().toString();
        String nickname = nickNameField.getText().toString();
        String email = emailField.getText().toString();
        String gender = genderField.getText().toString();
        String password = passwordField.getText().toString();
        Date birthDate = calendar.getTime();

        UserRestService service = RetrofitClientInstance.getRetrofitInstance().create(UserRestService.class);
        Call<ResponseMMH<User>> callUser = service.addUser(new User(email, name, nickname, gender, password, birthDate));
        callUser.enqueue(new Callback<ResponseMMH<User>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMMH<User>> call, @NonNull Response<ResponseMMH<User>> responseMMH) {
                if (responseMMH.body() != null) {
                    for (int i = 0; i < responseMMH.body().getErrorMessages().size(); i++) {
                        System.out.println(responseMMH.body().getErrorMessages().get(i));
                    }
                } else {
                    Log.d(TAG, "Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMMH<User>> call, @NonNull Throwable t) {
                Log.d(TAG, "Failed to process");
            }
        });


        // onSignUpSuccess();


    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        birthday.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void onSignUpSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmationPassword = repeatPasswordField.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameField.setError("Name must be at least 3 characters long");
            valid = false;
        } else {
            nameField.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordField.setError("Password must be longer than 4 characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        if (confirmationPassword.isEmpty() || confirmationPassword.length() < 4 || !(confirmationPassword.equals(password))) {
            repeatPasswordField.setError("Passwords do not match");
            valid = false;
        } else {
            repeatPasswordField.setError(null);
        }

        return valid;
    }
}