package com.ritacle.mymusichistory.scrobbling;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.ritacle.mymusichistory.MMHApplication;
import com.ritacle.mymusichistory.model.scrobbler_model.Scrobble;
import com.ritacle.mymusichistory.model.scrobbler_model.Song;
import com.ritacle.mymusichistory.model.scrobbler_model.User;
import com.ritacle.mymusichistory.utils.DataUtils;
import com.ritacle.mymusichistory.utils.NotificationUtil;

import java.text.DateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class ListenRegistrar implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final long MINIMUM_LISTENING_TIME = 15 * 1000;

    private static final String TAG = "Listen registrar";
    private Context context;
    private NotificationUtil notificationUtil;
    private int listenThresholdPercent;

    private ListenSender listenSender;

    public ListenRegistrar(Context context, NotificationUtil notificationUtil, ListenSender listenSender) {
        this.context = context;
        this.notificationUtil = notificationUtil;
        this.listenSender = listenSender;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.registerOnSharedPreferenceChangeListener(this);
        this.listenThresholdPercent = settings.getInt("listening_threshold", 50);
    }

    public Context getContext() {
        return context;
    }

    public void submit(PlaybackItem playbackItem) {
        playbackItem.updateAmountPlayed();
        Song song = playbackItem.getSong();
        long duration = song.getDuration();
        long playTime = playbackItem.getAmountPlayed();

        if (playTime < 1) {
            return;
        }

        if (duration == 0) {
            duration = playTime;
        }

        int playCount = (int) (playTime / duration);
        long listenThreshold = (duration * listenThresholdPercent) / 100;

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
            notificationUtil.showListeningNowNotification(song, "Saved");
        }

        if (newListens > 0) {
            Log.d(TAG, String.format("Queued %d listens", playCount));
        }

    }

    private void registerSong(Song song) {
        MMHApplication application = (MMHApplication) context.getApplicationContext();
        if (!application.isLoggedIn()) {
            return;
        }

        Scrobble listen = new Scrobble();
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        User user = new User();
        user.setMail(sharedPreferences.getString("mail", ""));
        user.setId(sharedPreferences.getLong("user_id", -1));
        listen.setUser(user);
        listen.setSong(song);
        setListenDateTime(listen);
        listen.setSyncId(new Random().nextLong());

        listenSender.submit(listen);
    }

    private void setListenDateTime(Scrobble listen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            utc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            listen.setListenDate(DataUtils.createFullDate(utc.getYear(), utc.getMonthValue(), utc.getDayOfMonth(), utc.getHour(), utc.getMinute(), utc.getSecond()));
        } else {
            DateFormat df = DateFormat.getDateTimeInstance(1, 1, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("utc"));
            listen.setListenDate(df.getCalendar().getTime());
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
        long listenThreshold = (duration * listenThresholdPercent) / 100;
        long nextListenAt = playbackItem.getPlaysScrobbled() * duration + listenThreshold;

        return Math.max(0, nextListenAt - playbackItem.getAmountPlayed());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("listening_threshold")) {
            listenThresholdPercent = sharedPreferences.getInt("listening_threshold", 50);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
