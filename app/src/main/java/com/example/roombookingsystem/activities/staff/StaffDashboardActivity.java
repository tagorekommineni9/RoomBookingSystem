package com.example.roombookingsystem.activities.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.UserLoginActivity;
import com.example.roombookingsystem.activities.admin.AdminDashboardActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StaffDashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    String firebaseCurrentUserID;
    MaterialToolbar toolbar;
    Boolean mSlideState=false;

    public StaffDashboardActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        //Firebase Connections & id retrieval
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseCurrentUserID = firebaseAuth.getCurrentUser().getUid();

        drawerLayout = findViewById(R.id.my_drawer);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_hamburger);
        toolbar.setTitle("Staff Dashboard");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mSlideState){
                drawerLayout.openDrawer(Gravity.LEFT);
                mSlideState=true;
                }
                else {
                    mSlideState=false;
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });


        setInitFragment();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {

                    case R.id.nav_rooms_available:
                        fragment = new RoomsAvailable();
                        break;
                    case R.id.nav_booked_rooms:
                        fragment = new BookedRooms();
                        break;
                    case R.id.nav_edit_profile:
                        fragment = new EditProfile();
                        break;
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        Toast.makeText(StaffDashboardActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                        Intent signoutIntent = new Intent(StaffDashboardActivity.this, UserLoginActivity.class);
                        signoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(signoutIntent);
                        break;
                }

                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.nav_rooms_available);


    }

    private void setInitFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new RoomsAvailable());
        ft.commit();
    }


}

