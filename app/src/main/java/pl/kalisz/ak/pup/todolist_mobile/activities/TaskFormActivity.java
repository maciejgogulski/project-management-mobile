package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;

public class TaskFormActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "TASK";

    EditText nameEditText;

    Spinner userSpinner;

    Spinner projectSpinner;

    DatePicker termDatePicker;

    String termValue; // TODO zmienić nazwę term na deadline

    Button submitBtn;

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
        List<User> usersList = List.of(
                new User("SampleUser1"),
                new User("SampleUser2")
        ); // TODO Pobieranie userów z api
        userSpinner.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, usersList)
        );

        projectSpinner = findViewById(R.id.task_form_project);
        List<Project> projectsList = new ArrayList<>(); // TODO Pobieranie projektów z api
        projectSpinner.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, projectsList)
        );
    }

    private void setupDatePicker() {
        termDatePicker = findViewById(R.id.task_form_term);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        termValue = year + "-" + (month + 1) + "-" + dayOfMonth + " " + (hour + 1) + ":" + min;

        termDatePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                termValue = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            }
        }); // TODO Zmiana z date na dateTimePicker
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
        }); // Todo wysyłanie do api nowego usera, obsługa błędów.
    }
}