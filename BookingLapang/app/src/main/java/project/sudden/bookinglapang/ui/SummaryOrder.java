package project.sudden.bookinglapang.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 26/05/2017.
 */

public class SummaryOrder extends BaseActivity{

    TextView waktuValid;
    TextView namaPemesan;
    TextView waktuPemesanan;
    TextView tempatPesanan;
    TextView lapanganPesanan;
    TextView waktuBooking;
    TextView totalHarga;

    String namaLapangan;
    String tanggal;
    String harga;
    String jadwal;
    String lapangan;
    String nama;
    String subLapangan;
    String waktuPesan;

    ArrayList order = new ArrayList();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_confirmation);

        waktuValid = (TextView) findViewById(R.id.waktuValid);
        namaPemesan = (TextView) findViewById(R.id.namaPemesan2);
        waktuPemesanan = (TextView) findViewById(R.id.waktuPemesanan2);
        tempatPesanan = (TextView) findViewById(R.id.tempatPesanan2);
        lapanganPesanan = (TextView) findViewById(R.id.lapanganPesanan2);
        waktuBooking = (TextView) findViewById(R.id.waktuBooking2);
        totalHarga = (TextView) findViewById(R.id.totalHarga2);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        namaLapangan= intent.getStringExtra("namaLapangan");
        tanggal= intent.getStringExtra("tanggal");

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Getting data...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();

        Log.d(TAG, mUser.getDisplayName()+tanggal);

        myRef.child("konfirmasi").child(namaLapangan).child(mUser.getDisplayName()+tanggal).addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                order.add(dataSnapshot.getValue().toString());

                if (order.size() == 6)
                    setView();
                /*for (int i = 0; i<order.size(); i++) {
                    Log.d(TAG, order.get(i).toString());
                    if (i == order.size())
                        myRef.child("konfirmasi").child(namaLapangan).child(mUser.getDisplayName()+tanggal).removeEventListener(this);
                }*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void setView()
    {
        harga = order.get(0).toString();
        jadwal = order.get(1).toString();
        lapangan = order.get(2).toString();
        nama = order.get(3).toString();
        subLapangan = order.get(4).toString();
        waktuPesan = order.get(5).toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        Date convertedDatePlus = new Date();
        try {
            convertedDate = dateFormat.parse(waktuPesan);
            convertedDatePlus.setTime(convertedDate.getTime()+7_200_200);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        waktuValid.setText(":  "+dateFormat.format(convertedDatePlus));
        namaPemesan.setText(":  "+nama);
        waktuPemesanan.setText(":  "+waktuPesan);
        tempatPesanan.setText(":  "+lapangan);
        lapanganPesanan.setText(":  "+subLapangan);
        waktuBooking.setText(":  "+jadwal);
        totalHarga.setText(":  "+harga);
    }
}