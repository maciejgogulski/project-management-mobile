package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.NoteListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.adapters.TaskListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;
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

    RecyclerView notesRecyclerView;
    NoteListAdapter noteListAdapter;

    Button editButton;
    Button deleteButton;
    Button addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_show);

        try {
            taskClient = new TaskClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        userTextView.setText(String.valueOf(task.getUserName()));

        projectTextView = findViewById(R.id.task_show_project);
        projectTextView.setText(String.valueOf(task.getProjectName()));

        deadlineTextView = findViewById(R.id.task_show_deadline);
        deadlineTextView.setText(task.getDeadlineString());

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

        deleteButton = findViewById(R.id.task_show_delete_btn);
        deleteButton.setOnClickListener(v -> {
            try {
                sendDeleteTaskRequest(v);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        addNoteButton = findViewById(R.id.task_show_add_note_btn);
        addNoteButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra(NoteActivity.EXTRA_TASK_ID, task.getId());
            intent.putExtra(NoteActivity.EXTRA_TASK_NAME, task.getName());
            context.startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        notesRecyclerView = findViewById(R.id.task_show_note_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Note> notes = task.getNotes();

        if (!notes.isEmpty()) {
            noteListAdapter = new NoteListAdapter(notes, taskId, null, task.getName(), null);
            notesRecyclerView.setAdapter(noteListAdapter);
        }
    }

    public void getTaskFromApi() throws IOException {
        taskClient.getOneTask(taskId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Task data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getTaskFromApi.onSuccess: " + data);
                    task = data;

                    setupTextViews();
                    setupButtons();
                    setupRecyclerViews();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "getTaskFromApi.onFailure: " + errorMessage);
            }
        });
    }

    public void sendDeleteTaskRequest(View v) throws IOException {
        taskClient.deleteTask(taskId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "deleteTask.onSuccess: ");
                    Toast.makeText(TaskShowActivity.this, "Usunięto zadanie " + task.getName(), Toast.LENGTH_SHORT).show();

                    Context context = v.getContext();
                    Intent intent;
                    if (task.getProjectId() != null) {
                        intent = new Intent(context, ProjectShowActivity.class);
                        intent.putExtra(ProjectShowActivity.EXTRA_PROJECT_ID, task.getProjectId());
                    } else {
                        intent = new Intent(context, MainActivity.class);
                    }
                    context.startActivity(intent);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "deleteTask.onFailure: " + errorMessage);
            }
        });
    }
}