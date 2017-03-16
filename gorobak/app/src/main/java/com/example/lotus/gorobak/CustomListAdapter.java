package com.example.lotus.gorobak;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotus on 09/03/2017.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    List<String> name;
    List<String> distance;
    //private final String[] name;
    //private final String[] distance;
    //private final Integer[] imageId;

    public CustomListAdapter(Activity context, List<String> name, List<String> distance) {
        super(context, R.layout.activity_listview, name);
        this.context = context;
        this.name = name;
        this.distance= distance;
        //this.imageId = imageId;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_listview, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listview_image);

        txtTitle.setText(name.get(position));
        txtDesc.setText(distance.get(position)+ " km from you");

        /*rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), name.get(position), Toast.LENGTH_SHORT).show();

            }
        });*/
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }
}