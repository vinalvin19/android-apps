package com.example.lotus.emailbuilder;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.io.Serializable;
import java.util.ArrayList;

import static android.os.UserHandle.readFromParcel;

public class Site implements Parcelable {

    public String nama;
    public String email;
    public String alamat;
    public ArrayList<Site> siteArrayList;
    public ArrayList<Site> orig;

    public Site() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Site(String nama, String alamat, String email) {
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
    }

    public Site(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Site> CREATOR = new Parcelable.Creator<Site>() {
        public Site createFromParcel(Parcel in) {
            return new Site(in);
        }

        public Site[] newArray(int size) {

            return new Site[size];
        }

    };

    /*private Site(Parcel pc){
        this.nama = pc.readString();
        this.email =  pc.readString();
        this.alamat = pc.readString();
    }*/

    public void readFromParcel(Parcel in) {
        nama = in.readString();
        email = in.readString();
        alamat = in.readString();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(alamat);
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