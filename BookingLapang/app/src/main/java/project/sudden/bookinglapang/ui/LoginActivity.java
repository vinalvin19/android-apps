package project.sudden.bookinglapang.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;

public class LoginActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName();
    private TextInputEditText emailAddressTIET, passwordTIET;
    private Button signInButton, registerButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference mFirebaseDatabaseReference;

    Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkUser();
        registerUI();

    }

    private void checkUser(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mUser != null) {
                    if (mUser.isEmailVerified()) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        Log.d("TAGES", "User kali ini" + mUser);
    }

    private void registerUI() {

        emailAddressTIET = (TextInputEditText) findViewById(R.id.email_addr_tiet_login);
        passwordTIET = (TextInputEditText) findViewById(R.id.password_tiet_login);
        signInButton = (Button) findViewById(R.id.sign_in_btn_login);
        registerButton = (Button) findViewById(R.id.register_btn_login);
        face = Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        signInButton.setText("LOGIN");
        registerButton.setText("SIGN UP");
        emailAddressTIET.setTypeface(face);
        signInButton.setTypeface(face);
        registerButton.setTypeface(face);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSignInCredentials();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

    }

    private void userSignIn() {

        String emailAddressString = emailAddressTIET.getText().toString();
        String passwordString = passwordTIET.getText().toString();

        showProgressDialog();
        mAuth.signInWithEmailAndPassword(emailAddressString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        mUser = mAuth.getCurrentUser();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful() && mUser.isEmailVerified())
                            goToMainActivity();
                        else
                        {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth Failed",Toast.LENGTH_SHORT).show();
                        }
                        /*if (!task.isSuccessful() || mUser.isEmailVerified()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth Failed",Toast.LENGTH_SHORT).show();
                        } else {
                            goToMainActivity();
                        }*/

                        // ...
                        hideProgressDialog();
                    }
                });

    }

    private void validateSignInCredentials() {

        if (emailAddressTIET.getText().toString().equals("")) {
            emailAddressTIET.setError("Email Address Needed");
        } else if (passwordTIET.getText().toString().equals("")) {
            passwordTIET.setError("Password Needed");
        } else {
            userSignIn();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
}
