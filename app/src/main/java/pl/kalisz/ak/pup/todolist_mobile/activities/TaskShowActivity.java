package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.TaskClient;

public class TaskShowActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    Long taskId;
    Task task;
    TaskClient taskClient;

    TextView nameTextView;
    TextView userTextView;
    TextView projectTextView;
    TextView deadlineTextView;
    TextView completedTextView;

    Button editButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_show);

        taskClient = new TaskClient(this);
        taskId = (Long) getIntent().getExtras().get(EXTRA_TASK_ID);

        try {
            getTaskFromApi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            getTaskFromApi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("TASK_ID", taskId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        taskId = savedInstanceState.getLong("TASK_ID");
    }

    private void setupTextViews() {
        nameTextView = findViewById(R.id.task_show_name);
        nameTextView.setText(task.getName());

        userTextView = findViewById(R.id.task_show_user);
        userTextView.setText(String.valueOf(task.getUserId()));

        projectTextView = findViewById(R.id.task_show_project);
        projectTextView.setText(String.valueOf(task.getProjectId()));

        deadlineTextView = findViewById(R.id.task_show_deadline);
        deadlineTextView.setText(task.getDeadline().toString());

        completedTextView = findViewById(R.id.task_show_completed);
        completedTextView.setText(
                (task.getCompleted() != 0)
                ? "Tak"
                : "Nie"
        );
    }

    private void setupButtons() {
        editButton = findViewById(R.id.task_show_edit_btn);
        editButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, TaskFormActivity.class);
            intent.putExtra(TaskFormActivity.EXTRA_TASK_ID, task.getId());
            context.startActivity(intent);
        });
    }

    public void getTaskFromApi() throws IOException {
        taskClient.getOneTask(taskId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Task data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getProjectFormApi.onSuccess: " + data);
                    task = data;

                    setupTextViews();
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