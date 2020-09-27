package com.ritacle.mymusichistory;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.ritacle.mymusichistory.db.PendingListensDB;
import com.ritacle.mymusichistory.service.ListenerService;
import com.ritacle.mymusichistory.utils.NotificationUtil;

public class MMHApplication extends Application {

    public boolean isLoggedIn;
    private PendingListensDB pendingListensDB;

    @Override
    public void onCreate() {
        super.onCreate();
        pendingListensDB = PendingListensDB.getInstance(getApplicationContext());
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
        stopService(new Intent(getApplicationContext(), ListenerService.class));
    }

    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        setLoggedOut();
        cancelNotifications();
        stopListenerService();
    }

    private void cancelNotifications() {
        NotificationUtil notificationUtil = new NotificationUtil(this);
        notificationUtil.hideListeningNowNotification();
    }

    public void setLoggedIn() {
        isLoggedIn = true;
    }

    public void setLoggedOut() {
        isLoggedIn = false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public PendingListensDB getPendingListensDB() {
        return pendingListensDB;
    }
}
