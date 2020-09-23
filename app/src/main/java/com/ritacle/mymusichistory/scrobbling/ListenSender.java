package com.ritacle.mymusichistory.scrobbling;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.db.PendingListenDao;
import com.ritacle.mymusichistory.db.PendingListensDB;
import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.network.StatisticRestService;
import com.ritacle.mymusichistory.utils.NetworkUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class ListenSender implements Callback<Scrobble> {

    private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
    private static final String TAG = "Listen Sender";
    private final StatisticRestService mmhRestAPI;
    private BlockingDeque<Scrobble> listens = new LinkedBlockingDeque<>();
    private Context context;
    private PendingListensDB pendingListensDB;
    private PendingListenDao pendingListenDao;

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
        MMHApplication mmhApplication = (MMHApplication) context.getApplicationContext();
        this.pendingListensDB = mmhApplication.getPendingListensDB();
        this.pendingListenDao = pendingListensDB.pendingListenDao();
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
        Log.d("Failed to send ", call.toString());
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    public void sendListen() {
        int listensListSize = listens.size();
        if (NetworkUtil.hasNetworkConnection(context)) {

            for (int i = 0; i < listensListSize; i++) {
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
                            pendingListenDao.insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(listen));
                        }
                    }
                } catch (Exception e) {
                    pendingListensDB.pendingListenDao().insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(listen));
                }
            }
        } else {
            Log.d("Network was  unavailable at", new Date().toString());
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
        Call<ResponseMMH<Scrobble>> call = mmhRestAPI.addListenIntoStat(listen);
        Log.d("Server call started for: ", listen.toString());
        Response<ResponseMMH<Scrobble>> response = call.execute();
        if (!response.isSuccessful()) {
            Log.d("Server call was unsuccessful: ", "response CODE : " + response.code() + " Listen: " + listen.getSong().getTitle());
        } else {
            Log.d("SUCCESS: ", " Listen: " + listen.getSong().getTitle());
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void submit(Scrobble message) throws InterruptedException {
        listens.put(message);
        sendListen();
    }

    public Scrobble take() throws InterruptedException {
        return listens.take();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void savePending() {
        List<Scrobble> pending = pendingListensDB.convertAllPendingListensToScrobbles();
        pendingListensDB.clearAllTables();

        int pendingListSize = pending.size();
        if (pendingListSize == 0) {
            return;
        }

        for (int i = 0; i < pendingListSize; i++) {
            Scrobble listen = pending.get(i);
            Log.d("Processing pending", listen.getSong().getTitle());

            try {
                pending.remove(listen);
                if (listenApplicableForSaving(listen)) {
                    Log.d(TAG, "Saving listen " + listen.toString());
                    try {
                        sentToMMH(listen);
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                        pending.add(listen);
                    }
                }
            } catch (Exception e) {
                pending.add(listen);
            }
        }

        if (pending.size() != 0) {
            for (int i = 0; i < pending.size(); i++) {
                pendingListensDB.pendingListenDao().insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(pending.get(i)));
            }
        }
    }
}