package com.ritacle.mymusichistory.scrobbling;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.service.StatisticRestService;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

import static java.lang.Thread.sleep;


public class ListenSender implements Callback<Scrobble> {

    private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
    public static final int WAIT_FOR_CONNECTION = 120000;//600000;
    public static final int WAIT_FOR_NEXT_LISTEN_SENDING = 5000;
    private final StatisticRestService mmhRestAPI;
    private BlockingDeque<Scrobble> listens = new LinkedBlockingDeque<>();
    private BlockingDeque<Scrobble> pendingListens = new LinkedBlockingDeque<>();
    private Context context;


    public ListenSender(Context context) {
        this.context = context;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mmhRestAPI = retrofit.create(StatisticRestService.class);

    }


    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Scrobble> call, Response<Scrobble> response) {

        if (response.isSuccessful()) {
        } else {
            //    Log.d("Sending response was unsuccessful: ", "" + response.code() + " "
            //           + response.message() + " " + response.body().toString());
            //     try {
            //       listens.put(response.body());
            //    } catch (InterruptedException e) {
            //         Log.d("Thread was interrupted", "" + response.body().toString());
            //     }

        }
    }

    @Override
    public void onFailure(Call<Scrobble> call, Throwable t) {
       /* try {
            listens.put(call.execute().body());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }*/
        Log.d("Failed to send ", call.toString());
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    public void sendListen() {

        if (hasNetworkConnection()) {

            for (int i = 0; i < listens.size(); i++) {

                Scrobble listen = null;
                try {
                    listen = listens.take();
                    Log.d("Processing ", listen.getSong().getTitle());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    if (listen != null && listenApplicableForSaving(listen)) {
                        Log.d("MMH", "Need be saved ");
                        try {
                            sentToMMH(listen);
                        } catch (IOException e) {
                            Log.d("Sending to server failed", e.getMessage());
                            listens.put(listen);
                        }
                    }
                } catch (Exception e) {
                    try {
                        listens.put(listen);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        } else {
            Log.d("Network is  unavailable. Waiting for  ", "" + WAIT_FOR_CONNECTION / 1000);
        }


    }


    private boolean listenApplicableForSaving(Scrobble listen) throws IOException {
        Log.d("Checking listen sync: ", "" + listen.getUser().getId() + ":" + listen.getSyncId());
        Call<Boolean> call = mmhRestAPI.checkListenSync(listen.getUser().getId(), listen.getSyncId());

        Response<Boolean> response = call.execute();
        if (response.body() == null || !response.isSuccessful()) {
            Log.e("MMH", "Sync check was unsuccessful ");
            throw new IOException("Sync check was unsuccessful");
        }
        return !response.body();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    private boolean sentToMMH(Scrobble listen) throws IOException {
        Call<Scrobble> call = mmhRestAPI.addListenIntoStat(listen);
        //call.enqueue(this);
        Log.d("Server call started for: ", listen.toString());
        Response<Scrobble> response = call.execute();
        if (!response.isSuccessful()) {
            Log.d("Server call was unsuccessful: ", "response CODE : " + response.code() + " Listen: " + listen.getSong().getTitle());
        } else {
            Log.d("SUCCESS: ", " Listen: " + listen.getSong().getTitle());
        }


        return false;
    }


    private boolean hasNetworkConnection() {

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

    public void submit(Scrobble message) throws InterruptedException {
        listens.put(message);
        sendListen();
    }

    public Scrobble take() throws InterruptedException {
        return listens.take();
    }
}