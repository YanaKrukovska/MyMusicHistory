package com.ritacle.mymusichistory.scrobbling;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ritacle.mymusichistory.MainActivity;
import com.ritacle.mymusichistory.R;
import com.ritacle.mymusichistory.model.scrobbler_model.Album;
import com.ritacle.mymusichistory.model.scrobbler_model.Artist;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.service.SendService;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class PlayerState {

    private static final String TAG = "Player state";

    private PlaybackItem playbackItem;
    private Timer submissionTimer;
    private Context context;
    private long MINIMUM_SCROBBLE_TIME = 10 * 1000;
    private static final int SCROBBLE_THRESHOLD = 15 * 1000;
    private BlockingDeque<Scrobble> listens;
    private SendService sendService;

    public PlayerState(Context context) {
        this.context = context;
        this.listens = new LinkedBlockingDeque<>();
        this.sendService = new SendService(context, listens);
        performOnBackgroundThread(sendService);
    }

    private void performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();

    }

    public void setPlaybackState(PlaybackState playbackState) {
        if (playbackItem == null) {
            return;
        }

        playbackItem.updateAmountPlayed();

        int state = playbackState.getState();
        boolean isPlaying = state == PlaybackState.STATE_PLAYING;

        if (isPlaying) {
            Log.d(TAG, "Track playing");
            playbackItem.startPlaying();
            showNotification(context.getApplicationContext(), playbackItem.getTrack(), "now playing");
            scheduleSubmission();
        } else {
            Log.d(TAG, String.format("Track paused (state %d)", state));
            submit(playbackItem);
        }
    }

    public void setTrack(Track track) {
        Track currentTrack = null;
        boolean isPlaying = false;
        long now = System.currentTimeMillis();

        if (playbackItem != null) {
            currentTrack = playbackItem.getTrack();
            isPlaying = playbackItem.isPlaying();
        }

        if (track.isSameTrack(currentTrack)) {
            Log.d(TAG, String.format("Track metadata updated: %s", track));
            playbackItem.setTrack(track);
        } else {
            Log.d(TAG, String.format("Changed track: %s", track));
            if (playbackItem != null) {
                playbackItem.stopPlaying();
            }
            playbackItem = new PlaybackItem(track, now);
        }

        if (isPlaying) {
            showNotification(context, track, "listening");
            playbackItem.startPlaying();
            scheduleSubmission();
        }
    }

    public long getMillisecondsUntilScrobble(PlaybackItem playbackItem) {
        if (playbackItem == null) {
            return -1;
        }

        long duration = playbackItem.getTrack().getDuration();
        if (duration < MINIMUM_SCROBBLE_TIME) {
            return -1;
        }
        long scrobbleThreshold = Math.min(duration / 2, SCROBBLE_THRESHOLD);
        long nextScrobbleAt = playbackItem.getPlaysScrobbled() * duration + scrobbleThreshold;

        return Math.max(0, nextScrobbleAt - playbackItem.getAmountPlayed());
    }


    public void submit(PlaybackItem playbackItem) {
        playbackItem.updateAmountPlayed();
        Track track = playbackItem.getTrack();

        long timestamp = playbackItem.getTimestamp();
        long duration = track.getDuration();
        long playTime = playbackItem.getAmountPlayed();

        if (playTime < 1) {
            return;
        }

        if (duration == 0) {
            duration = playTime;
        }

        int playCount = (int) (playTime / duration);
        long scrobbleThreshold = Math.min(SCROBBLE_THRESHOLD, duration / 2);

        if (duration < MINIMUM_SCROBBLE_TIME) {
            return;
        }

        if (playTime % duration > scrobbleThreshold) {
            playCount++;
        }

        int newScrobbles = playCount - playbackItem.getPlaysScrobbled();
        for (int i = playbackItem.getPlaysScrobbled(); i < playCount; i++) {
            int itemTimestamp = (int) ((timestamp + i * duration) / 1000);

            // Scrobble scrobble = Scrobble.builder().track(track).timestamp(itemTimestamp).build();
            // pending.add(scrobble);
            Song song = new Song(playbackItem.getTrack().getTitle(), new Album(playbackItem.getTrack().getAlbum(), new Artist(playbackItem.getTrack().getArtist())), playbackItem.getTrack().getDuration());
            registerSong(song);
            playbackItem.addScrobble();
        }

        if (newScrobbles > 0) {
            Log.d(TAG, String.format("Queued %d scrobbles", playCount));
        }

        showNotification(context, track, "scrobbled");
    }

    private void scheduleSubmission() {
        Log.d(TAG, "Scheduling scrobble submission");

        if (submissionTimer != null) {
            submissionTimer.cancel();
        }

        long delay = getMillisecondsUntilScrobble(playbackItem);

        if (delay > -1) {
            Log.d(TAG, "Scrobble scheduled");
            submissionTimer = new Timer();
            submissionTimer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            // scrobbler.submit(playbackItem);
                            submit(playbackItem);
                            scheduleSubmission();
                        }
                    },
                    delay);
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
            listens.put(listen);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private long getUserID(String mail) {
        return ("vkrukovskyy@gmail.com".equals(mail) || "v.krukovskyy@gmail.com".equals(mail)) ? 2L : 1L;
    }

    private void showNotification(Context context, Track receivedSong, String status) {
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
                .setContentTitle(receivedSong.getArtist() + " — " + receivedSong.getTitle())
                .setContentText(status);
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        notificationManager.notify(0, builder.build());

    }


}
