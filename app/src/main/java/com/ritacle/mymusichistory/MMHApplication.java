package com.ritacle.mymusichistory;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.ritacle.mymusichistory.service.ListenerService;

public class MMHApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void startListenerService() {
        if (ListenerService.isNotificationAccessEnabled(this)) {
            startService(new Intent(this, ListenerService.class));
        }
    }

    public void stopListenerService() {
        stopService(new Intent(this, ListenerService.class));
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferences.getBoolean("logged", false);
        stopListenerService();
    }

}
