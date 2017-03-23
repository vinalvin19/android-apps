package com.example.lotus.gorobak;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

// https://www.learn2crack.com/2015/10/android-marshmallow-permissions.html
// http://www.mobiledev.tips/2015/11/09/android-gps-locations/

public class MainActivity extends AppCompatActivity implements LocationListener{

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private AccountHeader accountHeader;
    private Drawer drawer = null;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    LocationManager locationManager;
    private GoogleMap googleMap;

    List<Double> arrayLatitude = new ArrayList<>();
    List<Double> arrayLongitude = new ArrayList<>();
    List<String> arrayName = new ArrayList<>();
    List<String> arrayEmail = new ArrayList<>();
    List<Integer> arrayDistance = new ArrayList<>();

    public Double latitudeUser = 0.0;
    public Double longitudeUser = 0.0;
    public Double latitudeSekitar = 0.0;
    public Double longitudeSekitar = 0.0;
    public int jarakTempuh = 0;
    public String namaAccount = "Alvin";
    public String currentUserEmail ="lk";
    Location locationUser = new Location ("User");
    Location locationSekitar = new Location ("Sekitar");

    private Query mQuery;
    //private ArrayList<User> mAdapterItems;
    private ArrayList<String> mAdapterKeys;

    CustomListAdapter adapter;
    //Button addHelp;
    //FloatingActionButton addHelp;
    ListView listView;

    private FirebaseAuth mAuth;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private LocationRequest mLocationRequest;

    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.my_list_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDrawer();
        //writeNewUser("Alvin", 24, 12.31, 13.21);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            currentUserEmail = mUser.getEmail().replace(".",",");
            Log.d("TAGES", "MainActivity.onAuthStateChanged:signed_in:" + currentUserEmail);

            if (checkPermission()) {
                Toast.makeText(getApplicationContext(),"Permission already granted.",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Permission not granted.",Toast.LENGTH_SHORT).show();
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    Toast.makeText(getApplicationContext(),"Permission already granted.",Toast.LENGTH_SHORT).show();
                }
            }

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            } else {
                requestPermission();
            }
        } else {
            // User is signed out
            Log.d("TAGES", "onAuthStateChanged:signed_out");
            Toast.makeText(this, "silahkan login", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        /*myRef.child("user").orderByChild("panggil").addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Toast.makeText(getApplicationContext(), "berubah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAGESI", "masuk location listener");

        latitudeUser=location.getLatitude();
        longitudeUser=location.getLongitude();

        Log.d("TAGESI", "Latitude saya:" + location.getLatitude() + " Longitude:" + location.getLongitude());
        checkPedagang();
    }

    /**
     * GPS turned off, stop watching for updates.
     * @param provider contains data on which provider was disabled
     */
    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()) {
            locationManager.removeUpdates(this);
            checkPedagang();
        } else {
            requestPermission();
        }
    }

    /**
     * GPS turned back on, re-enable monitoring
     * @param provider contains data on which provider was enabled
     */
    @Override
    public void onProviderEnabled(String provider) {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            checkPedagang();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),"Permission Granted, Now you can access location data.",Toast.LENGTH_SHORT).show();
                    if (checkPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
                    } else {
                        requestPermission();
                    }} else {
                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void checkPedagang()
    {
        myRef.child("pedagang").orderByChild("latitude").addChildEventListener(new ChildEventListener(){
            //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Pedagang pedagang = dataSnapshot.getValue(Pedagang.class);
                Log.d("TAGES", "Masuk childhood");

                jarakTempuh = distance(latitudeUser, longitudeUser, pedagang.getLatitude(), pedagang.getLongitude());
                Log.d("TAGES", "ada " + pedagang.getName() + " dengan jarak " + jarakTempuh);

                if (jarakTempuh < 50 && jarakTempuh != 0.0 ) {
                    latitudeSekitar = pedagang.getLatitude();
                    longitudeSekitar = pedagang.getLongitude();

                    Log.d("TAGES", "(geofence) ada " + pedagang.getName() + " dengan jarak " + jarakTempuh);

                    if (!arrayName.contains(pedagang.getName())) {
                        arrayLatitude.add(latitudeSekitar);
                        arrayLongitude.add(longitudeSekitar);
                        arrayName.add(pedagang.getName());
                        arrayEmail.add(pedagang.getEmail());
                        arrayDistance.add(jarakTempuh);
                    }

                    List<String> arrayDistanceBaru = new ArrayList<String>(arrayDistance.size());
                    for (Integer myInt : arrayDistance) {
                        arrayDistanceBaru.add(String.valueOf(myInt));
                    }

                    adapter = new CustomListAdapter(MainActivity.this, arrayName, arrayDistanceBaru);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                        {
                            Toast.makeText(getApplicationContext(), arrayName.get(position).toString(), Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("name", arrayName.get(position).toString());
                            intent.putExtra("email", arrayEmail.get(position).toString());
                            intent.putExtra("emailUser", currentUserEmail);
                            intent.putExtra("nameUser", namaAccount);
                            intent.putExtra("latitude", arrayLatitude.get(position));
                            intent.putExtra("longitude", arrayLongitude.get(position));
                            intent.putExtra("distance", jarakTempuh);
                            intent.putExtra("latitudeUser", latitudeUser);
                            intent.putExtra("longitudeUser", longitudeUser);
                            startActivity(intent);
                        }
                    });
                }

                else{
                    arrayLatitude.clear();
                    arrayLongitude.clear();
                    arrayName.clear();
                    arrayDistance.clear();
                    arrayEmail.clear();

                    List<String> arrayDistanceBaru = new ArrayList<String>(arrayDistance.size());
                    for (Integer myInt : arrayDistance) {
                        arrayDistanceBaru.add(String.valueOf(myInt));
                    }

                    adapter = new CustomListAdapter(MainActivity.this, arrayName, arrayDistanceBaru);
                    listView.setAdapter(adapter);
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

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    private void createDrawer() {

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com")
                                .withIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Belum"),
                        new PrimaryDrawerItem().withName("Settings"),
                        new PrimaryDrawerItem().withName("Sign Out")
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                Intent intent;

                                switch (position) {
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "belum tau", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        mAuth.signOut();
                                        intent = new Intent (getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
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
/*    private void writeNewUser(String name, int age, double latitude, double longitude) {
        User user = new User(age, latitude, longitude);

        myRef.child("user").child(name).setValue(user);
    }*/

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

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
