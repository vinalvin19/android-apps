package com.example.lotus.garudav3;

/**
 * Created by Lotus on 20/11/2017.
 */

public class Route {
    private String name, price, time, duration, method, points;

    public Route() {
    }

    public Route(String name, String price, String time, String duration, String method, String points) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.duration = duration;
        this.method = method;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name= name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
