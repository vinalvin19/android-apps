package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

/**
 * Created by Lotus on 10/05/2017.
 */

public class ResetPassword extends BaseActivity {

    EditText resetPassword;
    ImageButton savePassword;
    String passwordBaru;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        resetPassword = (EditText) findViewById(R.id.editPassword);
        savePassword = (ImageButton) findViewById(R.id.savePassword);

        savePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                passwordBaru = resetPassword.getText().toString();
                Log.d("TAGES", passwordBaru);

                // updating password
                user.updatePassword(passwordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this,"Update password success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPassword.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Update password failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}