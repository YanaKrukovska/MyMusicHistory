package com.ritacle.mymusichistory.scrobbling;

import androidx.annotation.NonNull;

import com.ritacle.mymusichistory.model.scrobbler_model.Song;

import java.util.Locale;

public class PlaybackItem {

    private final long timestamp;
    private Song song;
    private long amountPlayed;
    private long playbackStartTime;
    private boolean isPlaying;
    private int playsScrobbled;

    public PlaybackItem(Song song, long timestamp) {
        this.song = song;
        this.timestamp = timestamp;
    }

    public void updateSong(Song song) {
        this.song = song;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getAmountPlayed() {
        return amountPlayed;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getPlaysScrobbled() {
        return playsScrobbled;
    }

    public void addScrobble() {
        playsScrobbled++;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void startPlaying() {
        if (!isPlaying) {
            playbackStartTime = System.currentTimeMillis();
        }
        isPlaying = true;
    }

    public void stopPlaying() {
        updateAmountPlayed();
        isPlaying = false;
    }

    public void updateAmountPlayed() {
        if (!isPlaying()) {
            return;
        }
        long now = System.currentTimeMillis();
        long start = playbackStartTime;
        amountPlayed += now - start;
        playbackStartTime = now;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                Locale.getDefault(),
                "PlaybackItem{Track=%s, artist=%s, timestamp=%d, isPlaying=%s, amountPlayed=%d, playbackStartTime=%d, "
                        + "playsScrobbled=%d}",
                song.getTitle(),
                song.getAlbum().getArtist().getName(),
                timestamp,
                Boolean.toString(isPlaying),
                amountPlayed,
                playbackStartTime,
                playsScrobbled);
    }
}
