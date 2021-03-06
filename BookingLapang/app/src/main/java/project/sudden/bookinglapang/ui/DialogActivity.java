package project.sudden.bookinglapang.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 16/04/2017.
 */

public class DialogActivity extends BaseActivity {

    public Spinner spinner;
    public Spinner spinnerDay;
    public ArrayList<String> subLapangan = new ArrayList<>();
    List<String> statusLapangan = new ArrayList<>();
    List<String> hargaLapangan = new ArrayList<>();
    ArrayList<String> jamLapangan= new ArrayList<>();
    List<String> satuMinggu= new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    Toolbar toolbar;
    TextView data1, data2, data3, data4, data5, data6, data7, data8;
    TextView data9, data10, data11, data12, data13, data14, data15, data16;
    TextView total;

    String item;
    String cabangOlahraga;
    String tempatPilihan;
    TextView desc;
    Button processBook;
    String hari;
    String besokString;
    Integer totalHarga = 0;
    String subLapanganFix;
    String hariJadwal;
    String namaPemesan;
    ImageView gambarLapangan;
    String pathGambar;

    int press1 = 0, press2 = 0, press3 = 0, press4 = 0, press5 = 0, press6 = 0, press7 = 0, press8 = 0;
    int press9 = 0, press10 = 0, press11 = 0, press12 = 0, press13 = 0, press14 = 0, press15 = 0, press16 = 0;

    ValueEventListener searchSubLapangan;
    ValueEventListener searchDescription;
    ValueEventListener searchPrice;
    ArrayAdapter<String> spinnerArrayAdapter;

    Calendar calendar;
    Date today;
    Date tomorrow;
    DateFormat dateFormat;
    DateFormat dateFormat2;
    int tambahHari;

    Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_fragment);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
        TextView pilihanlapanganTv = (TextView) findViewById(R.id.name);
        TextView descriptionTv = (TextView) findViewById(R.id.textView2);
        face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        pilihanlapanganTv.setText("Pilihan Lapangan");
        pilihanlapanganTv.setTypeface(face);
        descriptionTv.setText("Description");
        descriptionTv.setTypeface(face);

        TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);
        desc = (TextView) findViewById(R.id.descText);
        data1 = (TextView) findViewById(R.id.data1);
        data2 = (TextView) findViewById(R.id.data2);
        data3 = (TextView) findViewById(R.id.data3);
        data4 = (TextView) findViewById(R.id.data4);
        data5 = (TextView) findViewById(R.id.data5);
        data6 = (TextView) findViewById(R.id.data6);
        data7 = (TextView) findViewById(R.id.data7);
        data8 = (TextView) findViewById(R.id.data8);
        data9 = (TextView) findViewById(R.id.data9);
        data10 = (TextView) findViewById(R.id.data10);
        data11 = (TextView) findViewById(R.id.data11);
        data12 = (TextView) findViewById(R.id.data12);
        data13 = (TextView) findViewById(R.id.data13);
        data14 = (TextView) findViewById(R.id.data14);
        data15 = (TextView) findViewById(R.id.data15);
        data16 = (TextView) findViewById(R.id.data16);
        total = (TextView) findViewById(R.id.total);
        processBook = (Button) findViewById(R.id.processbook);
        gambarLapangan = (ImageView) findViewById(R.id.gambarLapangan);
        processBook.setEnabled(false);

        // get intent information and assign it to array
        subLapangan = this.getIntent().getStringArrayListExtra("subLapangan");
        cabangOlahraga = this.getIntent().getStringExtra("cabangOlahraga");
        tempatPilihan = this.getIntent().getStringExtra("tempatPilihan");
        namaPemesan = this.getIntent().getStringExtra("namaPemesan");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(tempatPilihan.toUpperCase());
        setSupportActionBar(toolbar);

        // displaying spinner for subLapangan
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subLapangan);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();

        tambahHari = 0;

        // get current day and time for hour booking
        calendar = Calendar.getInstance();
        today = calendar.getTime();
        dateFormat = new SimpleDateFormat("EEEE");
        dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        hari = dateFormat.format(today);

        int i = 0;
        while(i<7){
            satuMinggu.add(dateFormat.format(today)+" "+dateFormat2.format(today));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            today = calendar.getTime();
            Log.i(TAG, satuMinggu.get(i));
            i++;
        }

        Log.d(TAG, "Selected: " + spinner.getSelectedItem().toString());


        // displaying spiner for one week
        ArrayAdapter<String> week_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, satuMinggu);
        week_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(week_adapter);

        checkHarga();

        // when subLapangan spinner being selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                item = parentView.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                statusLapangan.clear();
                hargaLapangan.clear();
                jamLapangan.clear();
                totalHarga = 0;
                total.setText("jumlah: " + totalHarga);
                total.setTypeface(face);
                processBook.setEnabled(false);

                press1 = 0; press2 = 0; press3 = 0; press4 = 0; press5 = 0; press6 = 0; press7 = 0; press8 = 0;
                press9 = 0; press10 = 0; press11 = 0; press12 = 0; press13 = 0; press14 = 0; press15 = 0; press16 = 0;

                myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(item)
                    .child("image").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    pathGambar = snapshot.getValue().toString();
                    Log.d(TAG, "Gambar: " + pathGambar);

                    new DownLoadImageTask(gambarLapangan).execute(pathGambar);
                }
                @Override public void onCancelled(DatabaseError error) { }
                });

                checkDatabase(item, hari);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                statusLapangan.clear();
                hargaLapangan.clear();
                totalHarga = 0;
                jamLapangan.clear();
                processBook.setEnabled(false);

                hari = parentView.getItemAtPosition(position).toString().split(" ")[0];
                totalHarga = 0;
                Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
                total.setText("Total: " + totalHarga);
                total.setTypeface(face);
                press1 = 0; press2 = 0; press3 = 0; press4 = 0; press5 = 0; press6 = 0; press7 = 0; press8 = 0;
                press9 = 0; press10 = 0; press11 = 0; press12 = 0; press13 = 0; press14 = 0; press15 = 0; press16 = 0;
                Log.d(TAG, hari);

                checkDatabase(item, hari);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // sending information to confirmBooking class
        processBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                Log.d(TAG, String.valueOf(hours));
                SimpleDateFormat dateFormat = new SimpleDateFormat("kk");
                String dateforrow = dateFormat.format(cal.getTime());

                Log.d(TAG, dateforrow);

                if(Integer.parseInt(dateforrow) > 21 || Integer.parseInt(dateforrow) < 9) {
                    Log.d(TAG, String.valueOf(hours));
                    AlertDialog.Builder builder = new AlertDialog.Builder(DialogActivity.this);
                    builder.setMessage("Mohon maaf atas ketidaknyamanannya. " +
                            "\n\nUntuk versi aplikasi saat ini, jam operasional kami dari jam 09.00-21.00 WIB. " +
                            "\n\nTerimakasih")
                    .setNegativeButton("Saya Mengerti",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                    }});
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Intent intent = new Intent(DialogActivity.this, ConfirmBooking.class);
                    intent.putExtra("namaPemesan", namaPemesan);
                    intent.putExtra("subLapangan", subLapanganFix);
                    intent.putStringArrayListExtra("jadwalPesanan", jamLapangan);
                    intent.putExtra("lapanganPesanan", tempatPilihan);
                    intent.putExtra("totalHarga", String.valueOf(totalHarga));
                    intent.putExtra("hariDipesan", hari);
                    intent.putExtra("cabangOlahraga", cabangOlahraga);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // huge function to check database based on selected item in spinner and current day
    public void checkDatabase(String sublapangan, String hari) {

        // looping database to get lapangan description
        searchDescription = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
                    desc.setText(eventSnapshot.getValue().toString());
                    desc.setTypeface(face);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };

        // looping database to get price of lapangan
        searchPrice = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                hargaLapangan.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    //Log.d("TAGES", eventSnapshot.getValue().toString());
                    hargaLapangan.add(eventSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        };

        subLapanganFix = sublapangan;
        hariJadwal = hari;

        // looping database to get status lapangan
        searchSubLapangan = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                statusLapangan.clear();
                Log.d(TAG, snapshot.toString());
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    //Log.d("TAGES", "GetChildren: "+ eventSnapshot.getKey() +" "+ eventSnapshot.getValue());
                    if (eventSnapshot.getValue() instanceof String) {
                        statusLapangan.add(eventSnapshot.getValue().toString());
                    }
                }

                // for debugging only
                for (int i = 0; i < statusLapangan.size(); i++)
                    Log.d(TAG, "status: " + i + " " + item + " " + statusLapangan.get(i));


                // check status lapangan form statusLapangan array
                for (int j = 0; j <= 15; j++) {
                    if (j == 0)
                        checkStatus(data1, statusLapangan.get(j).toString());
                    else if (j == 1)
                        checkStatus(data2, statusLapangan.get(j).toString());
                    else if (j == 2)
                        checkStatus(data3, statusLapangan.get(j).toString());
                    else if (j == 3)
                        checkStatus(data4, statusLapangan.get(j).toString());
                    else if (j == 4)
                        checkStatus(data5, statusLapangan.get(j).toString());
                    else if (j == 5)
                        checkStatus(data6, statusLapangan.get(j).toString());
                    else if (j == 6)
                        checkStatus(data7, statusLapangan.get(j).toString());
                    else if (j == 7)
                        checkStatus(data8, statusLapangan.get(j).toString());
                    else if (j == 8)
                        checkStatus(data9, statusLapangan.get(j).toString());
                    else if (j == 9)
                        checkStatus(data10, statusLapangan.get(j).toString());
                    else if (j == 10)
                        checkStatus(data11, statusLapangan.get(j).toString());
                    else if (j == 11)
                        checkStatus(data12, statusLapangan.get(j).toString());
                    else if (j == 12)
                        checkStatus(data13, statusLapangan.get(j).toString());
                    else if (j == 13)
                        checkStatus(data14, statusLapangan.get(j).toString());
                    else if (j == 14)
                        checkStatus(data15, statusLapangan.get(j).toString());
                    else
                        checkStatus(data16, statusLapangan.get(j).toString());

                    myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(subLapanganFix).child(hariJadwal).removeEventListener(searchSubLapangan);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        Log.d(TAG, "ini hari: " + hari);
        // getting database information
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).child(hariJadwal).addValueEventListener(searchSubLapangan);
        myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).orderByKey().
                startAt("description").endAt("description").limitToLast(1).addValueEventListener(searchDescription);

        if (hari.equals("Saturday")){
            Log.d(TAG, "hari sabtu");
            myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan)
                    .child("Sabtu").addValueEventListener(searchPrice);
        }
        else if (hari.equals("Sunday")) {
            Log.d(TAG, "hari minggu");
            myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan)
                    .child("Minggu").addValueEventListener(searchPrice);
        }
        else {
            Log.d(TAG, "hari biasa");
            myRef.child("lapangan").child(cabangOlahraga).child(tempatPilihan).child("sublapangan").child(sublapangan).orderByKey().
                    startAt("8").endAt("23").limitToLast(16).addValueEventListener(searchPrice);
        }
        //myRef.removeEventListener(aaa);
    }

    // checking status lapangan and coloring the table
    public void checkStatus(TextView tv, String status){
        if (status.equals("open")) {
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv.setClickable(true);
        }
        else if (status.equals("process")) {
            tv.setBackgroundColor(Color.parseColor("#FFFF00"));
            tv.setClickable(false);
        }
        else if (status.equals("booked") || status.equals("bookedVendor")) {
            tv.setBackgroundColor(Color.parseColor("#FF0000"));
            tv.setClickable(false);
        }
        else {
            tv.setBackgroundColor(Color.parseColor("#000000"));
            tv.setClickable(false);
        }
    }

    // checking total harga for every element in table being pressed
    public void hitungHarga(int press, String harga, String jam, TextView tv){
        if (press%2 == 1){
            totalHarga += Integer.parseInt(harga);
            jamLapangan.add(jam);
            tv.setBackgroundColor(Color.parseColor("#E3DAC9"));
        }
        else {
            totalHarga -= Integer.parseInt(harga);
            jamLapangan.remove(jam);
            tv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        total.setText("Total: " + totalHarga);
        total.setTypeface(face);
        if (totalHarga > 0) {
            processBook.setEnabled(true);
        }
        else {
            processBook.setEnabled(false);
        }
    }

    // getting harga in every hour
    public void checkHarga(){
        data1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press1++;
                hitungHarga (press1, hargaLapangan.get(0), "08.00", data1);
            }});
        data2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press2++;
                hitungHarga (press2, hargaLapangan.get(1), "09.00", data2);
            }});
        data3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press3++;
                hitungHarga (press3, hargaLapangan.get(2), "10.00", data3);
            }});
        data4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press4++;
                hitungHarga (press4, hargaLapangan.get(3), "11.00", data4);
            }});
        data5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press5++;
                hitungHarga (press5, hargaLapangan.get(4), "12.00", data5);
            }});
        data6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press6++;
                hitungHarga (press6, hargaLapangan.get(5), "13.00", data6);
            }});
        data7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press7++;
                hitungHarga (press7, hargaLapangan.get(6), "14.00", data7);
            }});
        data8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press8++;
                hitungHarga (press8, hargaLapangan.get(7), "15.00", data8);
            }});
        data9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press9++;
                hitungHarga (press9, hargaLapangan.get(8), "16.00", data9);
            }});
        data10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press10++;
                hitungHarga (press10, hargaLapangan.get(9), "17.00", data10);
            }});
        data11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press11++;
                hitungHarga (press11, hargaLapangan.get(10), "18.00", data11);
            }});
        data12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press12++;
                hitungHarga (press12, hargaLapangan.get(11), "19.00", data12);
            }});
        data13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press13++;
                hitungHarga (press13, hargaLapangan.get(12), "20.00", data13);
            }});
        data14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press14++;
                hitungHarga (press14, hargaLapangan.get(13), "21.00", data14);
            }});
        data15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press15++;
                hitungHarga (press15, hargaLapangan.get(14), "22.00", data15);
            }});
        data16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                press16++;
                hitungHarga (press16, hargaLapangan.get(15), "23.00", data16);
            }});
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}

