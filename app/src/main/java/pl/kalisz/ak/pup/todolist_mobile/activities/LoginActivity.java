package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.dto.UserAuthDto;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;

    EditText passwordEditText;

    Button submitBtn;

    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            userClient = new UserClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        emailEditText = findViewById(R.id.login_form_email);
        passwordEditText = findViewById(R.id.login_form_password);

        setupButtons();

        try {
            isAuthenticated();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putLong("TASK", taskId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // taskId = savedInstanceState.getLong("TASK_ID");

        // TODO Wypełnianie danych formularza po obrocie urządzenia.
    }

    private void setupButtons() {

        submitBtn = findViewById(R.id.login_form_submit_btn);
        submitBtn.setOnClickListener(v -> {
            UserAuthDto authDto = new UserAuthDto();
            authDto.setEmail(emailEditText.getText().toString());
            authDto.setPassword(passwordEditText.getText().toString());
            login(authDto);
        });
    }

    private void login(UserAuthDto authDto) {
        try {
            userClient.login(authDto, new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(String data) {
                    runOnUiThread(() -> {
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        sharedPreferences.edit()
                                .putString("API_TOKEN", data).apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    });
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Logowanie nie powiodło się.", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAuthenticated() throws IOException {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("API_TOKEN", null);

        if (token == null) {
            return false;
        }

        UserClient userClient = new UserClient(this);

        userClient.checkAuth(new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Boolean data) {
                runOnUiThread(() -> {
                    if (data) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

        return true;
    }
}