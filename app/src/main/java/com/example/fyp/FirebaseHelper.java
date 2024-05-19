package com.example.fyp;

// Programmer Name: Liew Chen Tzer
// Program Name: FirebaseHelper.java
//Description: To help at compiling data to be sent to Firebase
//First Written on: Tuesday, 1-Feb-2022
//Edited on: Thursday, 6-April-2022

import java.util.Date;

public class FirebaseHelper {
    String longitude, latitude;
    Date date;

    public FirebaseHelper() {
    }

    public FirebaseHelper(String longitude, String latitude, Date date) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
