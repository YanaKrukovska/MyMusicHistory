package com.ritacle.mymusichistory;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.service.ListenerService;
import com.ritacle.mymusichistory.service.SendService;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class MMHApplication extends Application {

    private static final BlockingDeque<Scrobble> listens = new LinkedBlockingDeque<>();
    private SendService sendService;

    @Override
    public void onCreate() {
        super.onCreate();
        sendService = new SendService(this, listens);
        performOnBackgroundThread(sendService);
    }

    private void performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();

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

    public static void submit(Scrobble message) throws InterruptedException {
        listens.put(message);
    }

    public static Scrobble take() throws InterruptedException {
        return listens.take();
    }
}
