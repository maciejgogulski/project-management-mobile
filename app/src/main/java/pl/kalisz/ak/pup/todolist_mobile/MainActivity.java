package pl.kalisz.ak.pup.todolist_mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.kalisz.ak.pup.todolist_mobile.fragments.ProjectListFragment;
import pl.kalisz.ak.pup.todolist_mobile.fragments.TasksAfterTermFragment;
import pl.kalisz.ak.pup.todolist_mobile.fragments.TasksBeforeTermFragment;

public class MainActivity extends AppCompatActivity {

    TasksAfterTermFragment tasksAfterTermFragment;

    TasksBeforeTermFragment tasksBeforeTermFragment;

    ProjectListFragment projectListFragment;

    Button tasksAfterTermButton;

    Button tasksBeforeTermButton;

    Button projectListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListFragment = ProjectListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_activity_fragment, projectListFragment)
                .commit();

        defineProjectListFragment();
        defineTasksAfterTermButton();
        defineTasksBeforeTermButton();

    }

    public void defineTasksAfterTermButton() {
        tasksAfterTermButton = findViewById(R.id.afterTermButton);
        tasksAfterTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksAfterTermFragment = new TasksAfterTermFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_fragment, tasksAfterTermFragment)
                        .commit();
            }
        });
    }

    public void defineTasksBeforeTermButton() {
        tasksBeforeTermButton = findViewById(R.id.beforeTermButton);
        tasksBeforeTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksBeforeTermFragment = new TasksBeforeTermFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_fragment, tasksBeforeTermFragment)
                        .commit();
            }
        });
    }

    public void defineProjectListFragment() {
        projectListButton = findViewById(R.id.projectListButton);
        projectListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_fragment, projectListFragment)
                        .commit();
            }
        });
    }
}