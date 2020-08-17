package com.ritacle.mymusichistory.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.MainActivity;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.SongStatistic;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.network.ReportRestService;
import com.ritacle.mymusichistory.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class NotificationUtil {

    private static final int NOW_PLAYING_ID = 0;
    private static final String CHANNEL_ID_NOW_LISTENING = "now_listening";
    private static final String TAG = "Notification Util";

    private static final String ONE_LISTEN_TEXT = " listen";
    private static final String MULTIPLE_LISTEN_TEXT = " listens";

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationUtil(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel nowListeningChannel =
                    new NotificationChannel(
                            CHANNEL_ID_NOW_LISTENING, "Currently playing song",
                            NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(nowListeningChannel);
        }
    }

    public void showListeningNowNotification(Song receivedSong, String status) {

        MMHApplication application = (MMHApplication) context.getApplicationContext();
        if (!application.isLoggedIn()) {
            return;
        }

        if (NetworkUtil.hasNetworkConnection(context)) {
            SharedPreferences sharedPreferences = application.getSharedPreferences("login", MODE_PRIVATE);
            String mail = sharedPreferences.getString("mail", "");
            ReportRestService service = RetrofitClientInstance.getRetrofitInstance().create(ReportRestService.class);
            Call<SongStatistic> songStatisticsCall = service.getSongListenCount(mail, receivedSong.getAlbum().getArtist().getName(), receivedSong.getTitle());
            songStatisticsCall.enqueue(new Callback<SongStatistic>() {
                @Override
                public void onResponse(@NonNull Call<SongStatistic> call, @NonNull Response<SongStatistic> response) {
                    if (response.body() != null) {
                        SongStatistic songStatistic = response.body();
                        Log.d(TAG, "Listen count of " + receivedSong.getTitle() + " is: " + songStatistic.getListenCount());
                        createNotification(receivedSong, songStatistic.getListenCount(), status);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SongStatistic> call, @NonNull Throwable t) {
                    Log.d(TAG, "failed to get listen count");
                    createNotification(receivedSong, -1, status);
                }
            });
        } else {
            createNotification(receivedSong, -1, status);
        }

    }

    public void hideListeningNowNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    private void createNotification(Song receivedSong, int listenCount, String status) {

        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent clickPendingIntent =
                PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notification =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_logo_notif)
                        .setContentTitle(String.format("%s — %s", receivedSong.getAlbum().getArtist().getName(), receivedSong.getTitle()))
                        .setOngoing(false)
                        .setCategory(Notification.CATEGORY_STATUS);
        notification.setContentIntent(clickPendingIntent);

        if (listenCount > 0) {
            if (listenCount == 1) {
                notification.setContentText(listenCount + ONE_LISTEN_TEXT);
            } else {
                notification.setContentText(listenCount + MULTIPLE_LISTEN_TEXT);
            }
            notification.setSubText(status);
        } else {
            notification.setContentText(status);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID_NOW_LISTENING);
        }

        notificationManager.notify(NOW_PLAYING_ID, notification.build());
    }

}
