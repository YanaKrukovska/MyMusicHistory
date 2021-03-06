package com.ritacle.mymusichistory.model;

public class TopArtist {
    private Long id;
    private String name;
    private int listenCount;
    private String userMail;

    public TopArtist() {
    }

    public TopArtist(String name, int listenCount, String userMail) {
        this.name = name;
        this.listenCount = listenCount;
        this.userMail = userMail;
    }

    public TopArtist(String artist, int listenCount) {
        this.name = artist;
        this.listenCount = listenCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


