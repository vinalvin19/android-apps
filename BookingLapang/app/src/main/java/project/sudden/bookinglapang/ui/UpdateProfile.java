package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 31/05/2017.
 */

public class UpdateProfile extends BaseActivity{

    FirebaseUser user;

    TextView judulTv;
    EditText resetName;
    Button save;

    String namaBaru;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        judulTv = (TextView) findViewById(R.id.textView9);
        resetName = (EditText) findViewById(R.id.editNama);
        save = (Button) findViewById(R.id.saveNama);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        judulTv.setText("Nama Pengguna Baru");
        judulTv.setTypeface(face);
        save.setText("SIMPAN");
        save.setTypeface(face);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                namaBaru = resetName.getText().toString();
                Log.d(TAG, namaBaru);

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(namaBaru)
                    .build();

                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User profile updated.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProfile.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
