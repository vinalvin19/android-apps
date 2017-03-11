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

    public User(int age, String description, String email, double latitude, double longitude, String name, int point, int rating, String status, String topic, int urgency) {
        this.age = age;
        this.description = description;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.point = point;
        this.rating = rating;
        this.status = status;
        this.topic = topic;
        this.urgency = urgency;
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
    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}