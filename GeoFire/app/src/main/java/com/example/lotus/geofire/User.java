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
    public int point;
    public int rating;
    public int urgency;
    public String name;
    public String status;
    public String email;
    public String description;
    public String topic;
    public double latitude;
    public double longitude;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, int age, double latitude, double longitude, String status, int point, int rating, String topic, int urgency, String description) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.point = point;
        this.rating = rating;
        this.topic = topic;
        this.urgency = urgency;
        this.description = description;
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