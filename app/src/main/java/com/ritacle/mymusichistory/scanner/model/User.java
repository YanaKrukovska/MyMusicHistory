package com.ritacle.mymusichistory.scanner.model;

public class User {

    private String mail;
    private Long id;

    public User() {
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
