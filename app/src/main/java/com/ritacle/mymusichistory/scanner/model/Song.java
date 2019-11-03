package com.ritacle.mymusichistory.scanner.model;

import java.util.Objects;
import java.util.StringJoiner;

public class Song {
    private String title;
    private Album album;
    private long duration;


    public Song() {
    }


    public Song(String title, Album album, long duration) {
        this.title = title;
        this.album = album;
        this.duration = duration;

    }

    public long getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return title.equals(song.title) && album.getTitle().equals(song.album.getTitle())
                && album.getArtist().getName().equals(song.getAlbum().getArtist().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, album);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Song.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("album='" + album + "'")
                .toString();
    }
}
