package com.ritacle.mymusichistory.model.scrobbler_model;

import android.annotation.TargetApi;
import android.media.MediaMetadata;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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

    public Song(SongBuilder songBuilder) {
        this.title = songBuilder.title;
        this.album = songBuilder.album;
        this.duration = songBuilder.duration;
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

    public boolean isSameSong(Song song) {
        return song != null && song.getTitle().equals(title) && song.getAlbum().getArtist().getName().equals(album.getArtist().getName());
    }

    public boolean isValid() {
        return title!= null && album != null && !title.equals("") && !album.getTitle().equals("");
    }

    public static Song fromMediaMetadata(MediaMetadata metadata) {
        String title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE);
        String artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
        String album = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM);
        long duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);

        if (title == null) {
            title = metadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE);

            if (title == null) {
                title = "";
            }
        }

       SongBuilder songBuilder = Song.builder();
        songBuilder.title(title);

        if (duration < 1000) {
            duration *= 1000;
        }

        if (duration > 0) {
            songBuilder.duration(duration);
        }
        if (album != null && !album.isEmpty() && !artist.isEmpty()) {
            songBuilder.album(new Album(album, new Artist(artist)));
        }

        return songBuilder.build();
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

    @NonNull
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        return new StringJoiner(", ", Song.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("album='" + album.getTitle() + "'")
                .toString();
    }

    public static SongBuilder builder() {
        return new SongBuilder();
    }

    public static final class SongBuilder {
        private String title;
        private Album album;
        private long duration;

        public SongBuilder title(String songTitle) {
            this.title = songTitle;
            return this;
        }

        public SongBuilder album(Album album) {
            this.album = album;
            return this;
        }

        public SongBuilder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }
}