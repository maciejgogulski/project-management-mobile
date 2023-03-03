package pl.kalisz.ak.pup.todolist_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public class MainActivity extends AppCompatActivity {

    Button button;

    HttpService httpService = new HttpService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        Context context = this;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpService.sendRequest("/project/1", Request.Method.GET, context);
            }
        });
    }
}