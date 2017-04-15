package com.example.lotus.emailbuilder;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.os.UserHandle.readFromParcel;

public class Site {

    public String nama;
    public String email;
    public String alamat;
    public String siteid;
    public String status;
    public ArrayList<Site> siteArrayList;
    public ArrayList<Site> orig;

    public Site() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Site(String nama, String alamat, String email, String siteid) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.siteid = siteid;
    }

    public Site(String nama, String status) {
        this.nama = nama;
        this.status = status;
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
    public String getSiteid() {
        return siteid;
    }
}