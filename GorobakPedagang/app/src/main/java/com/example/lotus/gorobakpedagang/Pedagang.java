package com.example.lotus.gorobakpedagang;

/**
 * Created by Lotus on 15/03/2017.
 */

public class Pedagang {

    public String name;
    public String email;
    public String dagangan;
    public String image;
    public String dipanggil;
    public double latitude;
    public double longitude;

    public Pedagang() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Pedagang(String name, String email, String dagangan, String dipanggil, String image, double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.dagangan = dagangan;
        this.dipanggil = dipanggil;
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
    public String getDipanggil() {
        return dipanggil;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}
