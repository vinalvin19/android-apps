package com.example.lotus.emailbuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lotus on 15/04/2017.
 */

public class HistorySite extends AppCompatActivity{

    ArrayList<Site> sites = new ArrayList<Site>();
    ListView listView;

    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        listView = (ListView) findViewById(R.id.list_view_history);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        DatabaseHandler db = new DatabaseHandler(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        //db.addSite(new Site("aaa", "bbb", formattedDate));
        //List<Site> sites = db.getAllSite();

        List<Site> contacts = db.getAllSite();

        for (Site cn : contacts) {
            String log = " ,Name: " + cn.getNama() + " ,status: " + cn.getStatus();
            Log.d("TAGES ", log);
            sites.add(new Site (cn.getNama(), cn.getStatus(), cn.getDate()));
        }

        CustomListAdapterHistory historySiteAdapter =new CustomListAdapterHistory(HistorySite.this, sites);
        listView.setAdapter(historySiteAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                /*Intent intent = new Intent(HistorySite.this, NewMessageActivity.class);
                startActivity(intent);*/
                //Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.an‌​droid.gm");
                String url = "https://mail.google.com/mail/u/0/#sent";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        //sites = db.getAllSite();
        /*for (int i = 0; i<2; i++) {
            //sites.add(new Site(cn.getNama(), cn.getStatus(), cn.getDate()));
            sites.add();
            String log = "Name: " + sites. ;
            //String log = "Name: " + sites.get(i) + " ,Status: " + cn.getStatus() + " ,date: " + cn.getDate();
            Log.d("TAGES: ", log);
        }*/

        //CustomListAdapterHistory historySiteAdapter =new CustomListAdapterHistory(HistorySite.this, sites);
        //listView.setAdapter(historySiteAdapter);
    }
}
