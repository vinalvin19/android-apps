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

    public Lapangan() {}

    public Lapangan(String namaLapangan, String alamatLapangan, Double latitude, Double longitude) {
        this.nama= namaLapangan;
        this.alamat = alamatLapangan;
        this.latitude= latitude;
        this.longitude = longitude;
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

    public void setNamaLapangan(String namaLapangan) {
        this.nama = namaLapangan;
    }

    public HashMap<String, Object> getSubLapangan() {
        return subLapangan;
    }

    public String getHarga() {
        return harga;
    }
}
