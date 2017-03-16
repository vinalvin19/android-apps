package com.example.lotus.gorobak;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private AccountHeader accountHeader;
    private Drawer drawer = null;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    //private GoogleMap googleMap;
    LocationManager locationManager;

    List<Double> arrayLatitude = new ArrayList<>();
    List<Double> arrayLongitude = new ArrayList<>();
    List<String> arrayName = new ArrayList<>();
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

    Button logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.my_list_view);
        //logout = (Button) findViewById(R.id.logout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDrawer();
        //writeNewUser("Alvin", 24, 12.31, 13.21);
        //initializePermission();

        mAuth = FirebaseAuth.getInstance();


        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            // User is signed in
            currentUserEmail = mUser.getEmail().replace(".",",");
            Log.d("TAGES", "MainActivity.onAuthStateChanged:signed_in:" + currentUserEmail);
            //checkCurrentUser();

            myRef.child("user").child(currentUserEmail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User akun = dataSnapshot.getValue(User.class);
                    latitudeUser = akun.getLatitude();
                    longitudeUser = akun.getLongitude();
                    //Log.d("TAGES", "Lokasi: " + user.getLatitude() + " ; " + user.getLongitude());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            // User is signed out
            Log.d("TAGES", "onAuthStateChanged:signed_out");
            Toast.makeText(this, "silahkan login", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        myRef.child("pedagang").orderByChild("latitude").addChildEventListener(new ChildEventListener(){
            //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Pedagang pedagang = dataSnapshot.getValue(Pedagang.class);
                Log.d("TAGES", "Masuk childhood");

                Log.d("TAGES", "Status: " + pedagang.getName() + " "+ pedagang.getName());
                Log.d("TAGES", "koordinat " + latitudeUser +" "+longitudeUser);
                jarakTempuh = distance(latitudeUser, longitudeUser, pedagang.getLatitude(), pedagang.getLongitude());
                Log.d("TAGES", "koordinat teman" + pedagang.getLatitude()+" "+pedagang.getLongitude());


                //if (jarakTempuh < 50 && jarakTempuh != 0.0 && pedagang.getStatus().equals("needHelp")) {
                if (jarakTempuh < 50 && jarakTempuh != 0.0 ) {
                    latitudeSekitar = pedagang.getLatitude();
                    longitudeSekitar = pedagang.getLongitude();

                    Log.d("TAGES", pedagang.getName() + " ada di dekatmu!");
                    Log.d("TAGESI", "Latitude:" + latitudeUser + " Longitude:" + longitudeUser);
                    Log.d("TAGESI", "LatitudeSekitar:" + latitudeSekitar + " LongitudeSekitar:" + longitudeSekitar);
                    Log.d("TAGES", "Distance: " + jarakTempuh);

                    arrayLatitude.add(latitudeSekitar);
                    arrayLongitude.add(longitudeSekitar);
                    arrayName.add(pedagang.getName());
                    arrayDistance.add(jarakTempuh);

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
                            /*Intent intent =new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("name", arrayName.get(position).toString());
                            intent.putExtra("nameUser", namaAccount);
                            intent.putExtra("latitude", arrayLatitude.get(position));
                            intent.putExtra("longitude", arrayLongitude.get(position));
                            intent.putExtra("distance", jarakTempuh);
                            intent.putExtra("latitudeUser", latitudeUser);
                            intent.putExtra("longitudeUser", longitudeUser);
                            startActivity(intent);*/
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }


    /*private void checkCurrentUser() {

        Log.d("TAGES", "current email: " + currentUserEmail);
        myRef.child("user").child(currentUserEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User akun = snapshot.getValue(User.class);
                Log.d("TAGES", "Masuk checkCurrentUser");
                Log.d("TAGES", akun.getEmail());
                latitudeUser = akun.getLatitude();
                longitudeUser = akun.getLongitude();
                Log.d("TAGES", "lokasimu: " + latitudeUser.toString() + " " + longitudeUser.toString());

                *//*if (currentUserEmail.equals(akun.getEmail())) ;
                {
                    Log.d("TAGES", "login akun: " + akun.getName());
                    latitudeUser = akun.getLatitude();
                    longitudeUser = akun.getLongitude();

                    //getlala();
                }*//*
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }*/
        /*myRef.child("user").child(currentUserEmail).addChildEventListener(new ChildEventListener() {
            //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

*//*                User akun = dataSnapshot.getValue(User.class);
                Log.d("TAGES", "Masuk checkCurrentUser");
                Log.d("TAGES", "login akun: " + akun.getName());
                latitudeUser = akun.getLatitude();
                longitudeUser = akun.getLongitude();

                if(currentUserEmail.equals(akun.getEmail()));
                {
                    Log.d("TAGES", "login akun: " + akun.getName());
                    latitudeUser = akun.getLatitude();
                    longitudeUser = akun.getLongitude();

                    //getlala();
                }*//*
                Log.d("TAGES", "masuk cari email awal");
                Log.d("TAGES", dataSnapshot.child("user").child(currentUserEmail).getKey());

                *//* myRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User akun = snapshot.getValue(User.class);
                Log.d("TAGES", "Masuk checkCurrentUser");
                Log.d("TAGES", akun.getEmail());

                if (currentUserEmail.equals(akun.getEmail())) ;
                {
                    Log.d("TAGES", "login akun: " + akun.getName());
                    latitudeUser = akun.getLatitude();
                    longitudeUser = akun.getLongitude();

                    //getlala();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }*//*
                *//*if (dataSnapshot.child("user").child(currentUserEmail).getValue() != null) {

                    Log.d("TAGES", "masuk cari email if");

                Object objLatitude = dataSnapshot.child(currentUserEmail).child("latitude").getValue();
                    //latitudeUser = Double.valueOf(objLatitude.toString());
                    latitudeUser = dataSnapshot.child("user").child(currentUserEmail).getValue();

                    Object objLongitude = dataSnapshot.child(currentUserEmail).child("longitude").getValue();
                    longitudeUser = Double.valueOf(objLongitude.toString());
                    Log.d("TAGES", "sekitarmu: " + latitudeUser.toString() + " " + longitudeUser.toString());

                }*//*
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }*/

    /*public void initializePermission() {

        String permission = "Manifest.permission.ACCESS_FINE_LOCATION";
        String permission2 = "Manifest.permission.ACCESS_COARSE_LOCATION";
        ActivityCompat.requestPermissions(this, new String[]{permission}, 8);
        checkLocationPermission();

        if (googleMap == null) {

            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission2) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission2, permission}, 200);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
            }

*//*
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.i("TAGESI", "GPS dinyalakan");
            } else {
                showGPSDisabledAlertToUser();
            }
*//*

*//*            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }*//*
        }
    }*/

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("TAGESI", "masuk");
            Log.d("TAGESI", "Latitude saya:" + location.getLatitude() + " Longitude:" + location.getLongitude());

            //latitudeUser=location.getLatitude();
            //longitudeUser=location.getLongitude();
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
        //initializePermission();
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

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
