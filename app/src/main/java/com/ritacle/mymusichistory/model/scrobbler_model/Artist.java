package com.ritacle.mymusichistory.model.scrobbler_model;


public class Artist {

    private Long id;

    private String name;

    public Artist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist() {
    }



    public Artist(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Artist: id = %s, name = %s", id, name);
    }
}