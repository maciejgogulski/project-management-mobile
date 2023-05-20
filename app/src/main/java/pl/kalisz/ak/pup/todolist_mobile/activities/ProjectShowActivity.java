package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.TaskListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class ProjectShowActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT = "PROJECT";

    private Project project;

    TextView nameTextView;

    RecyclerView tasksRecyclerView;

    TaskListAdapter adapter;

    Button addTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_show);

        project = (Project) getIntent().getExtras().get(EXTRA_PROJECT);

        nameTextView = findViewById(R.id.project_show_name);
        nameTextView.setText("Projekt " + project.getName());

        setupTaskRecyclerView();
        setupButtons();

    }

    private void setupButtons() {
        addTaskBtn = findViewById(R.id.project_show_add_task_btn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TaskFormActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void setupTaskRecyclerView() {
        tasksRecyclerView = findViewById(R.id.project_show_task_recycler_view);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Task> tasks = project.getTasks();

        if (!tasks.isEmpty()) {
            adapter = new TaskListAdapter(tasks);
            tasksRecyclerView.setAdapter(adapter);
        }
    }
}