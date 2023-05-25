package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.TaskListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;

public class ProjectShowActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT_ID = "PROJECT_ID";

    private Long projectId;

    private Project project;

    TextView nameTextView;

    RecyclerView tasksRecyclerView;

    TaskListAdapter adapter;

    Button addTaskBtn;
    Button editBtn;

    ProjectClient projectClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_show);

        projectClient = new ProjectClient(this);
        projectId = (Long) getIntent().getExtras().get(EXTRA_PROJECT_ID);

        try {
            getProjectFromApi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            getProjectFromApi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("PROJECT_ID", projectId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        projectId = savedInstanceState.getLong("PROJECT_ID");
    }

    private void setupButtons() {
        addTaskBtn = findViewById(R.id.project_show_add_task_btn);
        addTaskBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, TaskFormActivity.class);
            context.startActivity(intent);
        });

        editBtn = findViewById(R.id.project_show_edit_btn);
        editBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProjectFormActivity.class);
            intent.putExtra(ProjectFormActivity.EXTRA_PROJECT_ID, projectId);
            context.startActivity(intent);
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

    private void getProjectFromApi() throws IOException {
        projectClient.getOneProject(projectId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Project data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getProjectFormApi.onSuccess: " + data);
                    project = data;

                    nameTextView = findViewById(R.id.project_show_name);
                    nameTextView.setText("Projekt " + project.getName());

                    setupTaskRecyclerView();
                    setupButtons();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "getProjectFormApi.onFailure: " + errorMessage);
            }
        });
    }
}