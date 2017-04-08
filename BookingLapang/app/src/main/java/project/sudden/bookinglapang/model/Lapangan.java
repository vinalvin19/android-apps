package project.sudden.bookinglapang.model;

/**
 * Created by Lotus on 08/04/2017.
 */

public class Lapangan {

    public String nama;
    public String alamat;
    public Double latitude;
    public Double longitude;

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
}
