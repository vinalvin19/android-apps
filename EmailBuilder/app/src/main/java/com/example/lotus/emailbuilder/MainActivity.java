package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


// source:
// http://javapapers.com/android/android-email-app-with-gmail-smtp-using-javamail/
// http://www.oodlestechnologies.com/blogs/Send-Mail-in-Android-without-Using-Intent
// http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
// http://www.edumobile.org/android/send-email-on-button-click-without-email-chooser/
// https://www.mindstick.com/Articles/1673/sending-mail-without-user-interaction-in-android

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */

    Session session = null;
    ProgressDialog pdialog = null;

    Button send;
    Button editSite;
    EditText tujuanEmail;
    EditText judulEmail;
    EditText isiEmail;

    String judulEmailFull;
    String isiEmailFull;
    String pembuka;
    String siteid;
    String alamatid;
    String caseSite;
    String penutup;
    String idSite;
    String emailSender;
    String namaSite;
    String emailSite;
    String alamatSite;
    String formattedDate;
    String passwordSender;

    DatabaseHandler db;

    ProgressDialog progress;
    private Multipart _multipart = new MimeMultipart();

    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Template template = new Template();

        send = (Button) this.findViewById(R.id.send);
        editSite = (Button) this.findViewById(R.id.editSite);
        tujuanEmail= (EditText) findViewById(R.id.editTextTujuan);
        judulEmail = (EditText) findViewById(R.id.editTextJudul);
        isiEmail = (EditText) findViewById(R.id.editTextIsiEmail);

        Log.d("TAGES", "ini run loohh");

        sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        db = new DatabaseHandler(getApplicationContext());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());

        if (b != null) {
            idSite = (String) b.get("idsite");
            namaSite = (String) b.get("name");
            alamatSite = (String) b.get("alamat");
            emailSite = (String) b.get("email");
            Log.d("TAGES", "ada " + idSite+" "+namaSite + " dengan alamat " + alamatSite + " dan email " + emailSite);

            //judulEmailFull = template.judulEmail + namaSite;
            //isiEmailFull = template.isiEmail + namaSite + template.isiEmailTengah + alamatSite + template.isiEmailBawah;

            if (sharedpreferences.contains("email")) {
                Log.d("TAGES", "shared: "+sharedpreferences.getString("email", "null"));
                emailSender = sharedpreferences.getString("email", "null");
            }
            if (sharedpreferences.contains("password")) {
                Log.d("TAGES", "shared: "+sharedpreferences.getString("password", "null"));
                passwordSender = sharedpreferences.getString("password", "null");
            }
            if (sharedpreferences.contains("judul")) {
                judulEmailFull = sharedpreferences.getString("judul", "")+namaSite;
            }
            if (sharedpreferences.contains("pembuka")) {
                pembuka = sharedpreferences.getString("pembuka", "");
            }
            if (sharedpreferences.contains("siteId")) {
                siteid = sharedpreferences.getString("siteId", "");
            }
            if (sharedpreferences.contains("alamatId")) {
                alamatid = sharedpreferences.getString("alamatId", "");
            }
            if (sharedpreferences.contains("case")) {
                caseSite = sharedpreferences.getString("case", "");
            }
            if (sharedpreferences.contains("penutup")) {
                penutup = sharedpreferences.getString("penutup", "");
            }

            isiEmailFull = pembuka + "\n\n" + siteid + idSite+ "    /   " + namaSite + "\n" + alamatid + alamatSite + "\n"
                    + caseSite + "\n\n" + penutup;

            judulEmail.setText(judulEmailFull);
            isiEmail.setText(isiEmailFull);
            tujuanEmail.setText(emailSite);

        }
        else{
            Toast.makeText(this, "ga keintent", Toast.LENGTH_SHORT).show();
        }

        editSite.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            Intent intent = new Intent(getApplicationContext(), EditSite.class);
            Bundle bundle = new Bundle();
            bundle.putString("idSite", idSite );
            bundle.putString("namaSite", namaSite);
            bundle.putString("alamatSite", alamatSite);
            bundle.putString("emailSite", emailSite);
            intent.putExtras(bundle);
            startActivity(intent);

            }
        });

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                sendingEmail();
            }
        });
    }

    public void sendingEmail()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, passwordSender);
            }
        });

        pdialog = ProgressDialog.show(MainActivity.this, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        boolean status = true;
        String failed;

        @Override
        protected String doInBackground(String... params) {


            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("alvinalbuquerque@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailSite));
                message.setSubject(judulEmailFull);

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(isiEmailFull);
                _multipart.addBodyPart(messageBodyPart);
                message.setContent(_multipart);

                Transport.send(message);

                db.addSite(new Site(namaSite, "Success", formattedDate));
                Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_LONG).show();

            } catch(MessagingException e) {
                Log.d("TAGES", e.toString());
                db.addSite(new Site(namaSite, "Failed", formattedDate));
                status = false;
                failed = e.toString();
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            if (status)
                Toast.makeText(MainActivity.this, "message sent", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(MainActivity.this, failed, Toast.LENGTH_LONG).show();

            finish();
        }
    }

}