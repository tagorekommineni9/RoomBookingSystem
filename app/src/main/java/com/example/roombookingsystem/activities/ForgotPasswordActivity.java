package com.example.roombookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roombookingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText et_registeredemail;
    Button bt_reset;
    MaterialToolbar toolbar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et_registeredemail = findViewById(R.id.et_registeredemail);
        bt_reset = findViewById(R.id.btn_forgotpass_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffIntent = new Intent(ForgotPasswordActivity.this, UserLoginActivity.class);

                startActivity(staffIntent);
            }
        });


        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = et_registeredemail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this,"Please check your email",Toast.LENGTH_LONG).show();
                            et_registeredemail.getText().clear();
                            Intent staffIntent = new Intent(ForgotPasswordActivity.this, UserLoginActivity.class);
                            startActivity(staffIntent);
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}