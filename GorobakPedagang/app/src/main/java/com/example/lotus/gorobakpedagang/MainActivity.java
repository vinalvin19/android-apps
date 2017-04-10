package com.example.lotus.gorobakpedagang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    String emailPedagang="cuankinikmat@v,com";
    String dipanggilUser;

    TextView textNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNama = (TextView) findViewById(R.id.textNama);

        myRef.child("pedagang").child(emailPedagang).addValueEventListener(new ValueEventListener() {
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

    public void dipanggilUser()
    {
        myRef.child("user").child(dipanggilUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                textNama.setText("dipanggil sama "+ user.getName()+ " berjarak "+String.valueOf(user.getLatitude()));
                Log.d("TAGES", "dipanggil sama "+ user.getName()+ " berjarak "+String.valueOf(user.getLatitude()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
