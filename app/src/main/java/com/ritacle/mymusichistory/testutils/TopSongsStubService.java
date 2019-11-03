package com.ritacle.mymusichistory.testutils;

import com.ritacle.mymusichistory.model.ListenAmount;

import java.util.LinkedList;
import java.util.List;

public class TopSongsStubService {

    public List<ListenAmount> getSongs() {
        List<ListenAmount> topSongs = new LinkedList<>();
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 50));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 40));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 30));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 28));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 25));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 22));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 22));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 21));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 19));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 15));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 11));
        topSongs.add(new ListenAmount("Freaking Me Out", "Ava Max", "Freaking Me Out", 2));
        return topSongs;
    }
}
