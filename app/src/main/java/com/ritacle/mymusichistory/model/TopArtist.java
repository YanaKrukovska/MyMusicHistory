package com.ritacle.mymusichistory.model;

import com.google.gson.annotations.SerializedName;

public class TopArtist {


    private Long id;

    private String artist;
    private int listenCount;

    @SerializedName("user")
    private String userMail;


    public TopArtist() {
    }

    public TopArtist(String artist, int listenCount, String userMail) {
        this.artist = artist;
        this.listenCount = listenCount;
        this.userMail = userMail;
    }

    public TopArtist(String artist, int listenCount) {
        this.artist = artist;
        this.listenCount = listenCount;
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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


