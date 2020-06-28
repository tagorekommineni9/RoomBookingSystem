package com.example.roombookingsystem.activities.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.RegistrationActivity;
import com.example.roombookingsystem.activities.UserLoginActivity;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModifyBookedRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase,mBookingsDatabase,mUserDatabase;
    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    private TableLayout mTableLayoutView;
    TextView mRoomNo, mRoomCapacity, mRoomHardware, mRoomSoftware, mBlock, mFloor;
    Button btn_cancel;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor, currentId, startTime;
    String date;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_booked_room);


        currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        mBookingsDatabase = FirebaseDatabase.getInstance().getReference("bookings").child(currentId);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentId).child("bookings");

        mRoomNo = findViewById(R.id.et_room_no);
        mRoomCapacity = findViewById(R.id.et_room_capacity);
        mRoomHardware = findViewById(R.id.et_hardware_equipment);
        mRoomSoftware = findViewById(R.id.et_software_equipment);
        mBlock = findViewById(R.id.et_block);
        mFloor = findViewById(R.id.et_floor);
        btn_cancel = findViewById(R.id.btn_done);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffIntent = new Intent(ModifyBookedRoom.this, StaffDashboardActivity.class);

                startActivity(staffIntent);
            }
        });

        Intent intent = getIntent();

        roomID = intent.getStringExtra(BookedRooms.ROOM_ID);
        roomCapacity = intent.getStringExtra(BookedRooms.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(BookedRooms.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(BookedRooms.ROOM_SOFTWARE);
        roomIsAvailable = intent.getStringExtra(BookedRooms.ROOM_IS_AVAILABLE);
        block = intent.getStringExtra(BookedRooms.ROOM_BLOCK);
        floor = intent.getStringExtra(BookedRooms.ROOM_FLOOR);
        date = intent.getStringExtra(BookedRooms.ROOM_DATE);
        startTime = intent.getStringExtra(BookedRooms.ROOM_START_TIME);

        mRoomNo.setText(roomID);
        mRoomCapacity.setText(roomCapacity);
        mRoomHardware.setText(roomHardware);
        mRoomSoftware.setText(roomSoftware);
        mBlock.setText(block);
        mFloor.setText(floor);

        mBookingsDatabase = FirebaseDatabase.getInstance().getReference("bookings").child(roomID);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentId);
        mRoomsDatabase  = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBookingsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if (dataSnapshot.child(date).child(startTime).exists())
                            {

                                //remove from bookings table
                                mBookingsDatabase.child(date).child(startTime).removeValue();

                                //remove from users table
                                mUserDatabase.child("bookings").child(roomID).removeValue();

                                Toast.makeText(getApplicationContext(), "Booking Removed Successfully!", Toast.LENGTH_LONG).show();

                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }



    private ArrayList<Rooms> roomListingResult= new ArrayList<Rooms>();

    private List<Rooms> getRoomListing() {
        return roomListingResult;
    }
}