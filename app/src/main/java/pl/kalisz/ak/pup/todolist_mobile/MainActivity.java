package pl.kalisz.ak.pup.todolist_mobile;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.fragments.ProjectListFragment;
import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;

public class MainActivity extends AppCompatActivity {

    ProjectListFragment projectListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectListFragment = new ProjectListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.project_list_fragment, projectListFragment)
                .commit();
    }
}