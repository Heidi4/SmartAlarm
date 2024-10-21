package com.example.smartalarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import androidx.work.OneTimeWorkRequest;
//import androidx.work.WorkManager;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        triggerAlarm();
        return START_STICKY;
    }

    private void triggerAlarm() {
        // Trigger the alarm sound, vibration, or other notifications
        // ...
    }
}