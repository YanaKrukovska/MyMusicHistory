package com.ritacle.mymusichistory.model;

import java.util.Date;


public class Listen {


    private String title;

    private String artist;

    private String album;


    private Date date;


    public Listen() {
    }

    public Listen(String title, String artist, String album,  Date date) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.date = date;
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




    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Song statistic: title = %s, artist = %s, album = %s,  ", title, artist, album);
    }
}
