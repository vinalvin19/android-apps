package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    ArrayList tanggal = new ArrayList();
    ArrayList namaLapangan = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_view_history);

        // getting data from local database (sqLite_
        DatabaseHandler db = new DatabaseHandler(this);

        getApplicationContext().deleteDatabase("lapanganList");
        List<Lapangan> contacts = db.getAllSite();

        for (Lapangan cn : contacts) {
            String log = " ,Name: " + cn.getDate() + " ,status: " + cn.getPilihanLapangan();
            Log.d(TAG, log);
            namaLapangan.add(cn.getDate().split(" - ")[0]);
            tanggal.add(cn.getPilihanLapangan());
            sites.add(new Lapangan(cn.getNamaLapangan(), cn.getPilihanLapangan(), cn.getDate()));
        }

        CustomListAdapterHistory historySiteAdapter =new CustomListAdapterHistory(HistoryLapangan.this, sites);
        listView.setAdapter(historySiteAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long l) {
                Intent i = new Intent(HistoryLapangan.this, SummaryOrder.class);
                i.putExtra("namaLapangan", namaLapangan.get(position).toString());
                i.putExtra("tanggal", tanggal.get(position).toString());
                startActivity(i);
            }
        });
    }
}