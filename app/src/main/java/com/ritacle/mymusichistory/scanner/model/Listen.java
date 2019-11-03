package com.ritacle.mymusichistory.scanner.model;

import java.util.Date;
import java.util.StringJoiner;

public class Listen {

    private User user;

    private Song song;

    private Date listenDate;

    private long syncId;

    public Listen() {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Listen.class.getSimpleName() + "[", "]")
                .add("user=" + user.getMail())
                .add("song=" + song.toString())
                .add("listenDate=" + listenDate)
                .toString();
    }
}
