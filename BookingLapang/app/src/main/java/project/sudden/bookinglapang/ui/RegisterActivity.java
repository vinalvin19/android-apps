package project.sudden.bookinglapang.ui;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import project.sudden.bookinglapang.BaseActivity;
import project.sudden.bookinglapang.R;
import project.sudden.bookinglapang.model.User;

public class RegisterActivity extends BaseActivity {

    private String TAG = getClass().getSimpleName()+"TAGES";
    private TextInputEditText fullNameTIET, emailAddressTIET, userNameTIET,
            passwordTIET, confirmPasswordTIET;
    private Button registerButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerUI();

    }

    public void registerUI() {

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        fullNameTIET = (TextInputEditText) findViewById(R.id.full_name_tiet_reg);
        emailAddressTIET = (TextInputEditText) findViewById(R.id.email_addr_tiet_reg);
        userNameTIET = (TextInputEditText) findViewById(R.id.user_name_tiet_reg);
        passwordTIET = (TextInputEditText) findViewById(R.id.password_tiet_reg);
        confirmPasswordTIET = (TextInputEditText) findViewById(R.id.conf_password_tiet_reg);
        Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(), "futura.ttf");
        fullNameTIET.setTypeface(face);
        emailAddressTIET.setTypeface(face);
        userNameTIET.setTypeface(face);

        registerButton = (Button) findViewById(R.id.register_btn_reg);
        registerButton.setText("SIGN UP");
        registerButton.setTypeface(face);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegisterCredentials();
            }
        });

    }

    /*private void userRegister() {

        String emailAddressString = emailAddressTIET.getText().toString();
        String passwordString = passwordTIET.getText().toString();*/

    private void createAccount(String name, String email, String password) {
        Log.d(TAG, "createAccount:" + name+ " " + email);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Signing up...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register Failed!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Register Success!",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegisterActivity.this, "Kode Verifikasi telah dikirim ke email Anda",
                                    Toast.LENGTH_LONG).show();
                            onAuthSuccess(task.getResult().getUser());
                        }

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });

    }

    private void onAuthSuccess(FirebaseUser user) {
        //createNewUser(encodeEmail(user.getEmail()));
        createNewUser();
        updateUserData(user);
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                // Re-enable button
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,
                            "Verification email sent", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "sendEmailVerification", task.getException());
                    Toast.makeText(RegisterActivity.this,
                            "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goToLogin();
    }

    private void createNewUser(){
        User user = buildNewUser();
        final String encodedEmail = emailAddressTIET.getText().toString().replace(".", ",");
        myRef.child("users").child(encodedEmail).setValue(user);
        Log.d(TAG, "berhasil create");
    }

    private User buildNewUser() {
        Log.d(TAG, fullNameTIET.getText().toString()+" "+emailAddressTIET.getText().toString());
        return new User(
                fullNameTIET.getText().toString(),
                emailAddressTIET.getText().toString(),
                userNameTIET.getText().toString(),
                "",
                new Date().getTime()
        );
    }

    private void updateUserData(FirebaseUser user) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullNameTIET.getText().toString().replace(" ",""))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

    }


    public void validateRegisterCredentials() {

        if (fullNameTIET.getText().toString().equals("")) {
            fullNameTIET.setError("Full Name Needed");
            fullNameTIET.setFocusable(true);
        } else if (emailAddressTIET.getText().toString().equals("")) {
            emailAddressTIET.setError("Email Address Needed");
            emailAddressTIET.setFocusable(true);
        } else if (userNameTIET.getText().toString().equals("")) {
            userNameTIET.setError("Phone Number Needed");
            userNameTIET.setFocusable(true);
        } else if (passwordTIET.getText().toString().equals("")) {
            passwordTIET.setError("Password Needed");
            passwordTIET.setFocusable(true);
        } else if (!passwordTIET.getText().toString().equals(confirmPasswordTIET.getText().toString())) {
            confirmPasswordTIET.setError("Password didn't match");
            confirmPasswordTIET.setFocusable(true);
        } else {
            Log.d(TAG, "semua benar");
            //userRegister();
            createAccount(fullNameTIET.getText().toString(), emailAddressTIET.getText().toString(), passwordTIET.getText().toString());
        }

    }

}
