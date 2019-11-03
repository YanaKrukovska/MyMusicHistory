package com.ritacle.mymusichistory.scanner.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.scanner.model.Listen;
import com.ritacle.mymusichistory.scanner.rest.ScannerRestService;

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


public class SenderServiceImpl implements Runnable, Callback<Listen> {

    private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
    public static final int WAIT_FOR_CONNECTION = 120000;//600000;
    public static final int WAIT_FOR_NEXT_LISTEN_SENDING = 10000;
    private final ScannerRestService mmhRestAPI;
    private BlockingDeque<Listen> listens;
    private Context context;


    public SenderServiceImpl(Context context, BlockingDeque<Listen> listens) {
        this.context = context;
        this.listens = listens;
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mmhRestAPI = retrofit.create(ScannerRestService.class);

    }


    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Listen> call, Response<Listen> response) {

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
    public void onFailure(Call<Listen> call, Throwable t) {
        Log.d("Sending ", call.toString());
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (haveNetworkConnection()) {
                    Listen listen = listens.take();
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
                    Log.d("No network. Wait:", "" + WAIT_FOR_CONNECTION / 1000);
                    sleep(WAIT_FOR_CONNECTION);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }

    private boolean listenApplicableForSaving(Listen listen) throws IOException {
        Log.d("Checking listen sync: ", "" + listen.getUser().getId() + ":" + listen.getSyncId());
        Call<Boolean> call = mmhRestAPI.checkListenSync(listen.getUser().getId(), listen.getSyncId());

        Response<Boolean> response = call.execute();
        if (response.body() == null || !response.isSuccessful()) {
            Log.e("MMH", "Sync check was unsuccessful ");
            throw new IOException("Sync check was unsuccessful");
        }
        return !response.body();
    }

    private boolean sentToMMH(Listen listen) throws InterruptedException {
        Call<Listen> call = mmhRestAPI.addListenIntoStat(listen);
        //call.enqueue(this);

        try {
            Log.d("Call started for: ", listen.toString());
            Response<Listen> response = call.execute();
            if (!response.isSuccessful()) {
                Log.d("Call was unsuccessful: ", "response CODE : " + response.code() + " Listen: " + listen.getSong().getTitle());
            } else {
                Log.d("SUCCESS: ", " Listen: " + listen.getSong().getTitle());
            }
        } catch (IOException e) {
            Log.d("Sending  failed", e.getMessage());
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
