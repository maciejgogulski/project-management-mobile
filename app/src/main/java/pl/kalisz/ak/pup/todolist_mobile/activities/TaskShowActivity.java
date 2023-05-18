package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import pl.kalisz.ak.pup.todolist_mobile.R;

public class TaskShowActivity extends AppCompatActivity {

    public static final String EXTRA_TASK = "TASK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_show);
    }
}