package com.ritacle.mymusichistory.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "pending_listens")
public class PendingListenEntity {

    @PrimaryKey(autoGenerate = true)
    int id;
    String song;
    String album;
    String artist;
    String user;
    String listenDate;
    Long syncId;

    @Ignore
    public PendingListenEntity() {
    }

    @Ignore
    public PendingListenEntity(String name, String album, String artist, String user, String listenDate, Long syncId) {
        this.song = name;
        this.album = album;
        this.artist = artist;
        this.user = user;
        this.listenDate = listenDate;
        this.syncId = syncId;
    }

    public PendingListenEntity(int id, String song, String album, String artist, String user, String listenDate, Long syncId) {
        this.id = id;
        this.song = song;
        this.album = album;
        this.artist = artist;
        this.user = user;
        this.listenDate = listenDate;
        this.syncId = syncId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getListenDate() {
        return listenDate;
    }

    public void setListenDate(String listenDate) {
        this.listenDate = listenDate;
    }

    public Long getSyncId() {
        return syncId;
    }

    public void setSyncId(Long syncId) {
        this.syncId = syncId;
    }
}