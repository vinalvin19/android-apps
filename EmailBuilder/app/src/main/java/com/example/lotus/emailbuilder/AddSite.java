package com.example.lotus.emailbuilder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotus on 30/03/2017.
 */

public class AddSite extends AppCompatActivity{

    EditText _idSite;
    EditText _namaSite;
    EditText _alamatSite;
    EditText _emailSite;
    Button simpan;

    String idSite;
    String namaSite;
    String alamatSite;
    String emailSite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site);

        _idSite = (EditText) findViewById(R.id.idSiteEdit);
        _namaSite = (EditText) findViewById(R.id.namaSiteEdit);
        _alamatSite = (EditText) findViewById(R.id.alamatSiteEdit);
        _emailSite = (EditText) findViewById(R.id.emailEdit);
        simpan = (Button) findViewById(R.id.send);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                idSite = _idSite.getText().toString();
                namaSite= _namaSite.getText().toString();
                alamatSite = _alamatSite.getText().toString();
                emailSite = _emailSite.getText().toString();

                Log.d("TAGES", idSite+" "+ namaSite+" "+alamatSite+" "+emailSite);

                /*Map<String, Object> done = new HashMap<String, Object>();
                done.put("panggil", emailPedagangUpdate);
                myRef.child("user").child(emailUser).updateChildren(done);

                showProgressDialog();*/
            }
        });
    }
}
