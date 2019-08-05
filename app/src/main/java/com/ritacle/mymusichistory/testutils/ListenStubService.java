package com.ritacle.mymusichistory.testutils;

import com.ritacle.mymusichistory.model.Listen;

import java.util.LinkedList;
import java.util.List;

public class ListenStubService {

    public List<Listen> getListens() {
        List<Listen> listens = new LinkedList<>();
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Dreamer", "Martin Garrix", "Dreamer", null));
        listens.add(new Listen("Better Luck Next Time", "Kelsea Ballerini", "Better Luck Next Time", null));
        listens.add(new Listen("Hold Me While You Wait", "Lewis Capaldi", "Divinely Uninspired To A Hellish Extent", null));
        listens.add(new Listen("Live Is Life (digitally remastered) (Single Version)", "The B-52's", "Songs to Sing in the Shower", null));
        listens.add(new Listen("Latency", "Martin Garrix", "BYLAW EP", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        listens.add(new Listen("Freaking Me Out", "Ava Max", "Freaking Me Out", null));
        return listens;
    }
}
