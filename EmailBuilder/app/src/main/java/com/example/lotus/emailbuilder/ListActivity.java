package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 25/03/2017.
 */

public class ListActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    ListView listView;
    CustomListAdapter adapter;

    List<String> arrayName = new ArrayList<>();
    List<String> arrayEmail = new ArrayList<>();
    List<String> arrayAlamat = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.my_list_view);
        Log.d("TAGES", "Masuk onCreate");

        myRef.addChildEventListener(new ChildEventListener() {
            //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Site site = dataSnapshot.getValue(Site.class);
                Log.d("TAGES", "Masuk childhood");

                Log.d("TAGES", "ada " + site.getNama() + " dengan email " + site.getAlamat());


                if (!arrayName.contains(site.getNama())) {
                    arrayName.add(site.getNama());
                    arrayAlamat.add(site.getAlamat());
                    arrayEmail.add(site.getEmail());
                }


                adapter = new CustomListAdapter(ListActivity.this, arrayName, arrayAlamat);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        /*Toast.makeText(getApplicationContext(), arrayName.get(position).toString() + "\n" +
                                arrayAlamat.get(position).toString() + "\n" +
                                arrayEmail.get(position).toString(), Toast.LENGTH_SHORT).show();*/
                        Intent intent = new Intent(ListActivity.this, MainActivity.class);
                        intent.putExtra("name", arrayName.get(position).toString());
                        intent.putExtra("alamat", arrayAlamat.get(position).toString());
                        intent.putExtra("email", arrayEmail.get(position).toString());
                        startActivity(intent);
                    }
                });
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
                Log.d("TAGES", "Tidak Masuk childhood");
            }

        });
    }
}
