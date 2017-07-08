package project.sudden.bookinglapang.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 08/04/2017.
 */

public class MapsActivity extends BaseActivity {

    ArrayList<Double> arrayLatitude = new ArrayList<>();
    ArrayList<Double> arrayLongitude = new ArrayList<>();
    ArrayList<String> arrayName = new ArrayList<>();
    ArrayList<String> arraySubLapangan = new ArrayList<>();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> subLapanganFinal = new ArrayList<>();
    private MarkerOptions options = new MarkerOptions();
    private MarkerOptions optionsUser = new MarkerOptions();

    private Toolbar toolbar;
    GoogleMap googleMap;
    CameraPosition cameraPosition;
    LatLng locationUser;

    Double latitudeUser=-6.799;
    Double longitudeUser=107.79;
    Double latitude;
    Double longitude;
    Button processBook;
    String tempatPilihan;
    private String cabangOlahraga;
    String namaPemesan;
    String gambarLapangan;

    SupportMapFragment supportMapFragment;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference myRef = database.getReference();

    ArrayList<String> subLapangan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
        processBook = (Button) findViewById(R.id.processBook);
        processBook.setEnabled(false);
        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        processBook.setText("PILIH");
        processBook.setTypeface(face);

        // get information in intent from MainActivity
        Intent intent = getIntent();
        arrayName = getIntent().getStringArrayListExtra("namaLapangan");
        arrayLatitude = (ArrayList<Double>) getIntent().getSerializableExtra("latitudeLapangan");
        arrayLongitude = (ArrayList<Double>) getIntent().getSerializableExtra("longitudeLapangan");
        latitudeUser = intent.getDoubleExtra("latitudeUser", latitudeUser);
        longitudeUser = intent.getDoubleExtra("longitudeUser", longitudeUser);
        cabangOlahraga = intent.getStringExtra("cabangOlahraga");
        if (cabangOlahraga == null) {
            arraySubLapangan = (ArrayList<String>) getIntent().getSerializableExtra("cabangOlahraga");
            Log.d(TAG, "cabang tet: " + arraySubLapangan.get(arraySubLapangan.size()-1));
        }
        namaPemesan = intent.getStringExtra("namaPemesan");

        Log.d(TAG, String.valueOf(arrayLatitude.size()));
        Log.d(TAG, String.valueOf(latitudeUser));

        // set latitude and longitude into latlngs array to create marker
        for (int i = 0; i<arrayLatitude.size(); i++)
        {
            latitude = arrayLatitude.get(i);
            longitude = arrayLongitude.get(i);
            Log.d(TAG, String.valueOf(latitude)+" "+ String.valueOf(longitude));
            latlngs.add(new LatLng(latitude, longitude));
            //subLapanganFinal.add(arraySubLapangan.get(i));
        }

        locationUser = new LatLng(latitudeUser, longitudeUser);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        try {
            if (googleMap == null) {

                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        optionsUser.position(locationUser);
                        optionsUser.title("You");
                        optionsUser.snippet("You are here");
                        optionsUser.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        googleMap.addMarker(optionsUser);

                        // add marker in googlemaps
                        for (int z = 0; z<arrayName.size(); z++) {
                            options.position(latlngs.get(z));
                            options.title(arrayName.get(z));
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            if (arraySubLapangan.size()>0)
                                options.snippet(arraySubLapangan.get(z));
                            //options.snippet(arraySubLapangan.get(z));
                            googleMap.addMarker(options);
                        }

                        // when marker being clicked
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                        {
                            @Override
                            public boolean onMarkerClick(Marker arg0) {
                            if(!arg0.getTitle().contains("You"))
                            {
                                tempatPilihan = arg0.getTitle();

                                if (arraySubLapangan.size() > 0)
                                    cabangOlahraga = arg0.getSnippet();
                                processBook.setEnabled(true);
                                //cabangOlahraga = arg0.getSnippet().toString();

                                arg0.showInfoWindow();
                                return true;
                            }
                            return false;
                            }
                        });

                        // setting camera
                        cameraPosition = new CameraPosition.Builder().target(
                                new LatLng(latitudeUser, longitudeUser)).zoom(14).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        // when processbook being clicked, information from selected lapangan will be processed
        processBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d(TAG, cabangOlahraga + tempatPilihan);
                // getting subLapangan information from selected lapangan
                myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        subLapangan.clear();

                        // set the subLapangan to array
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot childEventSnapshot : eventSnapshot.getChildren()) {
                                Log.d(TAG, "GetChildren: "+childEventSnapshot.getKey() + childEventSnapshot.getValue());
                            }
                            subLapangan.add(eventSnapshot.getKey());
                        }

                        for (int i = 0; i<subLapangan.size();i++)
                            Log.d(TAG, "sub: "+subLapangan.get(i));

                        Log.d(TAG, "nama lapangan= "+ snapshot.getChildrenCount());

                        // sending information for selected lapangan to dialogActivity
                        Intent intent =new Intent(MapsActivity.this, DialogActivity.class);
                        intent.putStringArrayListExtra("subLapangan", subLapangan);
                        intent.putExtra("cabangOlahraga", cabangOlahraga);
                        intent.putExtra("tempatPilihan", tempatPilihan);
                        intent.putExtra("namaPemesan", namaPemesan);
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
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY );
        startActivity(i);
        Log.d(TAG, "Maps: onbackpressed");
        finish();
    }
}
