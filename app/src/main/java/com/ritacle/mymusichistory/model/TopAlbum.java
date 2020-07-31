package com.ritacle.mymusichistory.model;

public class TopAlbum {

    private Long id;
    private String albumName;
    private String artist;
    private String userMail;
    private int listenCount;

    public TopAlbum() {
    }

    public TopAlbum(String album, String artist, int listenCount, String userMail) {
        this.albumName = album;
        this.artist = artist;
        this.listenCount = listenCount;
        this.userMail = userMail;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return albumName;
    }

    public void setAlbum(String album) {
        this.albumName = album;
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


