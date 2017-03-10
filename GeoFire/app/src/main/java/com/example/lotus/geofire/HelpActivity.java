package com.example.lotus.geofire;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotus on 10/03/2017.
 */

public class HelpActivity extends AppCompatActivity{

    public String namaAccount = "Alvin";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    EditText topic;
    EditText description;
    RatingBar urgency;

    Button findHelp;
    Button getLocation;

    Object topicString;
    Object descString;
    Float urgencyString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        topic = (EditText) findViewById(R.id.topicText);
        description = (EditText) findViewById(R.id.descriptionText);
        urgency = (RatingBar) findViewById(R.id.urgency);
        findHelp = (Button) findViewById(R.id.findHelp);

        topicString = topic.getText();
        descString = description.getText();
        urgencyString = urgency.getRating();


        findHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                myRef.child("user").child(namaAccount).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User akun = dataSnapshot.getValue(User.class);
                        //Log.d("TAG", "Nama: " + akun.getName() + " "+ myRef.child("user").child(namaAccount).child("status").setValue(topicString));
                        Log.d("TAG", "Nama: " + akun.getName() + " "+ akun.getStatus());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                Log.d("TAG", topicString.toString()+descString.toString()+urgencyString);
                // intent ke activity lain setelah done
                Map<String, Object> findHelp = new HashMap<String, Object>();
                findHelp.put("status", "needHelp");
                findHelp.put("topic", topicString.toString());
                findHelp.put("description", descString.toString());
                findHelp.put("urgency", urgencyString.toString());
                myRef.child("user").child(namaAccount).updateChildren(findHelp);
            }
        });
    }
}