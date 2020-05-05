package com.ritacle.mymusichistory.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ritacle.mymusichistory.MainActivity;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.scrobbler_model.Album;
import com.ritacle.mymusichistory.model.scrobbler_model.Artist;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class ListeningBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;
    private Song currentSong = null;
    private Date startPlayDate = null;
    private static final double SONG_TIME_THRESHOLD = 0.5d;
    private BlockingDeque<Scrobble> listens;
    public static final int START_PLAY_STATE = 3;
    private static final String CHANGE_SONG_ACTION = "com.android.music.metachanged";
    private static final String CHANGE_STATE_ACTION = "com.android.music.playstatechanged";

    public ListeningBroadcastReceiver() {

    }

    public ListeningBroadcastReceiver(BlockingDeque<Scrobble> listens, MainActivity mainActivity) {
        this.listens = listens;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (!CHANGE_STATE_ACTION.equals(action)) {
            return;
        }
        String cmd = intent.getStringExtra("command");

        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String songTitle = intent.getStringExtra("track");
        int playerState = intent.getIntExtra("playerState", 0);
        boolean playing = intent.getBooleanExtra("playing", false);

        Log.d("***** Player State = ", "" + intent.getAction());
        Song receivedSong = new Song(songTitle, new Album(album, new Artist(artist)), intent.getLongExtra("duration", 1l));


        boolean isSongChange = (playerState == START_PLAY_STATE && currentSong != null && !currentSong.equals(receivedSong));

        if (currentSong == null) {
            currentSong = receivedSong;
            startPlayDate = new Date();
        }

        showNotification(context, receivedSong);
        Log.d("POSITION = ", "" + intent.getLongExtra("position", 1));


        if (START_PLAY_STATE == 3 && isSongChange) {

        Date newTime = new Date();
        long playedTime = (newTime.getTime() - startPlayDate.getTime());
            double ratio = (double) playedTime / (double) currentSong.getDuration();
        Log.d("***** TIME = ", "" + playedTime + " /" + currentSong.getDuration() + "= " + ratio);

        startPlayDate = newTime;


        if (ratio >= SONG_TIME_THRESHOLD) {
            registerSong(currentSong);
        }


        currentSong = receivedSong;

        Log.d("Music", action + ":" + artist + " : " + album + " : " + songTitle);
         } //else if (CHANGE_STATE_ACTION.equals(action)) {



       // }

    }

    private void showNotification(Context context, Song recievedSong) {
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
                .setContentTitle(recievedSong.getAlbum().getArtist().getName() + " — " + recievedSong.getTitle())
                .setContentText("listening");
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        notificationManager.notify(0, builder.build());

    }

    private void registerSong(Song song) {
        Scrobble listen = new Scrobble();
        User user = new User();
        user.setMail(mainActivity.getAccountName());
        user.setId(getUserID(user.getMail()));
        listen.setUser(user);
        listen.setSong(song);
        listen.setListenDate(new Date());
        listen.setSyncId(new Random().nextLong());


        try {
            listens.put(listen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private long getUserID(String mail) {
        return ("vkrukovskyy@gmail.com".equals(mail) || "v.krukovskyy@gmail.com".equals(mail)) ? 2L : 1L;
    }


}
