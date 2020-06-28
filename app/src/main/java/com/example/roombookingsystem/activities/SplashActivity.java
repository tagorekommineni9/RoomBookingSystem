package com.example.roombookingsystem.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.AdminDashboardActivity;
import com.example.roombookingsystem.activities.staff.StaffDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private DatabaseReference userDatabaseReference, mBookingDatabase;
    String userType;
    String currentDate;
    private static int TIMER = 5000;
    Animation topAnim,bottomAnim;
    TextView logo;
    ImageView image;
    int currentTime;
    boolean flagDeleteBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top);
        bottomAnim =AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        image =findViewById(R.id.imageView);
        logo=findViewById(R.id.textView);


        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mBookingDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");

        deleteOldRoomBookings();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null) {

                    userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());

                    userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                userType =  dataSnapshot.child("type").getValue(String.class);
                                if(userType.equals("staff"))
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            staffDashBoard();
                                            finish();
                                        }
                                    }, TIMER);
                                    //staffDashBoard();
                                }
                                else if (userType.equals("admin"))
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            adminDashBoard();
                                            finish();
                                        }
                                    }, TIMER);

                                }

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            loginScreen();
                            finish();
                        }
                    }, TIMER);
                }
            }
        };
    }

    private void deleteOldRoomBookings() {
        mBookingDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot roomIds : dataSnapshot.getChildren()){
                    for(DataSnapshot dates: roomIds.getChildren())
                    {
                        for(DataSnapshot startTimeBooking: dates.getChildren())
                        {

                            flagDeleteBooking = checkWithCurrentTime(startTimeBooking.child("endTime").getValue().toString(), dates.getKey());
                            if(flagDeleteBooking)
                            {
                                mBookingDatabase.child(roomIds.getKey()).child(dates.getKey()).child(startTimeBooking.getKey()).removeValue();
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int getCurrentTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
        Date date = new Date();
        currentDate = formatter.format(date);

        currentTime = Integer.parseInt(currentDate.split(" ")[1].split(":")[0]);

        return currentTime;
    }

    private String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY");
        Date date = new Date();
        currentDate = formatter.format(date);
        return currentDate;

    }
    private boolean checkWithCurrentTime(String key, String dateDb) {
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();

        if(Integer.parseInt(key) < getCurrentTime() && dateDb.compareTo(getCurrentDate())<=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void loginScreen() {
        Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    private void adminDashBoard() {
        Intent intent = new Intent(SplashActivity.this, AdminDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void staffDashBoard(){
        Intent intent = new Intent(SplashActivity.this, StaffDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}