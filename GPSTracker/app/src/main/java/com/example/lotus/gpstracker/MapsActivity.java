package com.example.lotus.gpstracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends AppCompatActivity {

    // Google Map
    private GoogleMap googleMap;
    LocationManager locationManager;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkLocationPermission();

        try {
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, mLocationListener);
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        Log.i("TAGI", "GPS dinyalakan");
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("TAGI", "masuk");
            Log.d("TAGI", "Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
            //textView.setText("Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
            Toast.makeText(MapsActivity.this, "Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude(), Toast.LENGTH_LONG).show();

            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Kamu Disini");

            // adding marker
            googleMap.addMarker(marker);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(location.getLatitude(), location.getLongitude())).zoom(12).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Latitude", "disable");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Latitude", "enable");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Latitude", "status");
        }
    };

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
}
