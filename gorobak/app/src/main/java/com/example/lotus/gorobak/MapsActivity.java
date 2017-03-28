package com.example.lotus.gorobak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotus on 09/03/2017.
 */

public class MapsActivity extends AppCompatActivity {

    String namaPedagang;
    String emailPedagang;
    String emailPedagangUpdate;
    String namaUser;
    String emailUser;

    private Double latitude;
    private Double longitude;
    private Double latitudeUser;
    private Double longitudeUser;
    private int distance;

    private GoogleMap googleMap;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    Button doneButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkLocationPermission();

        doneButton = (Button) findViewById(R.id.panggil);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            namaPedagang = (String) b.get("name");
            emailPedagang = (String) b.get("email");
            emailUser = (String) b.get("emailUser");
            namaUser = (String) b.get("nameUser");
            latitude = (Double) b.get("latitude");
            longitude = (Double) b.get("longitude");
            distance = (Integer) b.get("distance");
            latitudeUser = (Double) b.get("latitudeUser");
            longitudeUser = (Double) b.get("longitudeUser");
            Log.d("TAG", "namaPenolong= " + namaPedagang + " & distance= " + distance);

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


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent ke activity lain setelah done

                emailPedagangUpdate=emailPedagang.replace(".",",");

                Log.d("TAGES", "nama pedagang: " + namaPedagang + " email: " + emailPedagangUpdate);
                Map<String, Object> done = new HashMap<String, Object>();
                done.put("panggil", emailPedagangUpdate);
                myRef.child("user").child(emailUser).updateChildren(done);

                showProgressDialog();
            }
        });
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

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(namaPedagang);
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

    private void showProgressDialog(){
        final ProgressDialog pd = new ProgressDialog(MapsActivity.this);

        // Set progress dialog style spinner
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // Set the progress dialog title and message
        pd.setTitle("Memanggil Pedagang Gorobak");
        pd.setMessage("Loading.........");

        // Set the progress dialog background color
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));

        pd.setIndeterminate(false);

        // Finally, show the progress dialog
        pd.show();

        // Set the progress status zero on each button click
        progressStatus = 0;

        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 200){
                    // Update the progress status
                    progressStatus +=1;

                    // Try to sleep the thread for 20 milliseconds
                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // Update the progress status
                            pd.setProgress(progressStatus);
                            // If task execution completed
                            if(progressStatus == 200){
                                // Dismiss/hide the progress dialog
                                pd.dismiss();
                                Intent i = new Intent (MapsActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }
}
