package com.example.lotus.geofire;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotus on 09/03/2017.
 */

public class MapsActivity extends AppCompatActivity {

    private String namaPenolong;
    private String namaUser;
    private String currentUser;
    private String status;
    private Double latitude;
    private Double longitude;
    private Double latitudeUser;
    private Double longitudeUser;
    private int distance;
    private GoogleMap googleMap;

    Button cancelButton;
    Button doneButton;
    Button intentButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        checkLocationPermission();

        cancelButton = (Button) findViewById(R.id.cancelButton);
        doneButton = (Button) findViewById(R.id.doneButton);
        //intentButton = (Button) findViewById(R.id.intentButton);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            namaPenolong = (String) b.get("name");
            namaUser = (String) b.get("nameUser");
            latitude = (Double) b.get("latitude");
            longitude = (Double) b.get("longitude");
            distance = (Integer) b.get("distance");
            latitudeUser = (Double) b.get("latitudeUser");
            longitudeUser = (Double) b.get("longitudeUser");
            Log.d("TAG", "namaPenolong= " + namaPenolong + " & distance= " + distance);

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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);

                //baru bisa dicoba kalau udah ada login
                Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);
                Map<String, Object> cancelling = new HashMap<String, Object>();
                cancelling.put("status", "free");
                myRef.child("user").child(namaPenolong).updateChildren(cancelling);
                Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);

                Intent i = new Intent (MapsActivity.this, MainActivity.class);
                startActivity(i);

                /*//yang login itu minta tolong
                if (currentUser == namaUser) {
                    //database alanRef = myRef.child("alanisawesome");
                    Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);
                    Map<String, Object> cancelling = new HashMap<String, Object>();
                    cancelling.put("status", "free");
                    myRef.child("user").child(namaPenolong).updateChildren(cancelling);
                    Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);
                }

                //yang login itu mau nolong
                else if (currentUser==namaPenolong) {
                    Log.d("TAG", "masuk ke " + namaPenolong + "dengan status " + status);

                    //ubah icon di chat

                    //back to chatActivity
                    onBackPressed();
                }*/
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent ke activity lain setelah done
                Log.d("TAGES", "nama penolong " + namaPenolong + "dengan status " + status);
                Map<String, Object> done = new HashMap<String, Object>();
                done.put("status", "free");
                done.put("description", "null");
                done.put("urgency", 0);
                myRef.child("user").child(namaPenolong).updateChildren(done);
                Log.d("TAGES", "user " + namaPenolong + "dengan status " + status);
                Intent i = new Intent (MapsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

/*        intentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MapsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });*/
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

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(namaPenolong);
                    googleMap.addMarker(marker);
                    MarkerOptions markerUser = new MarkerOptions().position(new LatLng(latitudeUser, longitudeUser)).title(namaUser);
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
