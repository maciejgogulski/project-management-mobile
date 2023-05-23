package pl.kalisz.ak.pup.todolist_mobile.activities;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.UserClient;

public class TaskFormActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "TASK";

    EditText nameEditText;

    Spinner userSpinner;

    Spinner projectSpinner;

    Button deadlineBtn;

    String deadlineValue;

    Button submitBtn;

    private UserClient userClient;

    private ProjectClient projectClient;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        nameEditText = findViewById(R.id.task_form_name);
        setupSpinners();
        setupDatePicker();
        setupButtons();
    }

    private void setupSpinners() {
        userSpinner = findViewById(R.id.task_form_user);
        projectSpinner = findViewById(R.id.task_form_project);

        getUsersFromApi();
        getProjectsFromApi();
    }

    private void getUsersFromApi() {
        userClient = new UserClient(this);

        try {
            userClient.getUsers(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<User> data) {
                    runOnUiThread(() -> {
                        userSpinner.setAdapter(
                                new ArrayAdapter<>(TaskFormActivity.this, android.R.layout.simple_dropdown_item_1line, data)
                        );
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getProjectsFromApi() {
        projectClient = new ProjectClient(this);

        try {
            projectClient.getProjectsWithTasks(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<Project> data) {
                    runOnUiThread(() -> {
                        projectSpinner.setAdapter(
                                new ArrayAdapter<>(TaskFormActivity.this, android.R.layout.simple_dropdown_item_1line, data)
                        );
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupDatePicker() {
        deadlineBtn = findViewById(R.id.task_form_deadline);

        deadlineBtn.setText("Wybierz termin");
        deadlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });
    }

    private void openDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        deadlineValue = year + "-" + (month + 1) + "-" + dayOfMonth + " " + (hour + 1) + ":" + min;

        DatePickerDialog deadlineDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthString = addZeroInFrontOfSingleDigit(month);
                String dayOfMonthString = addZeroInFrontOfSingleDigit(dayOfMonth);
                deadlineValue = year + "-" + monthString + "-" + dayOfMonthString;
                deadlineBtn.setText(deadlineValue);
                openTimePickerDialog();
            }
        }, year, month, dayOfMonth);

        deadlineDatePicker.show();
    }

    private void openTimePickerDialog() {

        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hourString = addZeroInFrontOfSingleDigit(hourOfDay);
                String minuteString = addZeroInFrontOfSingleDigit(minute);
                deadlineValue += " " + hourString + ":" + minuteString;
                deadlineBtn.setText(deadlineValue);
            }
        }, hour, min, true);

        timePickerDialog.show();
    }

    private void setupButtons() {
        submitBtn = findViewById(R.id.task_form_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();

                Context context = v.getContext();
                Intent intent = new Intent(context, ProjectShowActivity.class);
                intent.putExtra(TaskShowActivity.EXTRA_TASK, (Task) task);
                context.startActivity(intent);
            }
        });
        // Todo wysyłanie do api nowego usera, obsługa błędów.
    }

    /**
     * Dodanie zera przed jednocyfowymi elementami daty i godziny, aby pasowały do formatu.
     * @param number Liczba do sparsowania.
     * @return Sparsowana liczba.
     */
    private String addZeroInFrontOfSingleDigit(int number) {
        return (number < 10) ? ("0" + number) : String.valueOf(number);
    }
}