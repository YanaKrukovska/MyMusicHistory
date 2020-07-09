package com.ritacle.mymusichistory.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.ritacle.mymusichistory.MainActivity;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;

public class NotificationUtil {

    private static final int NOW_PLAYING_ID = 0;
    private static final String CHANNEL_ID_NOW_LISTENING = "now_listening";

    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationUtil(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel nowListeningChannel =
                    new NotificationChannel(
                            CHANNEL_ID_NOW_LISTENING, "CURRENTLY_LISTENING",
                            NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(nowListeningChannel);
        }
    }

    public void showListeningNowNotification(Song receivedSong, String status) {

        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent clickPendingIntent =
                PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder notification =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_logo_notif)
                        .setContentTitle(String.format("%s — %s", receivedSong.getAlbum().getArtist().getName(), receivedSong.getTitle()))
                        .setContentText(status)
                        .setOngoing(true)
                        .setCategory(Notification.CATEGORY_STATUS)
                        .setContentIntent(clickPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID_NOW_LISTENING);
        }

        notificationManager.notify(NOW_PLAYING_ID, notification.build());
    }

    public void hideListeningNowNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

}
