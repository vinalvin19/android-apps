package com.example.lotus.garudav3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 20/11/2017.
 */

public class SearchResult extends AppCompatActivity{

    private List<Route> routeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RouteAdapter mAdapter;
    Intent intent;
    String arrivalCode;
    int totalPax;
    int addedPrice;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_route);

        mAdapter = new RouteAdapter(routeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItem(this, LinearLayoutManager.VERTICAL, 16));

        Intent i = getIntent();
        arrivalCode = i.getStringExtra("arrivalCode");
        totalPax = i.getIntExtra("totalPax",0);
        addedPrice = i.getIntExtra("addedPrice",0);
        TextView arrival = (TextView)findViewById(R.id.arrival_port);
        arrival.setText(arrivalCode);
        TextView summary = (TextView)findViewById(R.id.summary_book);
        String totalPaxStr = "Sat, 3 Oct, " + totalPax + " pax, Economy";
        summary.setText(totalPaxStr);

        prepareMovieData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                intent = new Intent(SearchResult.this, FillDetails.class);
                intent.putExtra("arrivalCode",arrivalCode);
                intent.putExtra("totalPax",totalPax);
                intent.putExtra("addedPrice",addedPrice);
                String totalPrice = routeList.get(position).getPrice();
                intent.putExtra("price",totalPrice.substring(3, totalPrice.length()-4));
                Log.d("TAG", totalPrice.substring(3, totalPrice.length()-4));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(SearchResult.this, "Long press on position :"+position, Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void prepareMovieData() {
        Route route = new Route("Garuda", "Rp " + (403+addedPrice) +".000", "07.00-08.30", "2h", "Direct", "Earn 40 points");
        routeList.add(route);

        route = new Route("Garuda", "Rp " + (410+addedPrice) +".000", "09.00-10.30", "1h 30m", "Direct", "Earn 30 points");
        routeList.add(route);

        route = new Route("Garuda", "Rp " + (423+addedPrice) +".000", "07.00-08.30", "2h", "Direct", "Earn 40 points");
        routeList.add(route);

        route = new Route("Garuda", "Rp " + (443+addedPrice) +".000", "07.00-08.30", "2h", "Direct", "Earn 40 points");
        routeList.add(route);

        route = new Route("Garuda", "Rp " + (463+addedPrice) +".000", "07.00-08.30", "2h", "Direct", "Earn 40 points");
        routeList.add(route);

        route = new Route("Garuda", "Rp " + (483+addedPrice) +".000", "07.00-08.30", "2h", "Direct", "Earn 40 points");
        routeList.add(route);

        mAdapter.notifyDataSetChanged();
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
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
