package com.ritacle.mymusichistory.testutils;

import com.ritacle.mymusichistory.model.TopArtist;

import java.util.LinkedList;
import java.util.List;

public class TopArtistsStubService {

    public List<TopArtist> getArtists() {
        List<TopArtist> topArtists = new LinkedList<>();
        topArtists.add(new TopArtist("Panic! at the Disco", 50));
        topArtists.add(new TopArtist("Ava Max", 40));
        topArtists.add(new TopArtist("I Don't Know How But They Found Me",  30));
        topArtists.add(new TopArtist("The Chainsmokers",  28));
        topArtists.add(new TopArtist("Weezer",  25));
        topArtists.add(new TopArtist("Kelsea Ballerini",  22));
        topArtists.add(new TopArtist("Anitta",  22));
        topArtists.add(new TopArtist("Lana Del Rey",  21));
        topArtists.add(new TopArtist("Justin Bieber",  19));
        topArtists.add(new TopArtist( "Taylor Swift",  15));
        topArtists.add(new TopArtist( "Fall Out Boy",  11));
        topArtists.add(new TopArtist("Buddy Holly",  2));
        return topArtists;
    }
}
