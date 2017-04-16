package project.sudden.bookinglapang.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.Lapangan;

/**
 * Created by Lotus on 16/04/2017.
 */

public class DialogActivity extends BaseActivity {

    public Spinner spinner;
    public ArrayList<String> subLapangan = new ArrayList<>();
    List<String> statusLapangan = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    TextView data1, data2, data3, data4, data5, data6, data7, data8;
    TextView data9, data10, data11, data12, data13, data14, data15, data16;

    String item;
    String cabangOlahraga;
    String tempatPilihan;
    TextView desc;
    ValueEventListener searchSubLapangan;
    ValueEventListener searchDescription;
    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_fragment);

        spinner = (Spinner) findViewById(R.id.spinner);

        TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);
        desc = (TextView) findViewById(R.id.descText);
        data1 = (TextView) findViewById(R.id.data1);
        data2 = (TextView) findViewById(R.id.data2);
        data3 = (TextView) findViewById(R.id.data3);
        data4 = (TextView) findViewById(R.id.data4);
        data5 = (TextView) findViewById(R.id.data5);
        data6 = (TextView) findViewById(R.id.data6);
        data7 = (TextView) findViewById(R.id.data7);
        data8 = (TextView) findViewById(R.id.data8);
        data9 = (TextView) findViewById(R.id.data9);
        data10 = (TextView) findViewById(R.id.data10);
        data11 = (TextView) findViewById(R.id.data11);
        data12 = (TextView) findViewById(R.id.data12);
        data13 = (TextView) findViewById(R.id.data13);
        data14 = (TextView) findViewById(R.id.data14);
        data15 = (TextView) findViewById(R.id.data15);
        data16 = (TextView) findViewById(R.id.data16);

        subLapangan = this.getIntent().getStringArrayListExtra("subLapangan");
        cabangOlahraga = this.getIntent().getStringExtra("cabangOlahraga");
        tempatPilihan = this.getIntent().getStringExtra("tempatPilihan");

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subLapangan);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                item = parentView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                statusLapangan.clear();

                checkDatabase(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void checkStatus(TextView tv, String status){
        if (status.equals("open"))
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        else if (status.equals("process"))
            tv.setBackgroundColor(Color.parseColor("#FFFF00"));
        else
            tv.setBackgroundColor(Color.parseColor("#FF0000"));
    }

    public void checkDatabase( String sublapangan) {

        searchDescription = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    desc.setText(eventSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };

        final String huhuh = sublapangan;
        searchSubLapangan = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                statusLapangan.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    if (eventSnapshot.getValue() instanceof String) {
                        statusLapangan.add(eventSnapshot.getValue().toString());
                    }
                }

                for (int i = 0; i < statusLapangan.size(); i++)
                    Log.d("TAGES", "status: " + i + " " + item + " " + statusLapangan.get(i));


                for (int j = 1; j <= 16; j++) {
                    if (j == 1)
                        checkStatus(data1, statusLapangan.get(j).toString());
                    else if (j == 2)
                        checkStatus(data2, statusLapangan.get(j).toString());
                    else if (j == 3)
                        checkStatus(data3, statusLapangan.get(j).toString());
                    else if (j == 4)
                        checkStatus(data4, statusLapangan.get(j).toString());
                    else if (j == 5)
                        checkStatus(data5, statusLapangan.get(j).toString());
                    else if (j == 6)
                        checkStatus(data6, statusLapangan.get(j).toString());
                    else if (j == 7)
                        checkStatus(data7, statusLapangan.get(j).toString());
                    else if (j == 8)
                        checkStatus(data8, statusLapangan.get(j).toString());
                    else if (j == 9)
                        checkStatus(data9, statusLapangan.get(j).toString());
                    else if (j == 10)
                        checkStatus(data10, statusLapangan.get(j).toString());
                    else if (j == 11)
                        checkStatus(data11, statusLapangan.get(j).toString());
                    else if (j == 12)
                        checkStatus(data12, statusLapangan.get(j).toString());
                    else if (j == 13)
                        checkStatus(data13, statusLapangan.get(j).toString());
                    else if (j == 14)
                        checkStatus(data14, statusLapangan.get(j).toString());
                    else if (j == 15)
                        checkStatus(data15, statusLapangan.get(j).toString());
                    else
                        checkStatus(data16, statusLapangan.get(j).toString());

                    myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(huhuh).removeEventListener(searchSubLapangan);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).addValueEventListener(searchSubLapangan);
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).orderByKey().
                startAt("description").endAt("description").limitToLast(1).addValueEventListener(searchDescription);
        //myRef.removeEventListener(aaa);
    }
}
