package com.ritacle.mymusichistory.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.scrobbling.ListenSender;

public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {
    final NetworkRequest networkRequest;
    private ListenSender listenSender;
    private static final String TAG = "Network Monitor";
    private Context context;

    public ConnectionStateMonitor(Context context, ListenSender listenSender) {
        networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
        this.listenSender = listenSender;
        this.context = context;
    }

    public void enable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, this);
    }


    @Override
    public void onAvailable(@NonNull Network network) {
        MMHApplication mmhApplication = (MMHApplication) context;
        Log.d(TAG, "available");
        if (mmhApplication.isLoggedIn()) {
            Log.d(TAG, "user logged in, saving pending listens");
            listenSender.savePending();
        }
    }

}
