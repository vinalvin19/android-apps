package com.example.lotus.gorobak;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 09/03/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    List<String> name;
    List<String> distance;
    List<String> image;
    ImageView imageView;
    //private final String[] name;
    //private final String[] distance;
    //private final Integer[] imageId;

    public CustomListAdapter(Activity context, List<String> name, List<String> distance, List<String> image) {
        super(context, R.layout.activity_listview, name);
        this.context = context;
        this.name = name;
        this.distance= distance;
        this.image= image;
        //this.imageId = imageId;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);
        imageView = (ImageView) rowView.findViewById(R.id.listview_image);

        txtTitle.setText(name.get(position));
        txtDesc.setText(distance.get(position)+ " km from you");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(image.get(position));
            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            Log.e("TAGES", "error "+e.getMessage());
        }

        return rowView;
    }
}