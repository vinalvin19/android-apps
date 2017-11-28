package com.example.lotus.garudav3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Lotus on 20/11/2017.
 */

public class FillDetails extends AppCompatActivity {

    Toolbar toolbar;
    Button continueBook;
    Intent intent;
    String arrivalCode;
    int totalPax;
    int addedPrice;
    int priceInt = 0;

    CheckBox travelInsurance;

    TextView priceTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_book_detail);

        continueBook = (Button) findViewById(R.id.continue_book);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        travelInsurance = (CheckBox) findViewById(R.id.travel_insurance);

        Intent i = getIntent();
        arrivalCode = i.getStringExtra("arrivalCode");
        totalPax = i.getIntExtra("totalPax",0);
        final String price = i.getStringExtra("price");

        priceInt = Integer.parseInt(price);

        TextView arrival = (TextView)findViewById(R.id.arrival_port);
        arrival.setText(arrivalCode);
        TextView summary = (TextView)findViewById(R.id.summary_book);
        String totalPaxStr = "Sat, 3 Oct, " + totalPax + " pax, Economy";
        summary.setText(totalPaxStr);
        TextView priceStr = (TextView)findViewById(R.id.price);
        priceStr.setText("Rp " + price + ".000");
        priceTotal = (TextView)findViewById(R.id.price_total);
        priceTotal.setText("Rp " + NumberFormat.getIntegerInstance(Locale.GERMAN).format(priceInt*totalPax) + ".000");

        travelInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    priceInt += 25;
                    priceTotal.setText("Rp " + NumberFormat.getIntegerInstance(Locale.GERMAN).format(priceInt*totalPax) + ".000");
                }
                else {
                    priceInt -= 25;
                    priceTotal.setText("Rp " + NumberFormat.getIntegerInstance(Locale.GERMAN).format(priceInt*totalPax) + ".000");
                }
            }
        });

        TextView bagagge = (TextView)findViewById(R.id.bagagge);
        String bagaggeStr = "CGK - " + arrivalCode;
        bagagge.setText(bagaggeStr);

        continueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceFix = priceTotal.getText().toString();
                intent = new Intent(FillDetails.this, ReviewBooking.class);
                intent.putExtra("arrivalCode",arrivalCode);
                intent.putExtra("totalPax",totalPax);
                intent.putExtra("price",priceFix.substring(3, priceFix.length()-4));
                startActivity(intent);
            }
        });
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
