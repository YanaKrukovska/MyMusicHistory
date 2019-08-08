package com.ritacle.mymusichistory.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LastListen {


    private Long id;

    private String title;

    private String artist;

    private String album;
    @SerializedName("user")
    private String userMail;
    @SerializedName("date")
    private Date listenDate;


    public LastListen() {
    }

    public LastListen(Long id, String title, String artist, String album, String userMail, Date listenDate) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.userMail = userMail;
        this.listenDate = listenDate;
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

    public Date getDate() {
        return listenDate;
    }

    public void setDate(Date listenDate) {
        this.listenDate = listenDate;
    }


}


