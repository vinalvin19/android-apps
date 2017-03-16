package com.example.lotus.gorobak;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Lotus on 04/03/2017.
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String email;
    public String lokasi;
    public String panggil;
    public double latitude;
    public double longitude;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String lokasi, String panggil, double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.lokasi = lokasi;
        this.panggil = panggil;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }
    public String getLokasi() {
        return lokasi;
    }
    public String getEmail() {
        return email;
    }
    public String getPanggil() {
        return panggil;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}