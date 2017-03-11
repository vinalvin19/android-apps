package com.example.lotus.geofire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Date;

/**
 * Created by Lotus on 10/03/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mNameField, mEmailField, mPassField, mConfPassField;
    private Button mButtonRegister;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Register");

        mNameField = (TextInputEditText) findViewById(R.id.edit_text_name_reg);
        mEmailField = (TextInputEditText) findViewById(R.id.edit_text_email_reg);
        mPassField = (TextInputEditText) findViewById(R.id.edit_text_pass_reg);
        mConfPassField = (TextInputEditText) findViewById(R.id.edit_text_conf_pass_reg);
        mButtonRegister = (Button) findViewById(R.id.button_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mNameField.getText().toString(), mEmailField.getText().toString(), mPassField.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String name, String email, String password) {
        Log.d(TAG, "createAccount:" + name+ " " + email);
        if (!validateForm()) {
            return;
        }

        //showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register Success",
                                    Toast.LENGTH_SHORT).show();
                            onAuthSuccess(task.getResult().getUser());

                            //
                            // addUserToFirebaseDb();
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void onAuthSuccess(FirebaseUser user) {
        createNewUser(user.getUid());
        goToMainActivity();
    }

    private void createNewUser(String userId){
        User user = buildNewUser();
        final String encodedEmail = mEmailField.getText().toString().replace(".", ",");
        myRef.child("user").child(encodedEmail).setValue(user);
    }

    /*this.name = name;
    this.email = email;
    this.age = age;
    this.latitude = latitude;
    this.longitude = longitude;
    this.status = status;
    this.point = point;
    this.rating = rating;
    this.topic = topic;
    this.urgency = urgency;
    this.description = description;*/

    private User buildNewUser() {
        return new User( 1, "free", mEmailField.getText().toString(), 12.33, 13.23,
                mNameField.getText().toString(), 100, 2, "free", "free", 2
        );
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPassField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPassField.setError("Required.");
            valid = false;
        } else {
            mPassField.setError(null);
        }

        String confirmPassword = mConfPassField.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfPassField.setError("Required.");
            valid = false;
        } else {
            mConfPassField.setError(null);
        }

        if (!confirmPassword.equals(password)) {
            mConfPassField.setError("Password didn't match");
            valid = false;
        } else {
            mConfPassField.setError(null);
        }

        return valid;
    }

    public void goToMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

}
