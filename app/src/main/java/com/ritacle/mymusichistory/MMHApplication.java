package com.ritacle.mymusichistory;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.service.ListenerService;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

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

}
