package com.ritacle.mymusichistory.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtil {

    public static boolean hasNetworkConnection(Context context) {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        isConnected = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        isConnected = true;
                    }
                }
            }
        } else {
            Network[] networks;
            if (cm != null) {
                networks = cm.getAllNetworks();
                for (Network network : networks) {
                    NetworkInfo networkInfo = cm.getNetworkInfo(network);
                    if ((networkInfo.getTypeName().equalsIgnoreCase("MOBILE")
                            || (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))) && networkInfo.isConnected()) {
                        isConnected = true;
                        break;
                    }
                }

            }
        }
        return isConnected;
    }

}
