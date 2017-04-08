package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 08/04/2017.
 */

public class MapsActivity extends BaseActivity  {

    private String TAG = getClass().getSimpleName()+"TAGES";

    ArrayList<Double> arrayLatitude = new ArrayList<>();
    ArrayList<Double> arrayLongitude = new ArrayList<>();
    ArrayList<String> arrayName = new ArrayList<>();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private MarkerOptions options = new MarkerOptions();

    private Toolbar toolbar;
    GoogleMap googleMap;

    Double latitudeUser=0.0;
    Double longitudeUser=0.0;
    Double latitude;
    Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        arrayName = this.getIntent().getStringArrayListExtra("namaLapangan");
        arrayLatitude = (ArrayList<Double>) getIntent().getSerializableExtra("latitudeLapangan");
        arrayLongitude = (ArrayList<Double>) getIntent().getSerializableExtra("longitudeLapangan");
        latitudeUser = intent.getDoubleExtra("latitudeUser", latitudeUser);
        longitudeUser = intent.getDoubleExtra("longitudeUser", longitudeUser);

        Log.d(TAG, String.valueOf(arrayLatitude.size()));
        Log.d(TAG, String.valueOf(latitudeUser));

        for (int i = 0; i<arrayLatitude.size(); i++)
        {
            latitude = arrayLatitude.get(i);
            longitude = arrayLongitude.get(i);
            Log.d(TAG, String.valueOf(latitude)+" "+ String.valueOf(longitude));
            latlngs.add(new LatLng(latitude, longitude));

        }

        try {
            if (googleMap == null) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        //for (LatLng point : latlngs) {
                        for (int z = 0; z<arrayName.size(); z++) {
                            options.position(latlngs.get(z));
                            options.title(arrayName.get(z));
                            //options.snippet("someDesc");
                            googleMap.addMarker(options);
                        }

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(latitudeUser, longitudeUser)).zoom(13).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (MapsActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
