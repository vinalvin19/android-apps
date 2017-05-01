package com.example.lotus.gorobak;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

/**
 * Created by Lotus on 01/05/2017.
 */

public class HistoryOrder extends AppCompatActivity {

    ArrayList<Pedagang> pedagangs = new ArrayList<Pedagang>();
    ListView listView;

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_order_listview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Riwayat Pesanan");
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_view_history);

        DatabaseHistory db = new DatabaseHistory(this);

        List<Pedagang> contacts = db.getAllSite();

        for (Pedagang cn : contacts) {
            String log = " ,Name: " + cn.getName();
            Log.d("TAGES ", log);
            pedagangs.add(new Pedagang (cn.getName(), cn.getDate()));
        }

        CustomListAdapterHistory historySiteAdapter =new CustomListAdapterHistory(HistoryOrder.this, pedagangs);
        listView.setAdapter(historySiteAdapter);
    }
}