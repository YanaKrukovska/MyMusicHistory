package com.ritacle.mymusichistory.model.scrobbler_model;


import androidx.annotation.NonNull;

import com.ritacle.mymusichistory.model.Country;

import java.util.Date;

public class User {

    private String mail;
    private String userName;
    private String nickName;
    private String gender;
    private String password;
    private String confirmationPassword;
    private Date birthDate;
    private Long id;
    private Country country;

    public User() {
    }

    public User(String mail, String userName, String nickName, String gender, String password, String confirmationPassword, Date birthDate, Country country) {
        this.mail = mail;
        this.userName = userName;
        this.nickName = nickName;
        this.gender = gender;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
        this.birthDate = birthDate;
        this.country = country;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @NonNull
    @Override
    public String toString() {
        return "User {" + "mail ='" + mail + '\'' +
                ", userName ='" + userName + '\'' +
                ", nickName ='" + nickName + '\'' +
                ", gender ='" + gender + '\'' +
                ", password ='" + password + '\'' +
                ", country ='" + country + '\'' +
                ", id=" + id +
                '}';
    }
}