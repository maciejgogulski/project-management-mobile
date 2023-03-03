package pl.kalisz.ak.pup.todolist_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String response = getIntent().getStringExtra("response");
        TextView textView = findViewById(R.id.displayResponse);

        textView.setText(response);
    }
}