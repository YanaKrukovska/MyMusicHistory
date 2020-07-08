package com.ritacle.mymusichistory.scrobbling;

import android.content.Context;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;

import com.ritacle.mymusichistory.model.scrobbler_model.Song;

import java.util.HashMap;
import java.util.Map;

public class PlaybackTracker {

    private Map<String, PlayerState> playerStates = new HashMap<>();
    private Context context;

    public PlaybackTracker(Context context) {
        this.context = context;
    }

    public void handlePlaybackStateChange(String player, PlaybackState playbackState) {
        if (playbackState == null) {
            return;
        }

        PlayerState playerState = getOrCreatePlayerState(player);
        playerState.setPlaybackState(playbackState);
    }

    public void handleMetadataChange(String player, MediaMetadata metadata) {
        if (metadata == null) {
            return;
        }

        Song song = Song.fromMediaMetadata(metadata);
        if (!song.isValid()) {
            return;
        }
        PlayerState playerState = getOrCreatePlayerState(player);
        playerState.setSong(song);
    }

    public void handleSessionTermination(String player) {
        PlayerState playerState = getOrCreatePlayerState(player);
        PlaybackState playbackState =
                new PlaybackState.Builder()
                        .setState(PlaybackState.STATE_PAUSED, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1)
                        .build();
        playerState.setPlaybackState(playbackState);
    }

    private PlayerState getOrCreatePlayerState(String player) {
        PlayerState playerState = playerStates.get(player);

        if (!playerStates.containsKey(player)) {
            playerState = new PlayerState(context);
            playerStates.put(player, playerState);
        }

        return playerState;
    }
}
