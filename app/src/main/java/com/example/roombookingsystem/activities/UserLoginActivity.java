package com.example.roombookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roombookingsystem.R;

public class UserLoginActivity extends AppCompatActivity {
    Button btn_user_login;
    TextView tv_signup;
    EditText mStaffID, mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

//        getSupportActionBar().setTitle("User Login");
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStaffID = findViewById(R.id.et_user_id);
        mStaffID = findViewById(R.id.et_password);

        btn_user_login=(Button)findViewById(R.id.btn_login);
        tv_signup=(TextView)findViewById(R.id.tv_signup);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),RegistrationActivity.class);

                startActivity(intent);
            }
        });

        btn_user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


}
