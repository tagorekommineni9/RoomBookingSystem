package com.example.roombookingsystem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.roombookingsystem.R;

public class RegistrationActivity extends AppCompatActivity {

    EditText mStaffID, mName, mPhone, mEmail, mPassword, mConfirmPassword;
    Button mRegister;
    String staff_id, name, phone, email, password, confirm_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_registration);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStaffID = findViewById(R.id.et_staff_id);
        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);
        mEmail = findViewById(R.id.et_email);
        mPassword = findViewById(R.id.et_password);
        mConfirmPassword = findViewById(R.id.et_confirm_pwd);


        mRegister = findViewById(R.id.btn_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mStaffID.getText().toString()))
                {
                    mStaffID.setError("Enter your ID");
                }
                else if(TextUtils.isEmpty(mName.getText().toString())){
                    mName.setError("Enter your Name");
                }
                else if(TextUtils.isEmpty(mPhone.getText().toString())){
                    mPhone.setError("Enter your Phone number");
                }
                else if(TextUtils.isEmpty(mEmail.getText().toString())){
                    mEmail.setError("Enter your Email");
                }
                else if(TextUtils.isEmpty(mPassword.getText().toString())){
                    mPassword.setError("Enter your Password");
                }
                else if(TextUtils.isEmpty(mConfirmPassword.getText().toString())){
                    mConfirmPassword.setError("Please confirm your password");
                }
                else {


                }
            }
        });


    }
}
