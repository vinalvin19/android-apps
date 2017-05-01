package com.example.lotus.gorobak;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lotus on 01/05/2017.
 */

public class CustomListAdapterHistory extends BaseAdapter {

    Context context;
    public ArrayList<Pedagang> historySite;

    public CustomListAdapterHistory(Context context, ArrayList<Pedagang> historySite) {
        super();
        this.context = context;
        this.historySite = historySite;
    }

    public class SiteHolder
    {
        public TextView nama;
        public TextView date;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        //View v = convertView;

        SiteHolder holder;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.history_order, parent, false);
            holder = new CustomListAdapterHistory.SiteHolder();
            holder.nama = (TextView) v.findViewById(R.id.historyNamaOrder);
            holder.date = (TextView) v.findViewById(R.id.statusDate);
            v.setTag(holder);
        } else {
            holder = (CustomListAdapterHistory.SiteHolder) v.getTag();
        }

        Log.d("TAGES", historySite.get(position).getName() + " " + historySite.get(position).getDate());

        holder.nama.setText(historySite.get(position).getName());
        holder.date.setText(historySite.get(position).getDate());

        return v;
    }

    public int getCount() {
        return historySite.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }
}
