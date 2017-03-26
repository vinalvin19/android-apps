package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


// source:
// http://javapapers.com/android/android-email-app-with-gmail-smtp-using-javamail/
// http://www.oodlestechnologies.com/blogs/Send-Mail-in-Android-without-Using-Intent
// http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
// http://www.edumobile.org/android/send-email-on-button-click-without-email-chooser/
// https://www.mindstick.com/Articles/1673/sending-mail-without-user-interaction-in-android

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */

    Button send;
    EditText tujuanEmail;
    EditText judulEmail;
    EditText isiEmail;

    String judulEmailFull;
    String isiEmailFull;
    String namaSite;
    String emailSite;
    String alamatSite;

    ProgressDialog progress;
    Handler handler;

    public ArrayList<Site> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Template template = new Template();

        send = (Button) this.findViewById(R.id.send);
        tujuanEmail= (EditText) findViewById(R.id.editTextTujuan);
        judulEmail = (EditText) findViewById(R.id.editTextJudul);
        isiEmail = (EditText) findViewById(R.id.editTextIsiEmail);

        Log.d("TAGES", "ini run loohh");

        //ArrayList<Site> myList = (ArrayList<Site>) getIntent().getSerializableExtra("email");
        //Intent intent = getIntent();
        //Bundle extras = intent.getExtras();
        //ArrayList<Site> arraylist  = extras.getParcelableArrayList("arraylist");
        //ArrayList<Site> testing = this.getIntent().getParcelableArrayListExtra("arraylist");
        //ObjectName object1 = arrayList[0];

        //Bundle data = this.getIntent().getBundleExtra("result.content");
        //Site site= data.getParcelableArrayList("arraylist");
        //Site site = getIntent().getParcelableExtra("student");
        //Bundle extras = getIntent().getExtras();

        //arrayList = this.getIntent().getExtras().getParcelableArrayList("arraylist");
        //Site namaa = arraylist[0];
        /*Bundle bundle = getIntent().getExtras();
        ArrayList<Site> arraylist = bundle.getParcelableArrayList("mylist");
        */
        Intent i = this.getIntent();
        ArrayList<Site> site = getIntent().getParcelableArrayListExtra("key");

        Log.d("TAGES", site.toString());

        /*Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            *//*namaSite = (String) b.get("name");
            alamatSite = (String) b.get("alamat");
            *//*emailSite = (String) b.get("email");
            Log.d("TAGES", "ada " + namaSite + " dengan alamat " + alamatSite + " dan email " + emailSite);

            judulEmailFull = template.judulEmail + namaSite;
            isiEmailFull = template.isiEmail + namaSite + template.isiEmailTengah + alamatSite + template.isiEmailBawah;

            judulEmail.setText(judulEmailFull);
            isiEmail.setText(isiEmailFull);
            tujuanEmail.setText(emailSite);

        }
        else{
            Toast.makeText(this, "ga keintent", Toast.LENGTH_SHORT).show();
        }*/

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                progress = ProgressDialog.show(MainActivity.this, "Sending email",
                        "Sending a report about " + namaSite, true);

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        // do the thing that takes a long time
                        Log.d("TAGES", "kepencet sih");
                        Log.d("TAGES", "ini judul: " + judulEmailFull);
                        Log.d("TAGES", "ini isi: " + isiEmailFull);
                        Log.d("TAGES", "ini email: " + emailSite);

                        try {
                            GMailSender sender = new GMailSender("alvinalbuquerque@gmail.com", "sayamaumakan");
                            sender.sendMail(judulEmailFull,
                                    isiEmailFull,
                                    "alvinalbuquerque@gmail.com",
                                    emailSite);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Success sending your email", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });

    }
}