package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lotus on 09/03/2017.
 */

//public class CustomListAdapter extends ArrayAdapter<String> implements Filterable{
public class CustomListAdapter extends BaseAdapter implements Filterable{

    private Activity context;
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

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d("TAGES", employeeArrayList.get(position).getNama());
                Intent intent = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", employeeArrayList.get(position).getNama());
                bundle.putString("alamat", employeeArrayList.get(position).getAlamat());
                bundle.putString("email", employeeArrayList.get(position).getEmail());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

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
        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (orig == null)
        {
            itemID = position;
        }
        else
        {
            itemID = orig.indexOf(employeeArrayList.get(position));
        }
        return itemID;    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}