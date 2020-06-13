package com.example.roombookingsystem.activities.staff;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor, currentId;

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

        mRoomNo.setText(roomID);
        mRoomCapacity.setText(roomCapacity);
        mRoomHardware.setText(roomHardware);
        mRoomSoftware.setText(roomSoftware);
        mBlock.setText(block);
        mFloor.setText(floor);

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set to available in rooms table
                String room_no = mRoomNo.getText().toString();
                String room_capacity = mRoomCapacity.getText().toString();
                String room_software = mRoomSoftware.getText().toString();
                String room_hardware = mRoomHardware.getText().toString();
                String room_block = mBlock.getText().toString();
                String room_floor = mFloor.getText().toString();
                updateRoom(room_no, room_capacity, room_software, room_hardware, true, room_block, room_floor);

                //remove from bookings table
                mBookingsDatabase.child(room_no).removeValue();
                //remove from users table
                mUserDatabase.child(room_no).removeValue();

                Intent intent = new Intent(getApplicationContext(), StaffDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean updateRoom(String roomno, String roomcapacity, String hardware, String software, boolean available, String block, String floor) {
        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomno);

        Rooms room = new Rooms(roomno, roomcapacity, hardware, software, available, block, floor);
        mRoomsDatabase.setValue(room);
        Toast.makeText(this, "Room updated", Toast.LENGTH_LONG).show();

        return true;
    }

    private ArrayList<Rooms> roomListingResult= new ArrayList<Rooms>();

    private List<Rooms> getRoomListing() {
        return roomListingResult;
    }
}