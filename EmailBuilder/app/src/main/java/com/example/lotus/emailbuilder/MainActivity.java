package com.example.lotus.emailbuilder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


// source:
// http://javapapers.com/android/android-email-app-with-gmail-smtp-using-javamail/
// http://www.oodlestechnologies.com/blogs/Send-Mail-in-Android-without-Using-Intent
// http://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
// http://www.edumobile.org/android/send-email-on-button-click-without-email-chooser/
// https://www.mindstick.com/Articles/1673/sending-mail-without-user-interaction-in-android

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button send = (Button) this.findViewById(R.id.send);

        Log.d("TAGES", "ini run loohh");

        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Thread(new Runnable() {
                    public void run() {

                        Log.d("TAGES", "kepencet sih");

                        try {
                            GMailSender sender = new GMailSender("alvinalbuquerque@gmail.com", "sayamaumakan");
                            sender.sendMail("This is Subject",
                                    "This is Body",
                                    "alvinalbuquerque@gmail.com",
                                    "alvinmsf94@gmail.com");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }).start();
            }
        });

    }
}