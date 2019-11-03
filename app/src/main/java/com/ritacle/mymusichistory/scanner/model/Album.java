package com.ritacle.mymusichistory.scanner.model;

public class Album {

    private Long id;
    private String title;
    private Artist artist;

    public Album() {
    }
    public Album(String title, Artist artist) {
        this.title = title;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return String.format("Album: id = %s, title = %s", id, title);
    }
}
