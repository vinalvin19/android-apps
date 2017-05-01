package project.sudden.bookinglapang.model;

import java.util.HashMap;

/**
 * Created by Lotus on 08/04/2017.
 */

public class Lapangan {

    public String nama;
    public String alamat;
    public Double latitude;
    public Double longitude;
    private HashMap<String, Object> subLapangan;
    public String harga;
    public String pilihanLapangan;
    public String date;

    public Lapangan() {}

    public Lapangan(String namaLapangan, String alamatLapangan, Double latitude, Double longitude) {
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


    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
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
