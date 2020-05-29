package com.example.roombookingsystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.AdminDashboardActivity;
import com.example.roombookingsystem.activities.staff.StaffDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLoginActivity extends AppCompatActivity {
    Button btn_user_login;
    TextView tv_signup;
    EditText mStaffID, mPassword;
    String userType;

    //Firebase Initial variables
    private FirebaseAuth firebaseAuth;
    private String firebaseUserID;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference userDatabaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        //OnAuthStateChanged gets invoked in the UI thread on changes in the authentication state
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                firebaseUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(firebaseUserID != null) {

                    userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUserID);

                    userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                userType =  dataSnapshot.child("type").getValue(String.class);
                                if(userType.equals("staff"))
                                {
                                    Intent intent = new Intent(UserLoginActivity.this, StaffDashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                else if ()
                                {

                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }
        };


        mStaffID = findViewById(R.id.et_user_email);
        mPassword = findViewById(R.id.et_password);

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

                if(TextUtils.isEmpty(mStaffID.getText().toString()))
                {
                    mStaffID.setError("Enter your Email ID");
                }
                else if (TextUtils.isEmpty(mPassword.getText().toString()))
                {
                    mPassword.setError("Please enter your password");
                }
                else {

                    String staffID = mStaffID.getText().toString();
                    String password = mPassword.getText().toString();

                    String type = "staff";
                    //check role and redirect

                    if(type.equals("staff")){

                        Intent userIntent = new Intent(UserLoginActivity.this, StaffDashboardActivity.class);
                        userIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(userIntent);
                    }
                    else if(type.equals("admin")){

                        Intent adminIntent = new Intent(UserLoginActivity.this, AdminDashboardActivity.class);
                        adminIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(adminIntent);
                    }
                    else {

                        Toast.makeText(UserLoginActivity.this, "User is not registered. Please register...", Toast.LENGTH_SHORT).show();

                        //Connect to Firebase. Get current session
                        firebaseAuth = FirebaseAuth.getInstance();












                    }
                }
            }
        });
    }


}
