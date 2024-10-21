//package com.example.smartalarm;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//public class AlarmActivity extends AppCompatActivity {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
//    private static final float TARGET_DISTANCE_METERS = 100.0f;
//    private TextView currentLocationText;
//    private TextView remainingDistanceText;
//    private Button toggleAlarmButton;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationCallback locationCallback;
//    private boolean isAlarmActive = false;
//    private Location targetLocation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alarm);
//
//        currentLocationText = findViewById(R.id.currentLocationText);
//        remainingDistanceText = findViewById(R.id.remainingDistanceText);
//        toggleAlarmButton = findViewById(R.id.toggleAlarmButton);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        targetLocation = new Location("");
//        targetLocation.setLatitude(-1.2921);
//        targetLocation.setLongitude(36.8219);
//
//        toggleAlarmButton.setOnClickListener(v -> toggleAlarm());
//
//        if (checkLocationPermission()) {
//            startLocationUpdates();
//        } else {
//            requestLocationPermission();
//        }
//    }
//
//    private boolean checkLocationPermission() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
//        } else {
//            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//        }
//    }
//
//    private void requestLocationPermission() {
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startLocationUpdates();
//            } else {
//                Toast.makeText(this, "Location permission denied. App functionality will be limited.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void startLocationUpdates() {
//        if (checkLocationPermission()) {
//            LocationRequest locationRequest = LocationRequest.create()
//                    .setInterval(10000)
//                    .setFastestInterval(5000)
//                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//            locationCallback = new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    if (locationResult == null) {
//                        return;
//                    }
//                    for (Location location : locationResult.getLocations()) {
//                        updateCurrentLocation(location);
//                    }
//                }
//            };
//
//            try {
//                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//            } catch (SecurityException e) {
//                Log.e("AlarmActivity", "Error requesting location updates: " + e.getMessage());
//                Toast.makeText(this, "Error requesting location updates", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            requestLocationPermission();
//        }
//    }
//
//    private void updateCurrentLocation(Location location) {
//        if (location != null) {
//            currentLocationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
//
//            float distanceToTarget = location.distanceTo(targetLocation);
//            remainingDistanceText.setText("Remaining Distance: " + distanceToTarget + " meters");
//
//            if (distanceToTarget <= TARGET_DISTANCE_METERS && !isAlarmActive) {
//                startAlarm();
//                isAlarmActive = true;
//            } else if (distanceToTarget > TARGET_DISTANCE_METERS && isAlarmActive) {
//                stopAlarm();
//                isAlarmActive = false;
//            }
//        } else {
//            currentLocationText.setText("Unable to retrieve current location");
//            remainingDistanceText.setText("Remaining Distance: N/A");
//        }
//    }
//
//    private void toggleAlarm() {
//        if (isAlarmActive) {
//            stopAlarm();
//        } else {
//            startAlarm();
//        }
//        isAlarmActive = !isAlarmActive;
//    }
//
//    private void startAlarm() {
//        Intent intent = new Intent(this, AlarmService.class);
//        startService(intent);
//    }
//
//    private void stopAlarm() {
//        Intent intent = new Intent(this, AlarmService.class);
//        stopService(intent);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (fusedLocationClient != null && locationCallback != null) {
//            fusedLocationClient.removeLocationUpdates(locationCallback);
//        }
//    }
//}
package com.example.smartalarm;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

public class AlarmActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private TextView currentLocationText;
    private TextView remainingDistanceText;
    private Button toggleAlarmButton;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isAlarmActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        currentLocationText = findViewById(R.id.currentLocationText);
        remainingDistanceText = findViewById(R.id.remainingDistanceText);
        toggleAlarmButton = findViewById(R.id.toggleAlarmButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        toggleAlarmButton.setOnClickListener(v -> toggleAlarm());

        if (checkLocationPermission()) {
            startLocationUpdates();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied. App functionality will be limited.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult. getLocations()) {
                    updateCurrentLocation(location);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateCurrentLocation(Location location) {
        currentLocationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        // Calculate remaining distance and update the UI
        // ...
    }

    private void toggleAlarm() {
        if (isAlarmActive) {
            stopAlarm();
        } else {
            startAlarm();
        }
        isAlarmActive = !isAlarmActive;
    }

    private void startAlarm() {
        // Start the alarm service
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    private void stopAlarm() {
        // Stop the alarm service
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);
    }
}