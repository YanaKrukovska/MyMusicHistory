package com.ritacle.mymusichistory.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ListenAmount {


    private Long id;

    private String title;
    private String artist;
    private String album;
    private int listenCount;

    @SerializedName("user")
    private String userMail;


    public ListenAmount() {
    }

    public ListenAmount( String title, String artist, String album, int listenCount, String userMail) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.listenCount = listenCount;
        this.userMail = userMail;
    }

    public ListenAmount( String title, String artist, String album, int listenCount) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.listenCount = listenCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public int getListenCount() {
        return listenCount;
    }

    public void setListenCount(int listenCount) {
        this.listenCount = listenCount;
    }

    public String getUserMail() {
        return userMail;
    }
}


