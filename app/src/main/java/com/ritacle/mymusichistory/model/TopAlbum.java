package com.ritacle.mymusichistory.model;

import com.google.gson.annotations.SerializedName;

public class TopAlbum {

    private Long id;
    private String album;
    private int listenCount;

    @SerializedName("user")
    private String userMail;

    public TopAlbum() {
    }

    public TopAlbum(String album, int listenCount, String userMail) {
        this.album = album;
        this.listenCount = listenCount;
        this.userMail = userMail;
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


