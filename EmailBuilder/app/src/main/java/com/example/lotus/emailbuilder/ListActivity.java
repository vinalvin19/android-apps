package com.example.lotus.emailbuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 25/03/2017.
 http://www.androidbegin.com/tutorial/android-search-filter-listview-images-and-texts-tutorial/
 */

public class ListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    SearchView mSearchView;
    ListView listView;
    CustomListAdapter employeeAdapter;

    List<String> arrayName = new ArrayList<>();
    List<String> arrayEmail = new ArrayList<>();
    List<String> arrayAlamat = new ArrayList<>();

    public ArrayList<Site> employeeArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSearchView=(SearchView) findViewById(R.id.searchView1);
        listView = (ListView) findViewById(R.id.my_list_view);
        Log.d("TAGES", "Masuk onCreate");

        employeeArrayList=new ArrayList<Site>();

        myRef.addChildEventListener(new ChildEventListener() {
            //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                Site site = dataSnapshot.getValue(Site.class);
                Log.d("TAGES", "Masuk childhood");

                Log.d("TAGES", "ada " + site.getNama() + " dengan email " + site.getAlamat());


                /*if (!arrayName.contains(site.getNama())) {
                    arrayName.add(site.getNama());
                    arrayAlamat.add(site.getAlamat());
                    arrayEmail.add(site.getEmail());
                }


                adapter = new CustomListAdapter(ListActivity.this, arrayName, arrayAlamat);
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);*/

                employeeArrayList.add(new Site(site.getNama(), site.getAlamat(), site.getEmail()));

                employeeAdapter=new CustomListAdapter(ListActivity.this, employeeArrayList);
                listView.setAdapter(employeeAdapter);
                listView.setTextFilterEnabled(true);

                setupSearchView();
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

    private void setupSearchView()
    {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        //mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            CustomListAdapter ca = (CustomListAdapter) listView.getAdapter();
            ca.getFilter().filter(newText);
            //listView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }


}
