package pl.kalisz.ak.pup.todolist_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class ProjectFormActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT_ID = "PROJECT_ID";

    Long projectId;
    Project project;

    EditText nameEditText;

    Spinner userSpinner;
    Long selectedUserId;

    Button submitBtn;

    private UserClient userClient;

    private ProjectClient projectClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_form);

        try {
            projectClient = new ProjectClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            userClient = new UserClient(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        nameEditText = findViewById(R.id.login_form_email);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PROJECT_ID)) {
            projectId = (Long) intent.getExtras().get(EXTRA_PROJECT_ID);
            try {
                getProjectFromApi();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setupSpinners();
        setupButtons();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putLong("TASK", taskId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // taskId = savedInstanceState.getLong("TASK_ID");

        // TODO Wypełnianie danych formularza po obrocie urządzenia.
    }

    private void setupSpinners() {
        userSpinner = findViewById(R.id.login_form_password);

        getUsersFromApi();

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                selectedUserId = user.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupButtons() {

        submitBtn = findViewById(R.id.login_form_submit_btn);
        submitBtn.setOnClickListener(v -> {
            if (project == null) {
                project = new Project();
            }

            project.setName(nameEditText.getText().toString());
            project.setUserId(selectedUserId);

            try {
                submitProject(project);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void submitProject(Project project) throws IOException {
        if (project.getId() == null) {
            sendNewProject(project);
        } else {
            sendEditedProject(project);
        }
    }

    private void sendEditedProject(Project project) throws IOException {
        projectClient.editProject(project, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Project data) {
                runOnUiThread(() -> {
                    Toast.makeText(ProjectFormActivity.this, data.toString(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(ProjectFormActivity.this, "Edycja projektu nie powiodła się.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sendNewProject(Project project) throws IOException {
        projectClient.addProject(project, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Project data) {
                runOnUiThread(() -> {
                    Toast.makeText(ProjectFormActivity.this, data.toString(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(ProjectFormActivity.this, "Dodanie projektu nie powiodło się.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void getProjectFromApi() throws IOException {
        projectClient.getOneProject(projectId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Project data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getProjectFormApi.onSuccess: " + data);
                    project = data;

                    nameEditText.setText(project.getName());
                    if (project.getUserId() != null) {
                        selectedUserId = project.getUserId();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "getProjectFormApi.onFailure: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(ProjectFormActivity.this, "Nie udało się pobrać projektu.", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void getUsersFromApi() {
        try {
            userClient.getUsers(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<User> data) {
                    runOnUiThread(() -> {
                        userSpinner.setAdapter(
                                new ArrayAdapter<>(ProjectFormActivity.this, android.R.layout.simple_dropdown_item_1line, data)
                        );

                        if (project != null && project.getUserId() != null) {
                            userSpinner.setSelection(getPositionById(data, project.getUserId()));
                        }
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }

                private int getPositionById(List<User> dataList, long id) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getId() == id) {
                            return i;
                        }
                    }
                    return -1; // Return -1 if the ID is not found
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}