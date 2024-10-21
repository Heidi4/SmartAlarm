//package com.example.smartalarm;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SetDestinationActivity extends AppCompatActivity {
//
//    private EditText latitudeInput;
//    private EditText longitudeInput;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_set_destination);
//
//        latitudeInput = findViewById(R.id.latitudeInput);
//        longitudeInput = findViewById(R.id.longitudeInput);
//        Button saveButton = findViewById(R.id.saveButton);
//        Button discardButton = findViewById(R.id.discardButton);
//
//        saveButton.setOnClickListener(v -> saveDestination());
//        discardButton.setOnClickListener(v -> finish());
//    }
//
//    private void saveDestination() {
//        try {
//            double latitude = Double.parseDouble(latitudeInput.getText().toString());
//            double longitude = Double.parseDouble(longitudeInput.getText().toString());
//
//            SharedPreferences prefs = getSharedPreferences("SmartAlarmPrefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putFloat("destinationLatitude", (float) latitude);
//            editor.putFloat("destinationLongitude", (float) longitude);
//            editor.apply();
//
//            Toast.makeText(this, "Destination saved", Toast.LENGTH_SHORT).show();
//            finish();
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
//        }
//    }
//}

package com.example.smartalarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SetDestinationActivity extends AppCompatActivity {

    private EditText latitudeInput;
    private EditText longitudeInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_destination);

        latitudeInput = findViewById(R.id.latitudeInput);
        longitudeInput = findViewById(R.id.longitudeInput);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String latitude = latitudeInput.getText().toString();
            String longitude = longitudeInput.getText().toString();

            if (latitude.isEmpty() || longitude.isEmpty()) {
                Toast.makeText(this, "Please enter both latitude and longitude", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = getSharedPreferences("destination", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("latitude", latitude);
            editor.putString("longitude", longitude);
            editor.apply();

            Toast.makeText(this, "Destination saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}