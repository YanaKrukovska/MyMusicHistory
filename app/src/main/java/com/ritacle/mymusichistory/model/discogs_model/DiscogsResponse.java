package com.ritacle.mymusichistory.model.discogs_model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class DiscogsResponse {

    @SerializedName("results")
    private Results[] results;

    public Results[] getResults() {
        return results;
    }

    public void setResults(Results[] results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Discogs Body Response: results = " + Arrays.toString(results) + ";";
    }
}
