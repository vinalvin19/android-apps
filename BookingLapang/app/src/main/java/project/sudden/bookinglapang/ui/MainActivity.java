package project.sudden.bookinglapang.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.Lapangan;
import project.sudden.bookinglapang.model.User;

public class MainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, LocationListener{

    LocationManager locationManager;

    String onSelected;
    public String currentUserEmail ="";
    public String namaUser = "";
    public double latitudeUser = -6.879;
    public double longitudeUser = 107.61;
    public int jarakTempuh = 0;

    private Spinner sportsSpinner;
    private Button searchOnMapsButton;
    private Button searchEachSpinner;
    private AccountHeader accountHeader;
    private Drawer drawer = null;
    private Toolbar toolbar;
    Intent intent;
    int j = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    ArrayList<Double> arrayLatitude = new ArrayList<>();
    ArrayList<Double> arrayLongitude = new ArrayList<>();
    ArrayList<String> arrayName = new ArrayList<>();
    ArrayList<String> arraySubLapangan= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkUserAuth();
        registerUI();

        mAuth = FirebaseAuth.getInstance();

        // checking current user and ask for GPS permission
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            currentUserEmail = mUser.getEmail().replace(".",",");
            namaUser = mUser.getDisplayName();
            Log.d(TAG, "MainActivity.onAuthStateChanged:signed_in: " + currentUserEmail);

            createDrawer();

            // checking GPS permission
            if (checkPermission()) {
                //Toast.makeText(getApplicationContext(),"Permission already granted.",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Permission already granted.");
            } else {
                Toast.makeText(getApplicationContext(),"Permission not granted.",Toast.LENGTH_SHORT).show();
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    Log.d(TAG, "Permission already granted.");
                }
            }

            // get GPS location in every 10 seconds
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
                Log.d(TAG, "minta koordinat harusnya");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            } else {
                requestPermission();
            }
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
            Toast.makeText(this, "silahkan login", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void registerUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sportsSpinner = (Spinner) findViewById(R.id.spinner_sports);
        searchOnMapsButton = (Button) findViewById(R.id.buttonSearchAll);
        searchEachSpinner = (Button) findViewById(R.id.buttonSearch);

        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        searchOnMapsButton.setText("CARI DI PETA");
        searchOnMapsButton.setTypeface(face);
        searchEachSpinner.setText("CARI LAPANG");
        searchEachSpinner.setTypeface(face);

        sportsSpinner.setOnItemSelectedListener(this);

        MobileAds.initialize(this, "ca-app-pub-1594284244924018~5570828484");
        AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequest);

        // set spinner from sports_array adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);
        sportsSpinner.setBackgroundColor(Color.parseColor("#FFFFFF"));

        // button search atas onclick
        searchEachSpinner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                checkLapangan();
            }
        });

        // button search bawah onclick
        searchOnMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Tast");
                checkLapanganSemua();
            }
        });
    }

    // template dari sananya
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        onSelected = item;
        clearArrayList();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    // when search button pressed, start to check lapangan based on spinner item
    public void checkLapangan()
    {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
        Log.d(TAG, "masuk cek lapangan");

        // get lapangan from firebase
        myRef.child("lapangan").child(onSelected).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.d(TAG, "ini kunci: "+dataSnapshot.getKey());

                Lapangan lapangan= dataSnapshot.getValue(Lapangan.class);
                Log.d(TAG, "count: " + String.valueOf(j++));

                // counting the nearest lapangan
                jarakTempuh = distance(latitudeUser, longitudeUser, Double.parseDouble(lapangan.getLatitude()), Double.parseDouble(lapangan.getLongitude()));
                if (jarakTempuh < 100 && jarakTempuh >= 0) {

                    // set every lapangan information to arrayLatitude, arrayLongitude, arrayName for future use
                    Log.d(TAG, "(geofence) ada " + lapangan.getNamaLapangan() + " dengan jarak " + jarakTempuh);
                    if (!arrayName.contains(lapangan.getNamaLapangan())) {
                        arrayLatitude.add(Double.parseDouble(lapangan.getLatitude()));
                        arrayLongitude.add(Double.parseDouble(lapangan.getLongitude()));
                        arrayName.add(dataSnapshot.getKey());
                        //arrayDistance.add(jarakTempuh);
                    }

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }

                    Log.d(TAG, "child finished");
                    intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putStringArrayListExtra("namaLapangan", arrayName);
                    intent.putExtra("latitudeLapangan", arrayLatitude);
                    intent.putExtra("longitudeLapangan", arrayLongitude);
                    intent.putExtra("latitudeUser", latitudeUser);
                    intent.putExtra("longitudeUser", longitudeUser);
                    intent.putExtra("cabangOlahraga", onSelected);
                    intent.putExtra("namaPemesan", namaUser);
                    startActivity(intent);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "child changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "child removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "child moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "child cancelled");
            }
        });

        Log.d(TAG, "child done");
    }

    // when search button bawah pressed (masih belom)
    public void checkLapanganSemua() {
        Log.d(TAG, "masuk cek lapangan semua");

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();

        myRef.child("lapangan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                for(int i = 0; i<dataSnapshot.getChildrenCount(); i++) {
                    arraySubLapangan.add(dataSnapshot.getKey());
                    Log.d(TAG, "ini: " + dataSnapshot.getKey() + i);
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lapangan lapangan = snapshot.getValue(Lapangan.class);
                    Log.d(TAG, "ada: "+lapangan.getNamaLapangan() + " dengan latitude " + lapangan.getLatitude());

                    jarakTempuh = distance(latitudeUser, longitudeUser, Double.parseDouble(lapangan.getLatitude()), Double.parseDouble(lapangan.getLongitude()));
                    if (jarakTempuh < 100) {

                        if (!arrayName.contains(lapangan.getNamaLapangan())) {
                            arrayLatitude.add(Double.parseDouble(lapangan.getLatitude()));
                            arrayLongitude.add(Double.parseDouble(lapangan.getLongitude()));
                            arrayName.add(snapshot.getKey());
                            //arrayDistance.add(jarakTempuh);
                        }

                        Intent intent =new Intent(MainActivity.this, MapsActivity.class);
                        intent.putStringArrayListExtra("namaLapangan", arrayName);
                        intent.putExtra("latitudeLapangan", arrayLatitude);
                        intent.putExtra("longitudeLapangan", arrayLongitude);
                        intent.putExtra("latitudeUser", latitudeUser);
                        intent.putExtra("longitudeUser", longitudeUser);
                        intent.putExtra("cabangOlahraga", arraySubLapangan);
                        intent.putExtra("namaPemesan", namaUser);
                        startActivity(intent);
                    }
                }

                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // every 10 seconds, onLocationChanged will be called to updating user's location
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "masuk location listener");

        latitudeUser=location.getLatitude();
        longitudeUser=location.getLongitude();

        Log.d(TAG, "Latitude saya:" + location.getLatitude() + " Longitude:" + location.getLongitude());
    }

    /**
     * GPS turned off, stop watching for updates.
     * @param provider contains data on which provider was disabled
     */
    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()) {
            locationManager.removeUpdates(this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    private boolean checkPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permission)){

            Toast.makeText(getApplicationContext(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{permission},1);
        }
    }

    public void clearArrayList(){
        arrayName.clear();
        arrayLatitude.clear();
        arrayLongitude.clear();
    }

    // to calculating distance between user and lapangan
    private int distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (int)(dist*1.60934);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void createDrawer() {

        Log.d(TAG, "create drawer");
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName(namaUser).withEmail(currentUserEmail.replace(",","."))
                                .withIcon(getResources().getDrawable(R.drawable.template))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Profile").withIcon(R.drawable.profile),
                        new PrimaryDrawerItem().withName("My Transaction").withIcon(R.drawable.history),
                        new PrimaryDrawerItem().withName("Setting").withIcon(R.drawable.setting),
                        new PrimaryDrawerItem().withName("Sign Out").withIcon(R.drawable.signout)
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                Intent intent;
                                switch (position) {
                                    case 1:
                                        //handle profile
                                        intent = new Intent(MainActivity.this, UpdateProfile.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        //handle transaction
                                        intent = new Intent(MainActivity.this, HistoryLapangan.class);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        //handle setting
                                        intent = new Intent(MainActivity.this, ResetPassword.class);
                                        startActivity(intent);
                                        break;
                                    case 4:
                                        //handle sign out
                                        mAuth.signOut();
                                        goToLogin();
                                        break;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        }
                )
                .build();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}