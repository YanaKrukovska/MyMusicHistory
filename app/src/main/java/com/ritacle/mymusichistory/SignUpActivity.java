package com.ritacle.mymusichistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.LinkedList;
import java.util.List;
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
    private TextView countryBanner;
    private TextView genderBanner;
    private Button signUpButton;
    private final Calendar calendar = Calendar.getInstance();
    private List<Country> countries;
    private String chosenGender;
    private Country chosenCountry;

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
        birthDateField = findViewById(R.id.birthday_edit);
        signUpButton = findViewById(R.id.btn_signup);
        countryBanner = findViewById(R.id.country_banner);
        genderBanner = findViewById(R.id.gender_banner);
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
                chosenGender = "F";
            } else if (checkedId == R.id.gender_male_option) {
                chosenGender = "M";
            } else if (checkedId == R.id.gender_other_option) {
                chosenGender = "O";
            } else {
                chosenGender = "N";
            }
        });


        String[] isoCountryCodes = Locale.getISOCountries();
        countries = new LinkedList<>();
        for (String countryCode : isoCountryCodes) {
            Locale locale = new Locale("", countryCode);
            String countryName = locale.getDisplayCountry();
            countries.add(new Country(countryName, countryCode));
        }

        Spinner countrySpinner = findViewById(R.id.countries_spinner);
        ArrayAdapter<Country> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                chosenCountry = countries.get(selectedItemPosition);
            }

            public void onNothingSelected(AdapterView<?> parent) {
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
        Country country = chosenCountry;
        String password = passwordField.getText().toString();
        String confirmationPassword = confirmationPasswordField.getText().toString();
        Date birthDate = calendar.getTime();

        UserRestService service = RetrofitClientInstance.getRetrofitInstance().create(UserRestService.class);
        Call<ResponseMMH<User>> callUser = service.addUser(new User(email, name, nickname, chosenGender, password, confirmationPassword, birthDate, country));
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
                        genderBanner.setError(error.getErrorMessage());
                        break;
                    case "country":
                        countryBanner.setError(error.getErrorMessage());
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
            genderBanner.setError("Gender can't be empty");
            valid = false;
        } else {
            genderBanner.setError(null);
        }

        String email = emailField.getText().toString();
        if (StringUtils.isAllBlank(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (chosenCountry == null) {
            countryBanner.setError("Choose country");
            valid = false;
        } else {
            countryBanner.setError(null);
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