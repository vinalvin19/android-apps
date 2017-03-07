package com.example.lotus.pushsimple;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.DeviceRegistration;
import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    Button buttonSend;
    EditText tujuan;
    EditText judulPesan;
    EditText isiPesan;

    public String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = (Button) findViewById(R.id.send);
        tujuan = (EditText) findViewById(R.id.editText);
        judulPesan = (EditText) findViewById(R.id.editText2);
        isiPesan = (EditText) findViewById(R.id.editText3);

        tujuan.setText("bbb");

        Backendless.initApp( getApplicationContext(), "9FEBB1D9-DCF5-F676-FFF9-3CF748B55000", "27CE7151-0C26-9A8B-FF78-D742F87F9F00", "v1" ); // where to get the argument values for this call

        BackendlessUser user = new BackendlessUser();
        user.setProperty( "email", "ddd@gmail.com" );
        user.setPassword( "zczxcz" );
        user.setProperty( "deviceID", Messaging.DEVICE_ID);
/*
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                Log.i("TAG", "Berhasil");
            }

            public void handleFault(BackendlessFault fault) {
                Log.i("TAG", fault.toString());
            }
        });
*/

         Backendless.Messaging.registerDevice("330204278969", "default", new AsyncCallback<Void>() {
             @Override
             public void handleResponse(Void response) {
                 Toast.makeText(getApplicationContext(), "registered", Toast.LENGTH_LONG).show();
             }

             @Override
             public void handleFault(BackendlessFault fault) {
                 Toast.makeText(getApplicationContext(), "belum registered", Toast.LENGTH_LONG).show();
             }
         });



        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                name = tujuan.getText().toString();

                //BackendlessUser user = new BackendlessUser();
                //user.getProperty("deviceID");
                Backendless.Data.of(BackendlessUser.class).find(new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
                    @Override
                    public void handleResponse(BackendlessCollection<BackendlessUser> users) {
                        Iterator<BackendlessUser> userIterator = users.getCurrentPage().iterator();
                        while (userIterator.hasNext()) {
                            final BackendlessUser user = userIterator.next();

                            if(user.getProperty("name").toString().equals(name)) {
                                Log.d("TAG", user.getProperty("deviceID").toString());
                                //Log.d("TAG", "yeeeyy");

                                DeliveryOptions deliveryOptions = new DeliveryOptions();
                                deliveryOptions.addPushSinglecast(user.getProperty("deviceID").toString());

                                PublishOptions publishOptions = new PublishOptions();
                                publishOptions.putHeader( "android-ticker-text", "You just got a private push notification!" );
                                publishOptions.putHeader( "android-content-title", judulPesan.getText().toString() );
                                publishOptions.putHeader( "android-content-text", isiPesan.getText().toString() );

                                Backendless.Messaging.publish("default", "Message!", publishOptions, deliveryOptions, new BackendlessCallback<MessageStatus>() {
                                    @Override
                                    public void handleResponse(MessageStatus response) {
                                        Log.d("TAG", "publish berhasil");
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Log.d("TAG", "publish belom berhasil");
                                    }
                                });
                            }

                            else
                                Toast.makeText(getApplicationContext(), "User tidak ada", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void handleFault (BackendlessFault backendlessFault) {
                        Log.i("TAG", "Server reported an error - " + backendlessFault.getMessage());
                    }
                });
            }
        });
    }

}
