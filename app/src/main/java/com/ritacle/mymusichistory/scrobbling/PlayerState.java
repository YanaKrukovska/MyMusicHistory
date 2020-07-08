package com.ritacle.mymusichistory.scrobbling;

import android.content.Context;
import android.media.session.PlaybackState;
import android.util.Log;

import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.utils.NotificationUtil;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerState {

    private static final String TAG = "Player State";

    private ListenRegistrar listenRegistrar;
    private PlaybackItem playbackItem;
    private Timer submissionTimer;
    private Context context;

    public PlayerState(Context context) {
        this.context = context;
        this.listenRegistrar = new ListenRegistrar(context);
    }

    public void setPlaybackState(PlaybackState playbackState) {
        if (playbackItem == null) {
            return;
        }
        playbackItem.updateAmountPlayed();
        int state = playbackState.getState();
        boolean isPlaying = state == PlaybackState.STATE_PLAYING;

        if (isPlaying) {
            Log.d(TAG, "Song playing");
            playbackItem.startPlaying();
            NotificationUtil.showNotification(context.getApplicationContext(), playbackItem.getSong(), "listening");
            scheduleSubmission();
        } else {
            Log.d(TAG, String.format("Track paused (state %d)", state));
            playbackItem.stopPlaying();
            NotificationUtil.hideListeningNowNotification(context.getApplicationContext());
            listenRegistrar.submit(playbackItem);
        }
    }


    public void setSong(Song song) {
        Song currentSong = null;
        boolean isPlaying = false;
        long now = System.currentTimeMillis();

        if (playbackItem != null) {
            currentSong = playbackItem.getSong();
            isPlaying = playbackItem.isPlaying();
        }

        if (song.isSameSong(currentSong)) {
            Log.d(TAG, String.format("Song metadata updated: %s", song));
            playbackItem.setSong(song);
        } else {
            Log.d(TAG, String.format("Changed Song: %s", song));
            if (playbackItem != null) {
                playbackItem.stopPlaying();
            }
            playbackItem = new PlaybackItem(song, now);
        }

        if (isPlaying) {
            NotificationUtil.showNotification(context, song, "listening");
            playbackItem.startPlaying();
            scheduleSubmission();
        }
    }

    private void scheduleSubmission() {
        Log.d(TAG, "Scheduling listen submission");

        if (submissionTimer != null) {
            submissionTimer.cancel();
        }

        long delay = listenRegistrar.getMillisecondsUntilListen(playbackItem);

        if (delay > -1) {
            Log.d(TAG, "Listen scheduled");
            submissionTimer = new Timer();
            submissionTimer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            listenRegistrar.submit(playbackItem);
                            scheduleSubmission();
                        }
                    },
                    delay);
        }
    }


}
