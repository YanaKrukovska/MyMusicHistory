package com.ritacle.mymusichistory.model;


public class Country {

    private String fullName;
    private String countryCode;


    public Country() {
    }

    public Country(String fullName, String countryCode) {
        this.fullName = fullName;
        this.countryCode = countryCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return "Country: full name = " + fullName + ", countryCode = " + countryCode;
    }
}