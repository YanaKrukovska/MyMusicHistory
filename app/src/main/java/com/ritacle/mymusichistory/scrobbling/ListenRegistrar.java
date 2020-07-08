package com.ritacle.mymusichistory.scrobbling;

import android.content.Context;
import android.util.Log;

import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.utils.NotificationUtil;

import java.util.Date;
import java.util.Random;

public class ListenRegistrar {

    private long MINIMUM_LISTENING_TIME = 10 * 1000;
    private static final int LISTEN_THRESHOLD = 15 * 1000;

    private static final String TAG = "Listen registrar";
    private Context context;

    public ListenRegistrar(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void submit(PlaybackItem playbackItem) {
        playbackItem.updateAmountPlayed();
        Song song = playbackItem.getSong();
        long timestamp = playbackItem.getTimestamp();
        long duration = song.getDuration();
        long playTime = playbackItem.getAmountPlayed();

        if (playTime < 1) {
            return;
        }

        if (duration == 0) {
            duration = playTime;
        }

        int playCount = (int) (playTime / duration);
        long listenThreshold = Math.min(LISTEN_THRESHOLD, duration / 2);

        if (duration < MINIMUM_LISTENING_TIME) {
            return;
        }

        if (playTime % duration > listenThreshold) {
            playCount++;
        }

        int newListens = playCount - playbackItem.getPlaysScrobbled();
        for (int i = playbackItem.getPlaysScrobbled(); i < playCount; i++) {
            registerSong(song);
            playbackItem.addScrobble();
            NotificationUtil.showNotification(context, song, "saved");
        }

        if (newListens > 0) {
            Log.d(TAG, String.format("Queued %d listens", playCount));
        }

    }

    private void registerSong(Song song) {
        Scrobble listen = new Scrobble();
        User user = new User();
        user.setMail("jana.krua@gmail.com");
        user.setId(getUserID(user.getMail()));
        listen.setUser(user);
        listen.setSong(song);
        listen.setListenDate(new Date());
        listen.setSyncId(new Random().nextLong());

        try {
            MMHApplication.submit(listen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long getMillisecondsUntilListen(PlaybackItem playbackItem) {
        if (playbackItem == null) {
            return -1;
        }

        long duration = playbackItem.getSong().getDuration();
        if (duration < MINIMUM_LISTENING_TIME) {
            return -1;
        }
        long listenThreshold = Math.min(duration / 2, LISTEN_THRESHOLD);
        long nextListenAt = playbackItem.getPlaysScrobbled() * duration + listenThreshold;

        return Math.max(0, nextListenAt - playbackItem.getAmountPlayed());
    }

    private long getUserID(String mail) {
        return ("vkrukovskyy@gmail.com".equals(mail) || "v.krukovskyy@gmail.com".equals(mail)) ? 2L : 1L;
    }


}
