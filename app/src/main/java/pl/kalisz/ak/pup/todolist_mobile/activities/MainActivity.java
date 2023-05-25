package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.fragments.ProjectListFragment;
import pl.kalisz.ak.pup.todolist_mobile.fragments.TaskListFragment;

public class MainActivity extends AppCompatActivity {

    TaskListFragment tasksAfterTermFragment;

    TaskListFragment tasksBeforeTermFragment;

    ProjectListFragment projectListFragment;

    Button tasksAfterTermButton;

    Button tasksBeforeTermButton;

    Button projectListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListFragment = ProjectListFragment.newInstance();

        projectListButton = findViewById(R.id.projectListButton);
        tasksAfterTermButton = findViewById(R.id.afterTermButton);
        tasksBeforeTermButton = findViewById(R.id.beforeTermButton);

        tasksAfterTermFragment = new TaskListFragment(true);
        tasksBeforeTermFragment = new TaskListFragment(false);

        defineProjectListFragment();
        defineTasksAfterTermButton();
        defineTasksBeforeTermButton();

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
}