package com.example.lotus.emailbuilder;


public class Site {

    public String nama;
    public String email;
    public String alamat;

    public Site() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Site(String nama, String alamat, String email) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
    }


    public String getNama() {
        return nama;
    }
    public String getAlamat() {
        return alamat;
    }
    public String getEmail() {
        return email;
    }

}