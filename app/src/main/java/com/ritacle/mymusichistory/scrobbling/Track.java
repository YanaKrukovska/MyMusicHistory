package com.ritacle.mymusichistory.scrobbling;

import android.media.MediaMetadata;

public class Track {

    private String title;
    private String album;
    private String artist;
    private long duration;

    public Track() {
    }

    public Track(TrackBuilder trackBuilder) {
        this.title = trackBuilder.title;
        this.album = trackBuilder.album;
        this.artist = trackBuilder.artist;
        this.duration = trackBuilder.duration;
    }

    public Track(String title, String artist, String album, long duration) {
        this.title = title;
        this.album = album;
        this.artist = artist;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public static Track fromMediaMetadata(MediaMetadata metadata) {
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


        TrackBuilder trackBuilder = Track.builder();
        trackBuilder.title(title);

        if (duration < 1000) {
            duration *= 1000;
        }

        if (duration > 0) {
            trackBuilder.duration(duration);
        }
        if (album != null && !album.isEmpty()) {
            trackBuilder.album(album);
        }

        if (artist != null) {
            trackBuilder.artist(artist);
        }
        return trackBuilder.build();
    }

    public boolean isSameTrack(Track track) {
        return track != null && track.getTitle().equals(title) && track.getArtist().equals(artist);
    }

    public boolean isValid() {
        return !title.equals("") && !artist.equals("");
    }


    public static TrackBuilder builder() {
        return new TrackBuilder();
    }

    public static final class TrackBuilder {
        private String title;
        private String artist;
        private String album;
        private long duration;

        public TrackBuilder title(String songTitle) {
            this.title = songTitle;
            return this;
        }

        public TrackBuilder artist(String artist) {
            this.artist = artist;
            return this;
        }

        public TrackBuilder album(String album) {
            this.album = album;
            return this;
        }

        public TrackBuilder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public Track build() {
            return new Track(this);
        }
    }
}
