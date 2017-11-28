package com.example.lotus.garudav3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by al-fatih on 27/11/17.
 */

public class SearchDestinationAdapter extends RecyclerView.Adapter<SearchDestinationAdapter.MyViewHolder> {

    private List<Destination> destinationList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView city, airport;

        public MyViewHolder(View itemView) {
            super(itemView);
            city = (TextView)itemView.findViewById(R.id.city);
            airport = (TextView)itemView.findViewById(R.id.airport);
        }
    }

    public SearchDestinationAdapter(List<Destination> destinationList){
        this.destinationList = destinationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.city.setText(destination.getCity());
        holder.airport.setText(destination.getAirport());
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }
}
