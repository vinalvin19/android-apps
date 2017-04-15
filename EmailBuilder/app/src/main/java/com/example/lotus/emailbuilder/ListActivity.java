package com.example.lotus.emailbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private AccountHeader accountHeader;
    private Drawer drawer = null;

    public ArrayList<Site> employeeArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mSearchView=(SearchView) findViewById(R.id.searchView1);
        listView = (ListView) findViewById(R.id.my_list_view);
        Log.d("TAGES", "Masuk onCreate");

        createDrawer();

        employeeArrayList=new ArrayList<Site>();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Site site = dataSnapshot.getValue(Site.class);
                Log.d("TAGES", "Masuk childhood");

                Log.d("TAGES", "ada " + site.getNama() + " dengan email " + site.getAlamat());

                employeeArrayList.add(new Site(site.getNama(), site.getAlamat(), site.getEmail(), site.getSiteid()));

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

    private void createDrawer() {

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Telkomsel")
                                .withIcon(getResources().getDrawable(R.drawable.header))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Tambah Site"),
                        new PrimaryDrawerItem().withName("Settings"),
                        new PrimaryDrawerItem().withName("History")
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                                Intent intent;

                                switch (position) {
                                    case 1:
                                        intent = new Intent(ListActivity.this, AddSite.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        intent = new Intent(ListActivity.this, SettingActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        intent = new Intent(ListActivity.this, HistorySite.class);
                                        startActivity(intent);
                                    default:
                                        break;
                                }

                                return false;
                            }
                        }
                )
                .build();

    }

}
