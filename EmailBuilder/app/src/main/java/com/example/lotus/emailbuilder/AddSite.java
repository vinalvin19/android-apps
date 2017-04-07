package com.example.lotus.emailbuilder;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotus on 30/03/2017.
 */

public class AddSite extends AppCompatActivity{

    ProgressDialog addSiteDialog = null;

    EditText _idSite;
    EditText _namaSite;
    EditText _alamatSite;
    EditText _emailSite;
    Button simpan;

    String idSite;
    String namaSite;
    String alamatSite;
    String emailSite;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

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

                addSiteDialog = ProgressDialog.show(AddSite.this, "", "Adding Site...", true);

                AddSite.addSiteTask task = new AddSite.addSiteTask();
                task.execute();
            }
        });
    }

    class addSiteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                Map<String, Object> site = new HashMap<String, Object>();
                site.put("siteid", idSite.toUpperCase());
                site.put("nama", namaSite.toUpperCase());
                site.put("alamat", alamatSite.toUpperCase());
                site.put("email", emailSite.toLowerCase());
                myRef.child(namaSite.toUpperCase()).setValue(site);

            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            addSiteDialog.dismiss();
            finish();
            Toast.makeText(getApplicationContext(), "Site Added", Toast.LENGTH_LONG).show();
        }
    }
}
