package com.example.lotus.garudav3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Lotus on 20/11/2017.
 */

public class ReviewBooking extends AppCompatActivity {

    Toolbar toolbar;
    String arrivalCode;
    int totalPax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_booking);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        arrivalCode = i.getStringExtra("arrivalCode");
        totalPax = i.getIntExtra("totalPax",0);
        final String price = i.getStringExtra("price");

        TextView arrival = (TextView)findViewById(R.id.arrival_port);
        arrival.setText(arrivalCode);

        TextView passengerTotal = (TextView) findViewById(R.id.passenger_name);
        passengerTotal.setText(totalPax + " person(s)");

        TextView priceStr = (TextView)findViewById(R.id.price);
        priceStr.setText("Rp " + price + ".000");

        TextView priceTotal = (TextView)findViewById(R.id.price_total);
        priceTotal.setText("Rp " + NumberFormat.getIntegerInstance(Locale.GERMAN).format(Integer.parseInt(price.replace(".",""))) + ".303");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}