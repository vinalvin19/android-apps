package com.example.lotus.geofire;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Lotus on 09/03/2017.
 */

public class MapsActivity extends AppCompatActivity {

    private String nama;
    private Double latitude;
    private Double longitude;
    private Double latitudeUser;
    private Double longitudeUser;
    private int distance;
    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        checkLocationPermission();

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            nama = (String) b.get("name");
            latitude = (Double) b.get("latitude");
            longitude = (Double) b.get("longitude");
            distance = (Integer) b.get("distance");
            latitudeUser = (Double) b.get("latitudeUser");
            longitudeUser = (Double) b.get("longitudeUser");
            Log.d("TAG", "nama= " + nama + " & distance= " + distance);

            try{
                initializeMap();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        else{
            Toast.makeText(this, "ga keintent", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeMap() {
        if (googleMap == null) {
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d("TAGI", "masuk");

                    googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(nama);
                    googleMap.addMarker(marker);
                    MarkerOptions markerUser = new MarkerOptions().position(new LatLng(latitudeUser, longitudeUser)).title(nama);
                    googleMap.addMarker(markerUser);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(latitude, longitude)).zoom(8).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(latitudeUser, longitudeUser), new LatLng(latitude, longitude))
                            .width(5)
                            .color(Color.RED));
                }
            });

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
