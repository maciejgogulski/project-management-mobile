package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class TaskShowActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "TASK";

    Task task;

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

        task = (Task) getIntent().getExtras().get(EXTRA_TASK);

        setupTextViews();
        setupButtons();
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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, TaskFormActivity.class);
                intent.putExtra(TaskFormActivity.EXTRA_TASK, task);
                context.startActivity(intent);
            }
        });
    }
}