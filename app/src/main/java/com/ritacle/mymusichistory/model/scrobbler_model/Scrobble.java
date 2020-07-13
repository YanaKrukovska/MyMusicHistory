package com.ritacle.mymusichistory.model.scrobbler_model;

import android.annotation.TargetApi;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.StringJoiner;

public class Scrobble {

    private User user;

    private Song song;

    private Date listenDate;

    private long syncId;

    public Scrobble() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Date getListenDate() {
        return listenDate;
    }

    public void setListenDate(Date listenDate) {
        this.listenDate = listenDate;
    }


    public long getSyncId() {
        return syncId;
    }

    public void setSyncId(long syncId) {
        this.syncId = syncId;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        return new StringJoiner(", ", Scrobble.class.getSimpleName() + "[", "]")
                .add("user=" + user.getMail())
                .add("song=" + song.toString())
                .add("listenDate=" + listenDate)
                .toString();

    }
}