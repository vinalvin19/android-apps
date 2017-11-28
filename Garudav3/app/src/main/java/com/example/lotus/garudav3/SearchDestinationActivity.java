package com.example.lotus.garudav3;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchDestinationActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<Destination> destinationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_destination);

        recyclerView = (RecyclerView) findViewById(R.id.destination_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItem(this, LinearLayoutManager.VERTICAL, 16));

        destinationList.add(new Destination("Surabaya, Indonesia","SUB - Juanda"));
        destinationList.add(new Destination("Bali/Denpasar, Indonesia","DPS - Ngurah Rai Int'l"));
        destinationList.add(new Destination("Yogyakarta, Indonesia","JUG - Adi Sutjipto"));
        destinationList.add(new Destination("Medan, Indonesia","KNO - Kuala Namu"));
        destinationList.add(new Destination("Singapore, Singapore","SIN - Changi Intl"));
        destinationList.add(new Destination("Kuala Lumpur, Malaysia","All airports in Kuala Lumpur"));
        destinationList.add(new Destination("Bangkok, Thailand","All airports in Bangkok"));

        SearchDestinationAdapter adapter = new SearchDestinationAdapter(destinationList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = getIntent();
                intent.putExtra("city", destinationList.get(position).getCity());
                intent.putExtra("airport", destinationList.get(position).getAirport());
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(SearchDestinationActivity.this, "Long press on position :"+position, Toast.LENGTH_LONG).show();
            }
        }));
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


}
