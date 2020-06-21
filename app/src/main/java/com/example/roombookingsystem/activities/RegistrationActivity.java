package com.example.roombookingsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.staff.StaffDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class RegistrationActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,10}" +               //at least 4 characters and maximum of 10 characters
            "$");

    EditText mName, mPhone, mEmail, mPassword, mConfirmPassword;
    Button mRegister;
    String  name, phone, email, password;

    MaterialToolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffIntent = new Intent(RegistrationActivity.this, UserLoginActivity.class);

                startActivity(staffIntent);
            }
        });


        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mConfirmPassword = findViewById(R.id.et_confirm_pwd);


        mRegister = findViewById(R.id.btn_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mName.getText().toString())) {
                    mName.setError("Enter your Name");
                } else if (TextUtils.isEmpty(mPhone.getText().toString())) {
                    mPhone.setError("Enter your Phone number");
                } else if (TextUtils.isEmpty(mEmail.getText().toString())) {
                    mEmail.setError("Enter your Email");

                    //Email pattern check
                }  else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    mEmail.setError("Please enter a valid email address");

                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mPassword.setError("Enter your Password");

                    //password pattern check
                } else if (!PASSWORD_PATTERN.matcher(mPassword.getText().toString()).matches()) {
                    mPassword.setError("Password too weak! please include atleast 1 number, 1 uppercase letter, 1 lowercase letter, 1 special character(@#$%^&+=)");
                } else if (TextUtils.isEmpty(mConfirmPassword.getText().toString())) {
                    mConfirmPassword.setError("Please confirm your password");
                } else if (!(mConfirmPassword.getText().toString().equals(mPassword.getText().toString()))){
                    mConfirmPassword.setError("Passwords are not matching!");
                } else {

                    name = mName.getText().toString();
                    email = mEmail.getText().toString();
                    phone = mPhone.getText().toString();
                    password = mPassword.getText().toString();


                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            String staffId = firebaseAuth.getCurrentUser().getUid();
                                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("users").child(staffId);

                                            Map staffInfo = new HashMap<>();

                                            staffInfo.put("name", name);
                                            staffInfo.put("email", email);
                                            staffInfo.put("phone", phone);
                                            staffInfo.put("type", "staff");

                                            currentUserDb.updateChildren(staffInfo);

                                            Toast.makeText(RegistrationActivity.this, "Registered successfully. Please check your email for Verification.", Toast.LENGTH_SHORT).show();
                                            clearFields();
                                        }
                                        else{
                                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });



                               /* Intent staffIntent = new Intent(RegistrationActivity.this, StaffDashboardActivity.class);
                                staffIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(staffIntent); */



                            }
                        }
                    });


                }
            }
        });
    }

    private void clearFields() {
        mName.getText().clear();
        mPassword.getText().clear();
        mConfirmPassword.getText().clear();
        mPhone.getText().clear();
    }



}
