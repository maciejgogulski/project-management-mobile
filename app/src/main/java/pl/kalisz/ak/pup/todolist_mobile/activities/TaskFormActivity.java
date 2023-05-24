package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.TaskClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class TaskFormActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    Long taskId;
    Task task;

    EditText nameEditText;

    Spinner userSpinner;
    Long selectedUserId;

    Spinner projectSpinner;
    Long selectedProjectId;

    Button deadlineBtn;
    String deadlineValue;

    int selectedCompleted;

    ToggleButton completedButton;

    Button submitBtn;

    private UserClient userClient;

    private ProjectClient projectClient;

    private TaskClient taskClient;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        projectClient = new ProjectClient(this);
        taskClient = new TaskClient(this);
        userClient = new UserClient(this);

        nameEditText = findViewById(R.id.task_form_name);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_TASK_ID)) {
            taskId = (Long) intent.getExtras().get(EXTRA_TASK_ID);
            try {
                getTaskFromApi();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setupSpinners();
        setupDatePicker();
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
        userSpinner = findViewById(R.id.task_form_user);
        projectSpinner = findViewById(R.id.task_form_project);

        getUsersFromApi();
        getProjectsFromApi();

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

        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Project project = (Project) parent.getItemAtPosition(position);
                selectedProjectId = project.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupDatePicker() {
        deadlineBtn = findViewById(R.id.task_form_deadline);

        int year, month, dayOfMonth, hour, min;

        if (task != null) {
            calendar.setTime(task.getDeadline());
        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);

        String monthString = addZeroInFrontOfSingleDigit(month + 1);
        String dayOfMonthString = addZeroInFrontOfSingleDigit(dayOfMonth);
        String hourString = addZeroInFrontOfSingleDigit(hour + 1);
        String minuteString = addZeroInFrontOfSingleDigit(min);

        deadlineValue = year + "-" + (monthString) + "-" + dayOfMonthString + " " + (hourString) + ":" + minuteString;

        deadlineBtn.setText(deadlineValue);

        deadlineBtn.setOnClickListener(v -> openDatePickerDialog());
    }

    private void openDatePickerDialog() {
        int year, month, dayOfMonth, hour, min;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);

        deadlineValue = year + "-" + (month + 1) + "-" + dayOfMonth + " " + (hour + 1) + ":" + min;

        DatePickerDialog deadlineDatePicker = new DatePickerDialog(this, (view, year1, month1, dayOfMonth1) -> {
            String monthString = addZeroInFrontOfSingleDigit(month1 + 1);
            String dayOfMonthString = addZeroInFrontOfSingleDigit(dayOfMonth1);
            deadlineValue = year1 + "-" + monthString + "-" + dayOfMonthString;
            deadlineBtn.setText(deadlineValue);
            openTimePickerDialog();
        }, year, month, dayOfMonth);

        deadlineDatePicker.show();
    }

    private void openTimePickerDialog() {

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String hourString = addZeroInFrontOfSingleDigit(hourOfDay);
            String minuteString = addZeroInFrontOfSingleDigit(minute);
            deadlineValue += " " + hourString + ":" + minuteString;
            deadlineBtn.setText(deadlineValue);
        }, hour, min, true);

        timePickerDialog.show();
    }

    private void setupButtons() {
        completedButton = findViewById(R.id.task_form_completed);
        completedButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedCompleted = isChecked ? 1 : 0;
        });


        submitBtn = findViewById(R.id.task_form_submit_btn);
        submitBtn.setOnClickListener(v -> {
            if (task == null) {
                task = new Task();
            }

            task.setName(nameEditText.getText().toString());
            task.setCompleted(selectedCompleted);
            try {
                task.setDeadline(deadlineValue);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            task.setUserId(selectedUserId);
            task.setProjectId(selectedProjectId);

            try {
                submitTask(task);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void submitTask(Task task) throws IOException {
        if (task.getId() == null) {
            sendNewTask(task);
        } else {
            sendEditedTask(task);
        }
    }

    private void sendEditedTask(Task task) throws IOException {
        taskClient.editTask(task, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Task data) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskFormActivity.this, data.toString(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
            }
        });
    }

    private void sendNewTask(Task task) throws IOException {
        taskClient.addTask(task, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Task data) {
                runOnUiThread(() -> {
                    Toast.makeText(TaskFormActivity.this, data.toString(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onClick: " + data);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
            }
        });
    }

    public void getTaskFromApi() throws IOException {
        taskClient.getOneTask(taskId, new HttpClient.ApiResponseListener<>() {
            @Override
            public void onSuccess(Task data) {
                runOnUiThread(() -> {
                    Log.d("DEBUG", "getProjectFormApi.onSuccess: " + data);
                    task = data;

                    nameEditText.setText(task.getName());
                    if (task.getProjectId() != null) {
                        selectedProjectId = task.getProjectId();
                    }
                    if (task.getUserId() != null) {
                        selectedUserId = task.getUserId();
                    }

                    completedButton.setChecked(task.isCompleted());
                    selectedCompleted = task.getCompleted();
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("ERROR", "getProjectFormApi.onFailure: " + errorMessage);
            }
        });
    }

    private void getProjectsFromApi() {
        try {
            projectClient.getProjectsWithTasks(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<Project> data) {
                    runOnUiThread(() -> {
                        projectSpinner.setAdapter(
                                new ArrayAdapter<>(TaskFormActivity.this, android.R.layout.simple_dropdown_item_1line, data)
                        );

                        if (task != null && task.getProjectId() != null) {
                            projectSpinner.setSelection(getPositionById(data, task.getProjectId()));
                        }
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }

                private int getPositionById(List<Project> dataList, long id) {
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

    private void getUsersFromApi() {
        try {
            userClient.getUsers(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<User> data) {
                    runOnUiThread(() -> {
                        userSpinner.setAdapter(
                                new ArrayAdapter<>(TaskFormActivity.this, android.R.layout.simple_dropdown_item_1line, data)
                        );

                        if (task != null && task.getUserId() != null) {
                            userSpinner.setSelection(getPositionById(data, task.getUserId()));
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

    /**
     * Dodanie zera przed jednocyfowymi elementami daty i godziny, aby pasowały do formatu.
     *
     * @param number Liczba do sparsowania.
     * @return Sparsowana liczba.
     */
    private String addZeroInFrontOfSingleDigit(int number) {
        return (number < 10) ? ("0" + number) : String.valueOf(number);
    }
}