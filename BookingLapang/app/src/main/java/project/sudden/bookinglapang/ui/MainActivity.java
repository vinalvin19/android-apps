package project.sudden.bookinglapang.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

    private String TAG = getClass().getSimpleName()+"TAGES";
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

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    ArrayList<Double> arrayLatitude = new ArrayList<>();
    ArrayList<Double> arrayLongitude = new ArrayList<>();
    ArrayList<String> arrayName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkUserAuth();
        registerUI();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            currentUserEmail = mUser.getEmail().replace(".",",");
            Log.d("TAGES", "MainActivity.onAuthStateChanged:signed_in: " + currentUserEmail);
            myRef.child("users").child(currentUserEmail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    namaUser = user.getFullName();
                    Log.d(TAG, "nama user= "+ user.getFullName());
                }
                @Override public void onCancelled(DatabaseError error) { }
            });

            createDrawer();

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

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (checkPermission()) {
                Log.d("TAGES", "minta koordinat harusnya");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
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
    }

    private void registerUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sportsSpinner = (Spinner) findViewById(R.id.spinner_sports);
        searchOnMapsButton = (Button) findViewById(R.id.buttonSearchAll);
        searchEachSpinner = (Button) findViewById(R.id.buttonSearch);

        sportsSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(adapter);

        searchEachSpinner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                checkLapangan();
            }
        });

        searchOnMapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Tast");
                checkLapanganSemua();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        onSelected = item;
        clearArrayList();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void checkLapangan()
    {
        Log.d(TAG, "masuk cek lapangan");

        myRef.child("lapangan").child(onSelected).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Lapangan lapangan= dataSnapshot.getValue(Lapangan.class);

                jarakTempuh = distance(latitudeUser, longitudeUser, lapangan.getLatitude(), lapangan.getLongitude());
                Log.d("TAGES", "(geofence) ada " + lapangan.getNamaLapangan() + " dengan jarak " + jarakTempuh);
                if (jarakTempuh < 100) {

                    if (!arrayName.contains(lapangan.getNamaLapangan())) {
                        arrayLatitude.add(lapangan.getLatitude());
                        arrayLongitude.add(lapangan.getLongitude());
                        arrayName.add(dataSnapshot.getKey());
                        //arrayDistance.add(jarakTempuh);
                    }

                    Intent intent =new Intent(MainActivity.this, MapsActivity.class);
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
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void checkLapanganSemua() {
        Log.d(TAG, "masuk cek lapangan semua");

        myRef.child("lapangan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Lapangan lapangan = snapshot.getValue(Lapangan.class);
                    Log.d(TAG, "ada: "+lapangan.getNamaLapangan() + " dengan latitude " + lapangan.getLatitude());

                    jarakTempuh = distance(latitudeUser, longitudeUser, lapangan.getLatitude(), lapangan.getLongitude());
                    if (jarakTempuh < 100) {

                        if (!arrayName.contains(lapangan.getNamaLapangan())) {
                            arrayLatitude.add(lapangan.getLatitude());
                            arrayLongitude.add(lapangan.getLongitude());
                            arrayName.add(lapangan.getNamaLapangan());
                            //arrayDistance.add(jarakTempuh);
                        }
                    }
                }

                Intent intent =new Intent(MainActivity.this, MapsActivity.class);
                intent.putStringArrayListExtra("namaLapangan", arrayName);
                intent.putExtra("latitudeLapangan", arrayLatitude);
                intent.putExtra("longitudeLapangan", arrayLongitude);
                intent.putExtra("latitudeUser", latitudeUser);
                intent.putExtra("longitudeUser", longitudeUser);
                startActivity(intent);

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

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAGESI", "masuk location listener");

        latitudeUser=location.getLatitude();
        longitudeUser=location.getLongitude();

        Log.d("TAGESI", "Latitude saya:" + location.getLatitude() + " Longitude:" + location.getLongitude());
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
                        new ProfileDrawerItem().withName(namaUser).withEmail(currentUserEmail)
                                .withIcon(getResources().getDrawable(R.drawable.userpic))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Profile"),
                        new PrimaryDrawerItem().withName("Transaction History"),
                        new PrimaryDrawerItem().withName("My Booking"),
                        new PrimaryDrawerItem().withName("Setting"),
                        new PrimaryDrawerItem().withName("Sign Out")
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                Intent intent;
                                switch (position) {
                                    case 1:
                                        //handle profile
                                        Toast.makeText(getApplicationContext(), "Profile", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        //handle transaction
                                        Toast.makeText(getApplicationContext(), "Transaction History", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        //handle booking
                                        Toast.makeText(getApplicationContext(), "My Booking", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 4:
                                        //handle setting
                                        Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 5:
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
        super.onBackPressed();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}