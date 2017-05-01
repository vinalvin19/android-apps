package com.example.lotus.gorobakpedagang;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;

    TextView textNama;
    Button logout;

    public String currentUserEmail ="null";
    String dipanggilUser;

    LocationManager locationManager;
    public Double latitudePedagang = -6.76;
    public Double longitudePedagang = 107.8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textNama = (TextView) findViewById(R.id.textNama);
        logout = (Button) findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            currentUserEmail = mUser.getEmail().replace(".",",");
            Log.d("TAGES", "MainActivity.onAuthStateChanged:signed_in:" + currentUserEmail);
            checkDipanggil();

            if (checkPermission()) {
                Log.d("TAGES", "Permission already granted.");
            }
            else {
                Toast.makeText(getApplicationContext(),"Permission not granted.",Toast.LENGTH_SHORT).show();
                if (!checkPermission()) {
                    requestPermission();
                } else {
                    Log.d("TAGES", "Permission already granted.");
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mAuth.signOut();
            Intent intent = new Intent (getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            }
        });
    }

    public void checkDipanggil(){
        myRef.child("pedagang").child(currentUserEmail).orderByKey()
            .startAt("dipanggil").endAt("dipanggil").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAGES", "berubah");

                Pedagang pedagang = dataSnapshot.getValue(Pedagang.class);
                dipanggilUser = pedagang.getDipanggil();
                Log.d("TAGES", pedagang.getDipanggil());

                dipanggilUser();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAGESI", "masuk location listener");

        latitudePedagang=location.getLatitude();
        longitudePedagang=location.getLongitude();

        Log.d("TAGESI", "Latitude saya:" + location.getLatitude() + " Longitude:" + location.getLongitude());

        Map<String, Object> done = new HashMap<String, Object>();
        done.put("latitude", latitudePedagang);
        done.put("longitude", longitudePedagang);
        myRef.child("pedagang").child(currentUserEmail).updateChildren(done);
    }

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    public void dipanggilUser()
    {
        myRef.child("user").child(dipanggilUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                //textNama.setText("dipanggil sama "+ user.getName()+ " berjarak "+String.valueOf(user.getLatitude()));
                Log.d("TAGES", "dipanggil sama "+ user.getName()+ " berjarak "+String.valueOf(user.getLatitude()));

                Uri sound = Uri.parse("android.resource://" + "com.example.lotus.gorobakpedagang" + "/" + R.raw.order);

                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle("gorobak").setContentText(user.getName()+ " di "+user.getLokasi())
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hand25))
                        .setContentTitle("Kamu mendapat pesanan")
                        .setSmallIcon(R.drawable.hand25)
                        .setAutoCancel(true)
                        .setSound(sound)
                        .build();

                notif.notify((int)Calendar.getInstance().getTimeInMillis(), notify);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
}
