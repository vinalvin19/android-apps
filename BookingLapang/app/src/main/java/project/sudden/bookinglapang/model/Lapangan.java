package project.sudden.bookinglapang.model;

import java.util.HashMap;

/**
 * Created by Lotus on 08/04/2017.
 */

public class Lapangan {

    public String nama;
    public String alamat;
    public String latitude;
    public String longitude;
    private HashMap<String, Object> subLapangan;
    public String harga;
    public String pilihanLapangan;
    public String date;

    public Lapangan() {}

    public Lapangan(String namaLapangan, String alamatLapangan, String latitude, String longitude) {
        this.nama= namaLapangan;
        this.alamat = alamatLapangan;
        this.latitude= latitude;
        this.longitude = longitude;
    }

    public Lapangan(String nama, String pilihanLapangan, String date) {
        this.nama = nama;
        this.pilihanLapangan= pilihanLapangan;
        this.date = date;
    }

    public String getNamaLapangan() {
        return nama;
    }

    public String getAlamatLapangan() {
        return alamat;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public HashMap<String, Object> getSubLapangan() {
        return subLapangan;
    }

    public String getHarga() {
        return harga;
    }
    public String getPilihanLapangan() {
        return pilihanLapangan;
    }
    public String getDate() {
        return date;
    }

    public void setNamaLapangan(String namaLapangan) {
        this.nama = namaLapangan;
    }
    public void setPilihanLapangan(String pilihanLapangan){
        this.pilihanLapangan= pilihanLapangan;
    }
    public void setDate(String date){
        this.date= date;
    }

}
