package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
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
    Context context = null;

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
    private Multipart _multipart = new MimeMultipart();

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

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            namaSite = (String) b.get("name");
            alamatSite = (String) b.get("alamat");
            emailSite = (String) b.get("email");
            Log.d("TAGES", "ada " + namaSite + " dengan alamat " + alamatSite + " dan email " + emailSite);

            judulEmailFull = template.judulEmail + namaSite;
            isiEmailFull = template.isiEmail + namaSite + template.isiEmailTengah + alamatSite + template.isiEmailBawah;

            judulEmail.setText(judulEmailFull);
            isiEmail.setText(isiEmailFull);
            tujuanEmail.setText(emailSite);

        }
        else{
            Toast.makeText(this, "ga keintent", Toast.LENGTH_SHORT).show();
        }

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
                return new PasswordAuthentication("alvinalbuquerque@gmail.com", "sayamaumakan");
            }
        });

        pdialog = ProgressDialog.show(MainActivity.this, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

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

            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            finish();
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }
}