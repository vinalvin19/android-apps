package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 09/03/2017.
 */

//public class CustomListAdapter extends ArrayAdapter<String> implements Filterable{
public class CustomListAdapter extends BaseAdapter implements Filterable{

    private final Activity context;
    //List<String> name;
    //List<String> alamat;
    //public ArrayList<Site> name;
    //public ArrayList<Site> alamat;
    public ArrayList<Site> employeeArrayList;
    public ArrayList<Site> orig;

    /*public CustomListAdapter(Activity context, List<String> name, List<String> alamat) {
        super(context, R.layout.activity_listview, name);
        this.context = context;
        this.name = name;
        this.alamat = alamat;
    }*/

    public CustomListAdapter(Activity context, ArrayList<Site> employeeArrayList) {
        super();
        this.context = context;
        this.employeeArrayList = employeeArrayList;
    }

    public class SiteHolder
    {
        TextView name;
        TextView alamat;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        /*LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);

        txtTitle.setText(name.get(position));
        txtDesc.setText(alamat.get(position));

        return rowView;*/

        SiteHolder holder;
        if(view==null)
        {
            view=LayoutInflater.from(context).inflate(R.layout.activity_listview, parent, false);
            holder=new SiteHolder();
            holder.name=(TextView) view.findViewById(R.id.title);
            holder.alamat=(TextView) view.findViewById(R.id.description);
            view.setTag(holder);
        }
        else
        {
            holder=(SiteHolder) view.getTag();
        }

        holder.name.setText(employeeArrayList.get(position).getNama());
        holder.alamat.setText(employeeArrayList.get(position).getAlamat());

        return view;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Site> results = new ArrayList<Site>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Site g : orig) {
                            if (g.getNama().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                employeeArrayList = (ArrayList<Site>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}