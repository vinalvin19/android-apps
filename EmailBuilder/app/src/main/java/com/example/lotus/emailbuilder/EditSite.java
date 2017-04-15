package com.example.lotus.emailbuilder;

import android.app.ProgressDialog;
import android.content.Intent;
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
 * Created by Lotus on 15/04/2017.
 */

public class EditSite extends AppCompatActivity{

    ProgressDialog editSiteDialog = null;

    EditText editIdSite;
    EditText editNamaSite;
    EditText editAlamatSite;
    EditText editEmailSite;
    Button simpan;

    String idSiteEdit;
    String namaSiteEdit;
    String alamatSiteEdit;
    String emailSiteEdit;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_site);

        editIdSite = (EditText) findViewById(R.id.editIdSite);
        editNamaSite = (EditText) findViewById(R.id.editNamaSite);
        editAlamatSite = (EditText) findViewById(R.id.editAlamatSite);
        editEmailSite = (EditText) findViewById(R.id.editEmailSite);
        simpan = (Button) findViewById(R.id.editSiteSimpan);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            idSiteEdit = (String) b.get("idSite");
            namaSiteEdit = (String) b.get("namaSite");
            alamatSiteEdit = (String) b.get("alamatSite");
            emailSiteEdit = (String) b.get("emailSite");
            Log.d("TAGES", "ada " + idSiteEdit +" "+ namaSiteEdit+ " dengan alamat " + alamatSiteEdit + " dan email " + emailSiteEdit);

            editIdSite.setText(idSiteEdit);
            editNamaSite.setText(namaSiteEdit);
            editAlamatSite.setText(alamatSiteEdit);
            editEmailSite.setText(emailSiteEdit);

        }
        else{
            Toast.makeText(this, "ga keintent", Toast.LENGTH_SHORT).show();
        }

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                idSiteEdit = editIdSite.getText().toString();
                namaSiteEdit= editNamaSite.getText().toString();
                alamatSiteEdit = editAlamatSite.getText().toString();
                emailSiteEdit = editEmailSite.getText().toString();

                Log.d("TAGES", idSiteEdit+" "+ namaSiteEdit+" "+alamatSiteEdit+" "+emailSiteEdit);

                editSiteDialog = ProgressDialog.show(EditSite.this, "", "Editing Site...", true);

                EditSite.editSiteTask task = new EditSite.editSiteTask();
                task.execute();
            }
        });
    }

    class editSiteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{

                Map<String, Object> editSite = new HashMap<String, Object>();
                editSite.put("siteid", idSiteEdit.toUpperCase());
                editSite.put("nama", namaSiteEdit.toUpperCase());
                editSite.put("alamat", alamatSiteEdit.toUpperCase());
                editSite.put("email", emailSiteEdit.toLowerCase());
                myRef.child(namaSiteEdit.toUpperCase()).updateChildren(editSite);


            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            editSiteDialog.dismiss();
            finish();
            Toast.makeText(getApplicationContext(), "Site Edited", Toast.LENGTH_LONG).show();
        }
    }
}
