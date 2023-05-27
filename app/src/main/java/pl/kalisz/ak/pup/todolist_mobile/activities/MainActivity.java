package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.dto.UserAuthDto;
import pl.kalisz.ak.pup.todolist_mobile.fragments.ProjectListFragment;
import pl.kalisz.ak.pup.todolist_mobile.fragments.TaskListFragment;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class MainActivity extends AppCompatActivity {

    TaskListFragment tasksAfterTermFragment;

    TaskListFragment tasksBeforeTermFragment;

    ProjectListFragment projectListFragment;

    Button tasksAfterTermButton;

    Button tasksBeforeTermButton;

    Button projectListButton;

    Button logoutButton;

    UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListFragment = ProjectListFragment.newInstance();

        projectListButton = findViewById(R.id.main_project_list_btn);
        tasksAfterTermButton = findViewById(R.id.main_after_deadline_btn);
        tasksBeforeTermButton = findViewById(R.id.main_before_deadline_btn);

        tasksAfterTermFragment = new TaskListFragment(true);
        tasksBeforeTermFragment = new TaskListFragment(false);

        defineProjectListFragment();
        defineTasksAfterTermButton();
        defineTasksBeforeTermButton();

        defineLogoutButton();
    }

    public void defineTasksAfterTermButton() {
        tasksAfterTermButton.setOnClickListener(v -> {
            setButtonColorOnClick(tasksAfterTermButton);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment, tasksAfterTermFragment)
                    .commit();
        });
    }

    public void defineTasksBeforeTermButton() {
        tasksBeforeTermButton.setOnClickListener(v -> {
            setButtonColorOnClick(tasksBeforeTermButton);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment, tasksBeforeTermFragment)
                    .commit();
        });
    }

    public void defineProjectListFragment() {
        setButtonColorOnClick(projectListButton);

        projectListButton.setOnClickListener(v -> {
            setButtonColorOnClick(projectListButton);
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_fragment, projectListFragment)
                .commit();
        });
    }

    private void setButtonColorOnClick(Button button) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        int primaryColor = typedValue.data;

        resetButtonColor(projectListButton);
        resetButtonColor(tasksAfterTermButton);
        resetButtonColor(tasksBeforeTermButton);

        button.setBackgroundColor(Color.WHITE);
        button.setTextColor(primaryColor);
    }

    private void resetButtonColor(Button button) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        int primaryColor = typedValue.data;

        button.setBackgroundColor(primaryColor);
        button.setTextColor(Color.WHITE);
    }

    private void defineLogoutButton() {
        try {
            userClient = new UserClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logoutButton = findViewById(R.id.main_logout_btn);
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        try {
            userClient.logout(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(String data) {
                    runOnUiThread(() -> {
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                        sharedPreferences.edit()
                                .putString("API_TOKEN", null).apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}