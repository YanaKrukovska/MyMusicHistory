package com.ritacle.mymusichistory.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.scrobbling.ListenSender;

import org.apache.commons.lang3.StringUtils;

public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback implements SharedPreferences.OnSharedPreferenceChangeListener {
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

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    public void enable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(networkRequest, this);
    }


    @Override
    public void onAvailable(Network network) {
        MMHApplication mmhApplication = (MMHApplication) context;
        Log.d(TAG, "available");
        if (mmhApplication.isLoggedIn()) {
            Log.d(TAG, "user logged in, saving pending listens");
            listenSender.savePending();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("mail") && !StringUtils.isAllBlank(sharedPreferences.getString(key, ""))) {
            Log.d(TAG, "shared preferences changed, saving pending listens");
            listenSender.savePending();
        }
    }
}
