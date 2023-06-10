package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.NoteListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.adapters.TaskListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;
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
    TaskListAdapter taskListAdapter;

    RecyclerView notesRecyclerView;
    NoteListAdapter noteListAdapter;

    Button editBtn;

    ProjectClient projectClient;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_show);

        try {
            projectClient = new ProjectClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        projectId = (Long) getIntent().getExtras().get(EXTRA_PROJECT_ID);

        try {
            getProjectFromApi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        defineFloatingActionButton();
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
        editBtn = findViewById(R.id.project_show_edit_btn);
        editBtn.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ProjectFormActivity.class);
            intent.putExtra(ProjectFormActivity.EXTRA_PROJECT_ID, projectId);
            context.startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        tasksRecyclerView = findViewById(R.id.project_show_task_recycler_view);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Task> tasks = project.getTasks();

        if (!tasks.isEmpty()) {
            taskListAdapter = new TaskListAdapter(tasks);
            tasksRecyclerView.setAdapter(taskListAdapter);
        }


        notesRecyclerView = findViewById(R.id.project_show_note_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Note> notes = project.getNotes();

        if (!notes.isEmpty()) {
            noteListAdapter = new NoteListAdapter(notes, null, projectId, null, project.getName());
            notesRecyclerView.setAdapter(noteListAdapter);
        }
    }

    private void defineFloatingActionButton() {
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(ProjectShowActivity.this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popup_menu_project_show, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                Intent intent;
                // Handle menu item click events here
                switch (item.getItemId()) {
                    case R.id.project_show_popup_add_task:
                        intent = new Intent(ProjectShowActivity.this, TaskFormActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.project_show_popup_add_note:
                        intent = new Intent(ProjectShowActivity.this, NoteActivity.class);
                        intent.putExtra(NoteActivity.EXTRA_PROJECT_ID, projectId);
                        intent.putExtra(NoteActivity.EXTRA_PROJECT_NAME, project.getName());
                        startActivity(intent);
                        return true;
                    // Add more cases for other menu options if needed
                }
                return false;
            });

            popupMenu.show();
        });
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

                    setupRecyclerViews();
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