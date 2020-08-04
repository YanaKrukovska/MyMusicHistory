package com.ritacle.mymusichistory.model.discogs_model;

public class Results {
    private String master_id;

    private String country;

    private String resource_url;

    private String year;

    private String thumb;

    private String[] format;

    private String master_url;

    private String[] label;

    private String type;

    private String title;

    private String uri;

    private String catno;

    private String[] genre;

    private String[] style;

    private String id;

    private String cover_image;

    public String getMaster_id() {
        return master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String[] getFormat() {
        return format;
    }

    public void setFormat(String[] format) {
        this.format = format;
    }

    public String getMaster_url() {
        return master_url;
    }

    public void setMaster_url(String master_url) {
        this.master_url = master_url;
    }

    public String[] getLabel() {
        return label;
    }

    public void setLabel(String[] label) {
        this.label = label;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCatno() {
        return catno;
    }

    public void setCatno(String catno) {
        this.catno = catno;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String[] getStyle() {
        return style;
    }

    public void setStyle(String[] style) {
        this.style = style;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    @Override
    public String toString() {
        return "ClassPojo [master_id = " + master_id + ", country = " + country + ", resource_url = " + resource_url + ", year = " + year + ", thumb = " + thumb + ", format = " + format + ", master_url = " + master_url + ", label = " + label + ", type = " + type + ", title = " + title + ", uri = " + uri + ", catno = " + catno + ", genre = " + genre + ", style = " + style + ", id = " + id + ", cover_image = " + cover_image + "]";
    }
}
