package com.example.smartalarm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setDestinationButton = findViewById(R.id.setDestinationButton);
        Button startAlarmButton = findViewById(R.id.startAlarmButton);

        setDestinationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetDestinationActivity.class);
            startActivity(intent);
        });

        startAlarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
            startActivity(intent);
        });
    }
}