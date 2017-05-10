package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.CustomListAdapterHistory;
import project.sudden.bookinglapang.model.DatabaseHandler;
import project.sudden.bookinglapang.model.Lapangan;

/**
 * Created by Lotus on 01/05/2017.
 */

public class HistoryLapangan extends BaseActivity {

    ArrayList<Lapangan> sites = new ArrayList<Lapangan>();
    ListView listView;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_view_history);

        // getting data from local database (sqLite_
        DatabaseHandler db = new DatabaseHandler(this);

        List<Lapangan> contacts = db.getAllSite();

        for (Lapangan cn : contacts) {
            String log = " ,Name: " + cn.getNamaLapangan() + " ,status: " + cn.getPilihanLapangan();
            Log.d(TAG, log);
            sites.add(new Lapangan(cn.getNamaLapangan(), cn.getPilihanLapangan(), cn.getDate()));
        }

        CustomListAdapterHistory historySiteAdapter =new CustomListAdapterHistory(HistoryLapangan.this, sites);
        listView.setAdapter(historySiteAdapter);
    }
}
