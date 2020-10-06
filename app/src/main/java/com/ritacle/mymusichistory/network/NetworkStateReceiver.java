package com.ritacle.mymusichistory.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ritacle.mymusichistory.scrobbling.ListenSender;

public class NetworkStateReceiver extends BroadcastReceiver {

    private final ListenSender listenSender;

    public NetworkStateReceiver(ListenSender listenSender) {
        this.listenSender = listenSender;
        Log.d("NETWORK", "created");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null) {
            Log.d("NETWORK", "connected");
            listenSender.savePending();
        } else {
            Log.d("NETWORK", "no internet");
        }
    }
}

