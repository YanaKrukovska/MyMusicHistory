package com.ritacle.mymusichistory.model;

import java.util.Date;


public class SongStats {


    private Long id;

    private int rank;

    private String title;

    private String artist;

    private String album;

    private int listenAmount;

    private String user;

    private Date date;


    public SongStats() {
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public int getListenAmount() {
        return listenAmount;
    }

    public void setListenAmount(int listenAmount) {
        this.listenAmount = listenAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Song statistic: rank = %s, title = %s, artist = %s, album = %s, amount of listens = %s ", rank, title, artist, album, listenAmount);
    }
}
