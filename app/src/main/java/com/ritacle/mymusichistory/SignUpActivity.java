package com.ritacle.mymusichistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ritacle.mymusichistory.model.Country;
import com.ritacle.mymusichistory.model.InputError;
import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;
import com.ritacle.mymusichistory.network.UserRestService;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SIGN UP";

    private EditText userNameField;
    private EditText nickNameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmationPasswordField;
    private EditText birthDateField;
    private Button signUpButton;
    private RadioButton lastGenderOptionButton;
    String chosenGender;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userNameField = findViewById(R.id.input_name);
        emailField = findViewById(R.id.input_email);
        passwordField = findViewById(R.id.input_password);
        confirmationPasswordField = findViewById(R.id.input_reEnterPassword);
        confirmationPasswordField = findViewById(R.id.input_reEnterPassword);
        nickNameField = findViewById(R.id.input_nickName);
        birthDateField = findViewById(R.id.Birthday);
        signUpButton = findViewById(R.id.btn_signup);
        lastGenderOptionButton = findViewById(R.id.gender_secret_option);
        TextView loginLink = findViewById(R.id.link_login);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        RadioGroup genderGroup = findViewById(R.id.radioGroup);
        genderGroup.getCheckedRadioButtonId();
        genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.gender_female_option) {
                chosenGender = "Female";
            } else if (checkedId == R.id.gender_male_option) {
                chosenGender = "Male";
            } else if (checkedId == R.id.gender_other_option) {
                chosenGender = "Other";
            } else {
                chosenGender = "Prefer not to tell";
            }
        });

        birthDateField.setOnClickListener(v -> new DatePickerDialog(SignUpActivity.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        signUpButton.setOnClickListener(v -> signUp());

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void signUp() {
        Log.d(TAG, "Starting signing up");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signUpButton.setEnabled(false);

        String name = userNameField.getText().toString();
        String nickname = nickNameField.getText().toString();
        String email = emailField.getText().toString();
        Log.d(TAG, "gender: " + chosenGender);

        String password = passwordField.getText().toString();
        Date birthDate = calendar.getTime();

        UserRestService service = RetrofitClientInstance.getRetrofitInstance().create(UserRestService.class);
        Call<ResponseMMH<User>> callUser = service.addUser(new User(email, name, nickname, chosenGender, password, birthDate, new Country("Ukraine", "UA")));
        callUser.enqueue(new Callback<ResponseMMH<User>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMMH<User>> call, @NonNull Response<ResponseMMH<User>> responseMMH) {
                if (responseMMH.body() != null) {
                    if (!responseMMH.body().isOkay()) {
                        Log.d(TAG, "failed to create new user");
                        setErrorMessages(responseMMH);
                        onSignUpFailed();
                    } else {
                        Log.d(TAG, "successfully created new user");
                        onSignUpSuccess();
                    }
                } else {
                    Log.d(TAG, "response body is empty");
                    onSignUpFailed();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMMH<User>> call, @NonNull Throwable t) {
                Log.d(TAG, "failed to process");
                onSignUpFailed();
            }
        });

    }

    private void setErrorMessages(@NonNull Response<ResponseMMH<User>> responseMMH) {
        if (responseMMH.body() != null) {
            for (int i = 0; i < responseMMH.body().getErrors().size(); i++) {
                InputError error = responseMMH.body().getErrors().get(i);
                switch (error.getFieldName()) {
                    case "mail":
                        emailField.setError(error.getErrorMessage());
                        break;
                    case "username":
                        userNameField.setError(error.getErrorMessage());
                        break;
                    case "nickname":
                        nickNameField.setError(error.getErrorMessage());
                        break;
                    case "gender":
                        lastGenderOptionButton.setError(error.getErrorMessage());
                        break;
                    case "birthDate":
                        birthDateField.setError(error.getErrorMessage());
                        break;
                    case "password":
                        passwordField.setError(error.getErrorMessage());
                        break;
                    case "confirmationPassword":
                        confirmationPasswordField.setError(error.getErrorMessage());
                        break;
                }
            }
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        birthDateField.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public void onSignUpSuccess() {
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = userNameField.getText().toString();
        if (StringUtils.isAllBlank(userName) || userName.length() < 3) {
            userNameField.setError("Name must be at least 3 characters long");
            valid = false;
        } else {
            userNameField.setError(null);
        }

        String nickName = nickNameField.getText().toString();
        if (StringUtils.isAllBlank(nickName)) {
            nickNameField.setError("Nickname can't be empty");
            valid = false;
        } else {
            nickNameField.setError(null);
        }

        if (StringUtils.isAllBlank(chosenGender)) {
            lastGenderOptionButton.setError("Gender can't be empty");
            valid = false;
        } else {
            lastGenderOptionButton.setError(null);
        }

        String email = emailField.getText().toString();
        if (StringUtils.isAllBlank(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (StringUtils.isAllBlank(password) || password.length() < 4) {
            passwordField.setError("Password must be longer than 4 characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        String confirmationPassword = confirmationPasswordField.getText().toString();
        if (StringUtils.isAllBlank(confirmationPassword) || confirmationPassword.length() < 4 || !(confirmationPassword.equals(password))) {
            confirmationPasswordField.setError("Passwords do not match");
            valid = false;
        } else {
            confirmationPasswordField.setError(null);
        }

        return valid;
    }
}