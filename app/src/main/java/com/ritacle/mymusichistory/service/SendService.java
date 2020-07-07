package com.ritacle.mymusichistory.service;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;


import java.io.IOException;
import java.util.concurrent.BlockingDeque;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

import static androidx.core.content.ContextCompat.getSystemService;
import static java.lang.Thread.sleep;


public class  SendService  implements Runnable, Callback<Scrobble> {

    private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
    public static final int WAIT_FOR_CONNECTION = 120000;//600000;
    public static final int WAIT_FOR_NEXT_LISTEN_SENDING = 5000;
    private final StatisticRestService mmhRestAPI;
    private BlockingDeque<Scrobble> listens;
    private Context context;


    public SendService(Context context, BlockingDeque<Scrobble> listens) {
        this.context = context;
        this.listens = listens;
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
        Log.d("Sending ", call.toString());
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public void run() {
        try {
            while (true) {
                if (haveNetworkConnection()) {
                    Scrobble listen = listens.take();
                    Log.d("Processing ", listen.getSong().getTitle());

                    try {
                        if (listenApplicableForSaving(listen)) {
                            Log.d("MMH", "Need be saved ");
                            sentToMMH(listen);
                            listens.put(listen);
                        }
                    } catch (Exception e) {
                        listens.put(listen);
                    }

                    sleep(WAIT_FOR_NEXT_LISTEN_SENDING);
                } else {
                    Log.d("Network is  unavailable. Waiting for  ", "" + WAIT_FOR_CONNECTION / 1000);
                    sleep(WAIT_FOR_CONNECTION);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }

    private boolean listenApplicableForSaving(Scrobble listen) throws IOException {
        Log.d("Checking listen sync: ", "" + listen.getUser().getId() + ":" + listen.getSyncId());
        Call<Boolean> call = mmhRestAPI.checkListenSync(listen.getUser().getId(), listen.getSyncId());

        Response<Boolean> response = call.execute();
        if ( response.body() == null || !response.isSuccessful()) {
            Log.e("MMH", "Sync check was unsuccessful ");
            throw new IOException("Sync check was unsuccessful");
        }
        return  !response.body();
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    private boolean sentToMMH(Scrobble listen) throws InterruptedException {
        Call<Scrobble> call = mmhRestAPI.addListenIntoStat(listen);
        //call.enqueue(this);

        try {
            Log.d("Server call started for: ", listen.toString());
            Response<Scrobble> response = call.execute();
            if (!response.isSuccessful()) {
                Log.d("Server call was unsuccessful: ", "response CODE : " + response.code() + " Listen: " + listen.getSong().getTitle());
            } else {
                Log.d("SUCCESS: ", " Listen: " + listen.getSong().getTitle());
            }
        } catch (IOException e) {
            Log.d("Sending to server failed", e.getMessage());
        }

        return false;
    }


    private boolean haveNetworkConnection() {
        boolean isConnected = false;

        ConnectivityManager cm = getSystemService(context, ConnectivityManager.class);

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


        return isConnected;
    }


}