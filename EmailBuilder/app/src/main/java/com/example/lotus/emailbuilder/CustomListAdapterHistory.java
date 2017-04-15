package com.example.lotus.emailbuilder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lotus on 15/04/2017.
 */

public class CustomListAdapterHistory extends BaseAdapter{

    Context context;
    ArrayList<Site> listItem;

    public ArrayList<Site> historySite;

    /*public CustomListAdapterHistory(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomListAdapterHistory(Context context, int resource, ArrayList<Site> historySite) {
        super(context, resource, historySite);
    }*/

    public CustomListAdapterHistory(Context context, ArrayList<Site> historySite) {
        super();
        this.context = context;
        this.historySite = historySite;
    }

    public class SiteHolder
    {
        public TextView nama;
        public TextView status;
        public TextView date;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        //View v = convertView;

        SiteHolder holder;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.activity_listview_history, parent, false);
            holder = new CustomListAdapterHistory.SiteHolder();
            holder.nama = (TextView) v.findViewById(R.id.historyNamaSite);
            holder.status = (TextView) v.findViewById(R.id.historyStatusSite);
            holder.date = (TextView) v.findViewById(R.id.historyDateSite);
            v.setTag(holder);
        } else {
            holder = (CustomListAdapterHistory.SiteHolder) v.getTag();
        }

        Log.d("TAGES", historySite.get(position).getNama() + " " + historySite.get(position).getDate());

        holder.nama.setText(historySite.get(position).getNama());
        holder.status.setText(historySite.get(position).getStatus());
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
