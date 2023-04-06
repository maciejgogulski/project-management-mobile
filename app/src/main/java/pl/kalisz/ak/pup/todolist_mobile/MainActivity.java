package pl.kalisz.ak.pup.todolist_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;

public class MainActivity extends AppCompatActivity {

    Button button;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.displayResponse);

        HttpService httpService = new HttpService(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    httpService.sendRequest("/api/projects");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}