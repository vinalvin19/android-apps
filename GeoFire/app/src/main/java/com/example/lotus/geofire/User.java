package com.example.lotus.geofire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Lotus on 04/03/2017.
 */

@IgnoreExtraProperties
public class User {

    public int age;
    public String name;
    public String status;
    public double latitude;
    public double longitude;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int age, double latitude, double longitude) {
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}