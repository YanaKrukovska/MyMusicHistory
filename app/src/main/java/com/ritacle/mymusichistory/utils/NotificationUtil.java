package com.ritacle.mymusichistory.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.ritacle.mymusichistory.MainActivity;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;

public class NotificationUtil {

    public static void showNotification(Context context, Song receivedSong, String status) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("0",
                    "CURRENTLY_LISTENING", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "0")
                .setSmallIcon(R.drawable.ic_logo_notif)
                .setAutoCancel(true)
                .setTicker("cat")
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setContentTitle(receivedSong.getAlbum().getArtist().getName() + " — " + receivedSong.getTitle())
                .setContentText(status);
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        notificationManager.notify(0, builder.build());

    }

}
