package pl.kalisz.ak.pup.todolist_mobile.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;

public class ProjectShowActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT = "PROJECT";

    private Project project;

    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_show);

        project = (Project) getIntent().getExtras().get(EXTRA_PROJECT);

        nameTextView = (TextView) findViewById(R.id.project_show_name);
        nameTextView.setText(project.getName());
    }
}