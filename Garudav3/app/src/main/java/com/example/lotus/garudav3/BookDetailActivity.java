package com.example.lotus.garudav3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Lotus on 15/11/2017.
 */

public class BookDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button continueBook;
    Intent intent;

    int totalAdult;
    int totalChildren;
    int totalInfact;
    int addedPrice;

    TextView textTotalAdult;
    TextView textTotalChildren;
    TextView textTotalInfact;

    Button economyButton;
    Button businessButton;
    Button firstClassButton;
    String arrivalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        continueBook = (Button) findViewById(R.id.continue_book);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        economyButton = (Button)findViewById(R.id.class_economy);
        businessButton = (Button)findViewById(R.id.class_business);
        firstClassButton = (Button)findViewById(R.id.class_first_class);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textTotalAdult = (TextView)findViewById(R.id.total_adult);
        textTotalChildren = (TextView)findViewById(R.id.total_children);;
        textTotalInfact = (TextView)findViewById(R.id.total_infact);;

        totalAdult = 0;
        totalChildren = 0;
        totalInfact = 0;
        addedPrice = 0;

        totalAdult++;
        updateNumber();

        Intent i = getIntent();
        arrivalCode = i.getStringExtra("arrivalCode");
//        Toast.makeText(this, "" + arrivalCode, Toast.LENGTH_SHORT).show();

        TextView arrival = (TextView)findViewById(R.id.arrival_port);
        arrival.setText(arrivalCode);

        economyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                economyButton.setBackgroundResource(R.drawable.layout_button_blue_left);
                businessButton.setBackgroundResource(R.drawable.layout_button_white_mid);
                firstClassButton.setBackgroundResource(R.drawable.layout_button_white_right);
                addedPrice = 0;
                economyButton.setTextColor(Color.parseColor("#ffffff"));
                businessButton.setTextColor(Color.parseColor("#8d9298"));
                firstClassButton.setTextColor(Color.parseColor("#8d9298"));
            }
        });

        businessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessButton.setBackgroundResource(R.drawable.layout_button_blue_left);
                economyButton.setBackgroundResource(R.drawable.layout_button_white_left);
                firstClassButton.setBackgroundResource(R.drawable.layout_button_white_right);
                economyButton.setTextColor(Color.parseColor("#8d9298"));
                firstClassButton.setTextColor(Color.parseColor("#8d9298"));
                businessButton.setTextColor(Color.parseColor("#ffffff"));
                addedPrice = 150;
            }
        });

        firstClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstClassButton.setBackgroundResource(R.drawable.layout_button_blue_left);
                economyButton.setBackgroundResource(R.drawable.layout_button_white_left);
                businessButton.setBackgroundResource(R.drawable.layout_button_white_mid);
                economyButton.setTextColor(Color.parseColor("#8d9298"));
                businessButton.setTextColor(Color.parseColor("#8d9298"));
                firstClassButton.setTextColor(Color.parseColor("#ffffff"));
                addedPrice = 300;
            }
        });

        continueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BookDetailActivity.this, SearchResult.class);
                intent.putExtra("arrivalCode",arrivalCode);
                intent.putExtra("totalPax",totalPax);
                intent.putExtra("addedPrice",addedPrice);
                startActivity(intent);
            }
        });
    }

    public void upAdult(View v){
        totalAdult++;
        updateNumber();
    }
    public void downAdult(View v){
        if(totalAdult > 0){
            totalAdult--;
        }
        updateNumber();
    }

    public void upChildren(View v){
        totalChildren++;
        updateNumber();
    }
    public void downChildren(View v){
        if(totalChildren > 0){
            totalChildren--;
        }
        updateNumber();
    }

    public void upInfact(View v){
        totalInfact++;
        updateNumber();
    }
    public void downInfact(View v){
        if(totalInfact > 0){
            totalInfact--;
        }
        updateNumber();
    }

    int totalPax;

    void updateNumber(){

        totalPax = totalAdult + totalChildren + totalInfact;

        String adult = "" + totalAdult;
        String children = "" + totalChildren;
        String infact = "" + totalInfact;


        textTotalAdult.setText(adult);
        textTotalChildren.setText(children);
        textTotalInfact.setText(infact);
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
