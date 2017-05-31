package project.sudden.bookinglapang.model;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 01/05/2017.
 */

public class CustomListAdapterHistory extends AppCompatActivity implements ListAdapter {

    Context context;
    public ArrayList<Lapangan> historySite;

    public CustomListAdapterHistory(Context context, ArrayList<Lapangan> historySite) {
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
            v = LayoutInflater.from(context).inflate(R.layout.activity_history, parent, false);
            holder = new CustomListAdapterHistory.SiteHolder();
            holder.nama = (TextView) v.findViewById(R.id.historyOrder);
            holder.status = (TextView) v.findViewById(R.id.historyDateLapangan);
            holder.date = (TextView) v.findViewById(R.id.historyLapangan);
            Typeface face= Typeface.createFromAsset(context.getAssets(), "futura.ttf");
            holder.nama.setTypeface(face);
            holder.status.setTypeface(face);
            holder.date.setTypeface(face);
            v.setTag(holder);
        } else {
            holder = (CustomListAdapterHistory.SiteHolder) v.getTag();
        }

        Log.d("TAGES", historySite.get(position).getNamaLapangan() + " " + historySite.get(position).getDate());

        holder.nama.setText(historySite.get(position).getNamaLapangan());
        holder.status.setText(historySite.get(position).getPilihanLapangan());
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

    public int getViewTypeCount() {
        return 1;
    }

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public boolean hasStableIds() {
        return false;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public boolean areAllItemsEnabled() {
        return true;
    }

    public boolean isEnabled(int position) {
        return true;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

}

