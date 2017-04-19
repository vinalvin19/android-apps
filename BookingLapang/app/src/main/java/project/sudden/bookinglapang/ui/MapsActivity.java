package project.sudden.bookinglapang.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.Lapangan;
import project.sudden.bookinglapang.model.User;

/**
 * Created by Lotus on 08/04/2017.
 */

public class MapsActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName()+"TAGES";

    ArrayList<Double> arrayLatitude = new ArrayList<>();
    ArrayList<Double> arrayLongitude = new ArrayList<>();
    ArrayList<String> arrayName = new ArrayList<>();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private MarkerOptions options = new MarkerOptions();

    private Toolbar toolbar;
    GoogleMap googleMap;

    Double latitudeUser=-6.799;
    Double longitudeUser=107.79;
    Double latitude;
    Double longitude;
    Button processBook;
    String tempatPilihan;
    String cabangOlahraga;

    SupportMapFragment supportMapFragment;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference();

    ArrayList<String> subLapangan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setSupportActionBar(toolbar);

        processBook = (Button) findViewById(R.id.processBook);
        processBook.setEnabled(false);

        Intent intent = getIntent();
        arrayName = this.getIntent().getStringArrayListExtra("namaLapangan");
        arrayLatitude = (ArrayList<Double>) getIntent().getSerializableExtra("latitudeLapangan");
        arrayLongitude = (ArrayList<Double>) getIntent().getSerializableExtra("longitudeLapangan");
        latitudeUser = intent.getDoubleExtra("latitudeUser", latitudeUser);
        longitudeUser = intent.getDoubleExtra("longitudeUser", longitudeUser);
        cabangOlahraga = intent.getStringExtra("cabangOlahraga");

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
                supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        //for (LatLng point : latlngs) {
                        for (int z = 0; z<arrayName.size(); z++) {
                            options.position(latlngs.get(z));
                            options.title(arrayName.get(z));
                            //options.snippet(arrayName.get(z));
                            googleMap.addMarker(options);
                        }

                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                        {
                            @Override
                            public boolean onMarkerClick(Marker arg0) {
                                tempatPilihan = arg0.getTitle();
                                Toast.makeText(MapsActivity.this, tempatPilihan+" "+cabangOlahraga, Toast.LENGTH_SHORT).show();// display toast
                                processBook.setBackgroundColor(Color.parseColor("#FF33B5E5"));
                                processBook.setEnabled(true);
                                arg0.showInfoWindow();
                                return true;
                            }
                        });

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(latitudeUser, longitudeUser)).zoom(10).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        processBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                subLapangan.clear();
                myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Lapangan lapangan = snapshot.getValue(Lapangan.class);

                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot childEventSnapshot : eventSnapshot.getChildren()) {
                                Log.d("TAGES", "GetChildren: "+childEventSnapshot.getKey() + childEventSnapshot.getValue());
                            }
                            subLapangan.add(eventSnapshot.getKey());
                        }

                        for (int i = 0; i<subLapangan.size();i++)
                            Log.d("TAGES", "sub: "+subLapangan.get(i));

                        Log.d("TAGES", "nama lapangan= "+ lapangan.getNamaLapangan());
                        Log.d("TAGES", "nama lapangan= "+ snapshot.getChildrenCount());

                        //da.spinnerArrayAdapter.clear();
                        //spinnerArrayAdapter.clear();
                        //spinnerArrayAdapter.notifyDataSetChanged();

                        Intent intent =new Intent(MapsActivity.this, DialogActivity.class);
                        intent.putStringArrayListExtra("subLapangan", subLapangan);
                        intent.putExtra("cabangOlahraga", cabangOlahraga);
                        intent.putExtra("tempatPilihan", tempatPilihan);
                        startActivity(intent);

                    }
                    @Override public void onCancelled(DatabaseError error) { }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        Log.d("TAGES", "Maps: onbackpressed");
    }
}