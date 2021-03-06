package com.ritacle.mymusichistory.scrobbling;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.db.PendingListenDao;
import com.ritacle.mymusichistory.db.PendingListensDB;
import com.ritacle.mymusichistory.model.ResponseMMH;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.network.ListenRestService;
import com.ritacle.mymusichistory.utils.NetworkUtil;
import com.ritacle.mymusichistory.utils.NotificationUtil;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class ListenSender implements Callback<Scrobble> {

    private static final String BASE_URL = "https://my-music-history.herokuapp.com/";
    private static final String TAG = "Listen Sender";
    private final ListenRestService mmhRestAPI;
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

        mmhRestAPI = retrofit.create(ListenRestService.class);
        MMHApplication mmhApplication = (MMHApplication) context.getApplicationContext();
        this.pendingListensDB = mmhApplication.getPendingListensDB();
        this.pendingListenDao = pendingListensDB.pendingListenDao();
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<Scrobble> call, Response<Scrobble> response) {

    }

    @Override
    public void onFailure(Call<Scrobble> call, @NonNull Throwable t) {
        Log.d("Failed to send ", call.toString());
    }

    @SuppressLint("LongLogTag")
    public void sendListen(Scrobble listen) {
        if (NetworkUtil.hasNetworkConnection(context)) {
            try {
                if (listen != null && listenApplicableForSaving(listen)) {
                    Log.d("MMH", "Need be saved ");
                    try {
                        sentToMMH(listen);
                    } catch (IOException e) {
                        if (e.getMessage() != null) {
                            Log.d("Sending to server failed", e.getMessage());
                        }
                        pendingListenDao.insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(listen));
                    }
                }
            } catch (Exception e) {
                pendingListenDao.insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(listen));
            }

        } else {
            Log.d("Network was unavailable at", new Date().toString());
            pendingListenDao.insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(listen));
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

    @SuppressLint("LongLogTag")
    private void sentToMMH(Scrobble listen) throws IOException {
        Call<ResponseMMH<Scrobble>> call = mmhRestAPI.addListenIntoStat(listen);
        Log.d("Server call started for: ", listen.toString());
        Response<ResponseMMH<Scrobble>> response = call.execute();
        if (!response.isSuccessful()) {
            Log.d("Server call was unsuccessful: ", "response CODE : " + response.code() + " Listen: " + listen.getSong().getTitle());
        } else {
            Log.d("SUCCESS: ", " Listen: " + listen.getSong().getTitle());
        }
    }

    public void submit(Scrobble listen) {
        sendListen(listen);
    }

    public void savePending() {
        List<Scrobble> pending = pendingListensDB.convertAllPendingListenEntitiesToScrobbles(pendingListenDao.loadAllListens());
        List<Scrobble> failedPending = new LinkedList<>();
        pendingListensDB.clearAllTables();

        int pendingListSize = pending.size();
        if (pendingListSize == 0) {
            return;
        }

        NotificationUtil notificationUtil = new NotificationUtil(context);
        notificationUtil.showSavingPendingListensNotification();

        for (int i = 0; i < pendingListSize; i++) {
            Scrobble listen = pending.get(i);
            Log.d("Processing pending", listen.getSong().getTitle());
            try {
                if (listenApplicableForSaving(listen)) {
                    Log.d(TAG, "Saving listen " + listen.toString());
                    try {
                        sentToMMH(listen);
                    } catch (IOException e) {
                        if (e.getMessage() != null) {
                            Log.d(TAG, e.getMessage());
                        }
                        failedPending.add(listen);
                    }
                }
            } catch (Exception e) {
                failedPending.add(listen);
            }
        }

        if (failedPending.size() != 0) {
            for (int i = 0; i < failedPending.size(); i++) {
                pendingListenDao.insertListen(pendingListensDB.convertScrobbleToPendingListenEntity(failedPending.get(i)));
            }
        }

        notificationUtil.hideSavingPendingListensNotification();

    }
}