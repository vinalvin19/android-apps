package com.example.lotus.gorobak;

/**
 * Created by Lotus on 15/03/2017.
 */

public class Pedagang {

    public String name;
    public String email;
    public String dagangan;
    public String image;
    public double latitude;
    public double longitude;

    public Pedagang() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Pedagang(String name, String email, String dagangan, double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.dagangan = dagangan;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }
    public String getDagangan() {
        return dagangan;
    }
    public String getEmail() {
        return email;
    }
    public String getImage() {
        return image;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
