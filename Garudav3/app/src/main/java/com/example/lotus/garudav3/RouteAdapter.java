package com.example.lotus.garudav3;

/**
 * Created by Lotus on 20/11/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {

    private List<Route> routeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, time, duration, method, point;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            time = (TextView) view.findViewById(R.id.time);
            duration = (TextView) view.findViewById(R.id.duration);
            method = (TextView) view.findViewById(R.id.method);
            point = (TextView) view.findViewById(R.id.point);
        }
    }

    public RouteAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Route route = routeList.get(position);
        holder.name.setText(route.getName());
        holder.price.setText(route.getPrice());
        holder.time.setText(route.getTime());
        holder.duration.setText(route.getDuration());
        holder.method.setText(route.getMethod());
        holder.point.setText(route.getPoints());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}