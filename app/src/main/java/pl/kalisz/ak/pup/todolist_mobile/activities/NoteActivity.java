package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.NoteClient;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE_ID = "NOTE_ID";
    public static final String EXTRA_PROJECT_ID = "PROJECT_ID";
    public static final String EXTRA_TASK_ID = "TASK_ID";
    public static final String EXTRA_TASK_NAME = "TASK_NAME";
    public static final String EXTRA_PROJECT_NAME = "PROJECT_NAME";

    Long taskId;
    Long projectId;
    Long noteId;

    Note note;

    TextView label;
    String taskName;
    String projectName;
    EditText contentEditText;

    Button submitButton;
    Button deleteButton;

    NoteClient noteClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        label = findViewById(R.id.note_form_label);
        contentEditText = findViewById(R.id.note_form_content_edit_text);
        try {
            noteClient = new NoteClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getExtras();
        setupButtons();
    }

    private void getExtras() {
        String labelText = "Tworzenie notatki ";
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = (Long) intent.getExtras().get(EXTRA_NOTE_ID);
            try {
                getNoteFromApi();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            labelText = "Edycja notatki ";
        }
        if (intent.hasExtra(EXTRA_TASK_ID)) {
            taskId = (Long) intent.getExtras().get(EXTRA_TASK_ID);
            taskName = (String) intent.getExtras().get(EXTRA_TASK_NAME);

            labelText += "dla zadania " + taskName;
        }
        if (intent.hasExtra(EXTRA_PROJECT_ID)) {
            projectId = (Long) intent.getExtras().get(EXTRA_PROJECT_ID);
            projectName = (String) intent.getExtras().get(EXTRA_PROJECT_NAME);

            labelText += "dla projektu " + projectName;
        }
        label.setText(labelText);
    }

    private void setupButtons() {
        submitButton = findViewById(R.id.note_form_submit_btn);
        submitButton.setOnClickListener(v -> {
            if(note == null) {
                note = new Note();
            }

            note.setContent(contentEditText.getText().toString());

            if (projectId != null) {
                note.setProjectId(projectId);
            }

            if (taskId != null) {
                note.setTaskId(taskId);
            }

            try {
                submitNote(note);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        });

        deleteButton = findViewById(R.id.note_form_delete_btn);

        if (noteId != null) {
            deleteButton.setText(R.string.delete);
        } else {
            deleteButton.setText(R.string.back);
        }

        deleteButton.setOnClickListener(v -> {
            if(noteId != null) {
                try {
                    deleteNote(noteId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Intent intent = null;
            if (projectId != null) {
                intent = new Intent(NoteActivity.this, ProjectShowActivity.class);
                intent.putExtra(ProjectShowActivity.EXTRA_PROJECT_ID, projectId);
            }
            if (taskId != null) {
                intent = new Intent(NoteActivity.this, TaskShowActivity.class);
                intent.putExtra(TaskShowActivity.EXTRA_TASK_ID, taskId);
            }
            startActivity(intent);
        });
    }

    private void submitNote(Note note) throws IOException {
        if (note.getId() == null) {
            sendNewNote(note);
        } else {
            sendEditedNote(note);
        }
    }

    private void sendEditedNote(Note note) throws IOException {
        noteClient.editNote(note, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Note data) {
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Pomyślnie dodano notatkę", Toast.LENGTH_LONG).show();

                    Intent intent = null;
                    if (projectId != null) {
                        intent = new Intent(NoteActivity.this, ProjectShowActivity.class);
                        intent.putExtra(ProjectShowActivity.EXTRA_PROJECT_ID, projectId);
                    }
                    if (taskId != null) {
                        intent = new Intent(NoteActivity.this, TaskShowActivity.class);
                        intent.putExtra(TaskShowActivity.EXTRA_TASK_ID, taskId);
                    }
                    startActivity(intent);
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Edycja notatki nie powiodła się.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sendNewNote(Note note) throws IOException {
        noteClient.addNote(note, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Note data) {
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Pomyślnie dodano notatkę", Toast.LENGTH_LONG).show();

                    Intent intent = null;
                    if (projectId != null) {
                        intent = new Intent(NoteActivity.this, ProjectShowActivity.class);
                        intent.putExtra(ProjectShowActivity.EXTRA_PROJECT_ID, projectId);
                    }
                    if (taskId != null) {
                        intent = new Intent(NoteActivity.this, TaskShowActivity.class);
                        intent.putExtra(TaskShowActivity.EXTRA_TASK_ID, taskId);
                    }
                    startActivity(intent);
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Dodanie notatki nie powiodło się.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void getNoteFromApi() throws IOException {
        noteClient.getOneNote(noteId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Note data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getNoteFromApi.onSuccess: " + data);
                    note = data;
                    contentEditText.setText(note.getContent());
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "getNoteFromApi.onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Nie udało się pobrać notatki", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void deleteNote(Long noteId) throws IOException {
        noteClient.deleteNote(noteId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(String data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "deleteNote.onSuccess: " + data);
                    Toast.makeText(NoteActivity.this, "Usunięto notatkę", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "deleteNote.onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(NoteActivity.this, "Nie udało się usunąć notatki.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}