package com.ritacle.mymusichistory.scanner.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ritacle.mymusichistory.scanner.model.Listen;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ScannerService extends IntentService {
    private String userAccount;
    private BlockingDeque<Listen> listens = new LinkedBlockingDeque<>();
    private boolean isRunning = false;
    private ListeningBroadcastReceiver broadcastReceiver;

    public ScannerService(String userAccount) {
        super("Scanner service");
        this.userAccount = userAccount;

    }

    public ScannerService() {
        super("Scanner service");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        //    if (!isRunning) {
        //      userAccount = (String) intent.getExtras().get("accountName");


        //SenderServiceImpl sendingService = new SenderServiceImpl(getApplicationContext(), listens);
        //  performOnBackgroundThread(sendingService);
        //        isRunning = true;
        //      }

       // unregisterReceiver(broadcastReceiver);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //     unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCreate() {
        broadcastReceiver = new ListeningBroadcastReceiver(listens);
        registerReceiver(broadcastReceiver, createFilter());
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        t.start();

    }

    private IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter();

          intentFilter.addAction("com.android.music.musicservicecommand");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.updateprogress");
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.htc.music.metachanged");
        intentFilter.addAction("fm.last.android.metachanged");
        intentFilter.addAction("com.sec.android.app.music.metachanged");
        intentFilter.addAction("com.nullsoft.winamp.metachanged");
        intentFilter.addAction("com.amazon.mp3.metachanged");
        intentFilter.addAction("com.miui.player.metachanged");
        intentFilter.addAction("com.real.IMP.metachanged");
        intentFilter.addAction("com.sonyericsson.music.metachanged");
        intentFilter.addAction("com.rdio.android.metachanged");
        intentFilter.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        intentFilter.addAction("com.andrew.apollo.metachanged");

        return intentFilter;
    }

}
