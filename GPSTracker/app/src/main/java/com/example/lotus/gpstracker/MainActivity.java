package com.example.lotus.gpstracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text1);
        button = (Button) findViewById(R.id.button);

        initLocation();

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent (MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void initLocation(){
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
            textView.setText("Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
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
}
