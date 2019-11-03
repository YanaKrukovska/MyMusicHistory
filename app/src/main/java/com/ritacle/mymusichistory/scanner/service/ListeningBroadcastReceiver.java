package com.ritacle.mymusichistory.scanner.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ritacle.mymusichistory.scanner.model.Album;
import com.ritacle.mymusichistory.scanner.model.Artist;
import com.ritacle.mymusichistory.scanner.model.Listen;
import com.ritacle.mymusichistory.scanner.model.Song;
import com.ritacle.mymusichistory.scanner.model.User;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class ListeningBroadcastReceiver extends BroadcastReceiver {

    public static final int START_PLAY_STATE = 3;

    private Song currentSong = null;
    private Date startPlayDate = null;
    private static final double SONG_TIME_THRESHOLD = 0.5d;
    private BlockingDeque<Listen> listens;

    private static final String CHANGE_STATE_ACTION = "com.android.music.playstatechanged";
    private String userAccount;

    public ListeningBroadcastReceiver(BlockingDeque<Listen> listens) {
        this.listens = listens;
        this.userAccount = "";
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (!CHANGE_STATE_ACTION.equals(action)) {
            return;
        }

        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String songTitle = intent.getStringExtra("track");
        int playerState = intent.getIntExtra("playerState", 0);


        Log.d("***** Player State = ", "" + playerState);
        Song receivedSong = new Song(songTitle, new Album(album, new Artist(artist)), intent.getLongExtra("duration", 1L));

        boolean isSongChange = (playerState == START_PLAY_STATE && currentSong != null && !currentSong.equals(receivedSong));

        if (currentSong == null) {
            currentSong = receivedSong;
            startPlayDate = new Date();
        }


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
        }

    }

    private void registerSong(Song song) {

        try {
            listens.put(createListen(song));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Listen createListen(Song song) {
        Listen listen = new Listen();
        User user = new User();
        user.setMail(userAccount);
        user.setId(getUserID(user.getMail()));
        listen.setUser(user);
        listen.setSong(song);
        listen.setListenDate(new Date());
        listen.setSyncId(new Random().nextLong());
        return listen;
    }


    private long getUserID(String mail) {
        return ("vkrukovskyy@gmail.com".equals(mail) || "v.krukovskyy@gmail.com".equals(mail)) ? 2L : 1L;
    }


}
