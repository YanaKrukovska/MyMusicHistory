package com.ritacle.mymusichistory.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import com.ritacle.mymusichistory.scrobbling.ListenSender;

public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {

    final NetworkRequest networkRequest;
    private ListenSender listenSender;
    private static final String TAG = "Network Monitor";

    public ConnectionStateMonitor(ListenSender listenSender) {
        networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build();
        this.listenSender = listenSender;
    }

    public void enable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, this);
    }


    @Override
    public void onAvailable(Network network) {
        Log.d(TAG, "available");
        listenSender.savePending();
    }

}
