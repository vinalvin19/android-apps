package project.sudden.bookinglapang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import project.sudden.bookinglapang.ui.LoginActivity;
import project.sudden.bookinglapang.ui.MainActivity;
import project.sudden.bookinglapang.ui.MapsActivity;
import project.sudden.bookinglapang.ui.RegisterActivity;

/**
 * Created by mac on 4/8/17.
 */

public class BaseActivity extends AppCompatActivity {

    public String TAG = getClass().getSimpleName()+" BookingLapang ";
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseUser currentUser;
    public ProgressDialog mProgressDialog;

    //public FirebaseDatabase database = FirebaseDatabase.getInstance();
    //public DatabaseReference mRef = database.getReference();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    public void checkUserAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    goToMainActivity();
                    getUserData(user);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    goToLogin();
                }
                // ...
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void getUserData(FirebaseUser firebaseUser) {

        currentUser = firebaseUser;

    }

    public String encodeEmail(String emailToBeEncoded) {

        return emailToBeEncoded.replace(".", ",");

    }

    public String decodeEmail(String emailToBeDecoded) {

        return emailToBeDecoded.replace(",", ".");

    }

    public void goToLogin() {

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void goToRegister() {

        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();

    }

    public void goToMaps() {

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        finish();

    }

    public void goToMainActivity() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loging in...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
