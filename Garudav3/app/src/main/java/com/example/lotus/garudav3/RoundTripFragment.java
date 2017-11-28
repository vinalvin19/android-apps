package com.example.lotus.garudav3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RoundTripFragment extends Fragment {

    ImageView searchDestination;
    TextView searchTravel;
    Intent intent;


    int REQUEST_CODE = 99;
    String arrivalCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rel = inflater.inflate(R.layout.fragment_roundtrip, container, false);

        searchTravel = (TextView) rel.findViewById(R.id.search_travel);

        searchTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), SearchDestinationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        searchDestination = (ImageView) rel.findViewById(R.id.search_destination);

        searchDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("arrivalCode",arrivalCode);
                startActivity(intent);
            }
        });


        return rel;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Jika kembali dengan menekan list item
        if (resultCode == -1) {
            String city = data.getStringExtra("city");
            String airport = data.getStringExtra("airport");
            searchTravel.setText(city);

            airport = airport.substring(0, airport.indexOf(" "));
            arrivalCode = airport;
//            Toast.makeText(getActivity(), "" + arrivalCode, Toast.LENGTH_SHORT).show();
        }
    }

}