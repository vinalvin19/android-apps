package com.example.lotus.emailbuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Lotus on 15/04/2017.
 */

public class SettingActivity extends AppCompatActivity{

    EditText emailEdit;
    EditText passwordEdit;
    EditText judulEmailEdit;
    EditText pembukaEdit;
    EditText siteIdEdit;
    EditText alamatIdEdit;
    EditText caseEdit;
    EditText penutupEdit;
    Button saveButton;

    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        emailEdit = (EditText) findViewById(R.id.emailEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        judulEmailEdit = (EditText) findViewById(R.id.judulEmailEdit);
        pembukaEdit = (EditText) findViewById(R.id.pembukaEdit);
        siteIdEdit = (EditText) findViewById(R.id.siteIdEdit);
        alamatIdEdit = (EditText) findViewById(R.id.alamatIdEdit);
        caseEdit = (EditText) findViewById(R.id.caseEdit);
        penutupEdit = (EditText) findViewById(R.id.penutupEdit);
        saveButton = (Button) findViewById(R.id.saveButton);

        /*judulEmailEdit.setText("BACKUP ");
        pembukaEdit.setText("Dear rekans,<br> Mohon bantuannya untuk backup dan open ticket site sbb:");
        siteIdEdit*/

        getSharedPreference();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("email", emailEdit.getText().toString());
                editor.putString("password", passwordEdit.getText().toString());
                editor.putString("judul", judulEmailEdit.getText().toString());
                editor.putString("pembuka", pembukaEdit.getText().toString());
                editor.putString("siteId", siteIdEdit.getText().toString());
                editor.putString("alamatId", alamatIdEdit.getText().toString());
                editor.putString("case", caseEdit.getText().toString());
                editor.putString("penutup", penutupEdit.getText().toString());
                editor.commit();

                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSharedPreference(){
        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        if (sharedpreferences.contains("email")) {
            //Log.d("TAGES", "shared: "+sharedpreferences.getString("email", "null"));
            emailEdit.setText(sharedpreferences.getString("email", ""));
        }
        if (sharedpreferences.contains("password")) {
            passwordEdit.setText(sharedpreferences.getString("password", ""));
        }
        if (sharedpreferences.contains("judul")) {
            judulEmailEdit.setText(sharedpreferences.getString("judul", ""));
        }
        if (sharedpreferences.contains("pembuka")) {
            pembukaEdit.setText(sharedpreferences.getString("pembuka", ""));
        }
        if (sharedpreferences.contains("siteId")) {
            siteIdEdit.setText(sharedpreferences.getString("siteId", ""));
        }
        if (sharedpreferences.contains("alamatId")) {
            alamatIdEdit.setText(sharedpreferences.getString("alamatId", ""));
        }
        if (sharedpreferences.contains("case")) {
            caseEdit.setText(sharedpreferences.getString("case", ""));
        }
        if (sharedpreferences.contains("penutup")) {
            penutupEdit.setText(sharedpreferences.getString("penutup", ""));
        }
    }
}
