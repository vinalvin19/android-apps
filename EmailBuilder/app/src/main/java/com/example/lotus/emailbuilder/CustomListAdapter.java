package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Lotus on 09/03/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    List<String> name;
    List<String> alamat;

    public CustomListAdapter(Activity context, List<String> name, List<String> alamat) {
        super(context, R.layout.activity_listview, name);
        this.context = context;
        this.name = name;
        this.alamat = alamat;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);

        txtTitle.setText(name.get(position));
        txtDesc.setText(alamat.get(position));

        return rowView;
    }
}