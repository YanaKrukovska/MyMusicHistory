package com.ritacle.mymusichistory.model;

public class SongStatistic {

    private String title;
    private String artist;
    private int listenCount;
    private String listener;

    public SongStatistic() {
    }

    public SongStatistic(String title, String artist, int listenCount, String listener) {
        this.title = title;
        this.artist = artist;
        this.listenCount = listenCount;
        this.listener = listener;
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

    public int getListenCount() {
        return listenCount;
    }

    public void setListenCount(int listenCount) {
        this.listenCount = listenCount;
    }


    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }


    @Override
    public String toString() {
        return String.format("Song statistic: title = %s, artist = %s, amount of listens = %s ", title, artist, listenCount);
    }
}
