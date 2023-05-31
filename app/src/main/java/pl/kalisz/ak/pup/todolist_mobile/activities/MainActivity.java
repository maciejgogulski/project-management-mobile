package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.fragments.ProjectListFragment;
import pl.kalisz.ak.pup.todolist_mobile.fragments.TaskListFragment;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class MainActivity extends AppCompatActivity{

    TaskListFragment tasksAfterTermFragment;

    TaskListFragment tasksBeforeTermFragment;

    ProjectListFragment projectListFragment;

    Button tasksAfterDeadlineButton;

    Button tasksBeforeDeadlineButton;

    Button projectListButton;

    Button logoutButton;

    FloatingActionButton fab;

    UserClient userClient;

    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resources = getResources();

        projectListFragment = ProjectListFragment.newInstance();

        projectListButton = findViewById(R.id.main_project_list_btn);
        tasksAfterDeadlineButton = findViewById(R.id.main_after_deadline_btn);
        tasksBeforeDeadlineButton = findViewById(R.id.main_before_deadline_btn);

        tasksAfterTermFragment = new TaskListFragment(true);
        tasksBeforeTermFragment = new TaskListFragment(false);

        defineProjectListFragment();
        defineTasksAfterTermButton();
        defineTasksBeforeTermButton();

        setHeightOfTheButtons();

        defineLogoutButton();

        defineFloatingActionButton();
    }

    @Override
    public void onBackPressed() {
        // Do nothing to block the back button
    }

    public void defineTasksAfterTermButton() {
        tasksAfterDeadlineButton.setOnClickListener(v -> {
            setButtonColorOnClick(tasksAfterDeadlineButton);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_fragment, tasksAfterTermFragment)
                    .commit();
        });
    }

    public void defineTasksBeforeTermButton() {
        tasksBeforeDeadlineButton.setOnClickListener(v -> {
            setButtonColorOnClick(tasksBeforeDeadlineButton);
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
        int colorSecondry = resources.getColor(R.color.dark_blue);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        int backgroundColor = typedValue.data;

        resetButtonColor(projectListButton);
        resetButtonColor(tasksAfterDeadlineButton);
        resetButtonColor(tasksBeforeDeadlineButton);

        button.setBackgroundColor(backgroundColor);
        button.setTextColor(colorSecondry);
    }

    private void resetButtonColor(Button button) {
        int colorPrimary = resources.getColor(R.color.light_blue);
        int colorSecondry = resources.getColor(R.color.dark_blue);

        button.setBackgroundColor(colorPrimary);
        button.setTextColor(colorSecondry);
    }

    private void setHeightOfTheButtons() {
        ViewTreeObserver viewTreeObserver = projectListButton.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                projectListButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int maxHeight = 0;

                // Measure the height of each button
                maxHeight = Math.max(maxHeight, projectListButton.getMeasuredHeight());
                maxHeight = Math.max(maxHeight, tasksBeforeDeadlineButton.getMeasuredHeight());
                maxHeight = Math.max(maxHeight, tasksAfterDeadlineButton.getMeasuredHeight());

                // Set the maximum height to all buttons
                projectListButton.setHeight(maxHeight);
                tasksBeforeDeadlineButton.setHeight(maxHeight);
                tasksAfterDeadlineButton.setHeight(maxHeight);
            }
        });
    }

    private void defineFloatingActionButton() {
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_menu_main, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                Intent intent;
                // Handle menu item click events here
                switch (item.getItemId()) {
                    case R.id.popup_add_project:
                        intent = new Intent(MainActivity.this, ProjectFormActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.popup_add_task:
                        intent = new Intent(MainActivity.this, TaskFormActivity.class);
                        startActivity(intent);
                        return true;
                    // Add more cases for other menu options if needed
                }
                return false;
            });

            popupMenu.show();
        });
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