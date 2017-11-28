package com.example.lotus.garudav3;

/**
 * Created by al-fatih on 27/11/17.
 */

public class Destination {

    private String city;
    private String airport;

    public Destination(String city, String airport){
        this.city = city;
        this.airport = airport;
    }

    public String getCity(){
        return this.city;
    }

    public String getAirport(){
        return this.airport;
    }
}
