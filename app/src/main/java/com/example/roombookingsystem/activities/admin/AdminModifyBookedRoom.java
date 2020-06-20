package com.example.roombookingsystem.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.example.roombookingsystem.activities.staff.BookedRooms;
import com.example.roombookingsystem.activities.staff.StaffDashboardActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminModifyBookedRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase, mBookingDatabase, mUserDatabase;
    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    private TableLayout mTableLayoutView;
    TextView mRoomNo, mRoomCapacity, mRoomHardware, mRoomSoftware, mBlock, mFloor;
    Button btncancel;
    String date;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor, currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_booked_room);

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms");

        mRoomNo = findViewById(R.id.et_room_no);
        mRoomCapacity = findViewById(R.id.et_room_capacity);
        mRoomHardware = findViewById(R.id.et_hardware_equipment);
        mRoomSoftware = findViewById(R.id.et_software_equipment);
        mBlock = findViewById(R.id.et_block);
        mFloor = findViewById(R.id.et_floor);
        btncancel = findViewById(R.id.btn_remove);

        Intent intent = getIntent();

        roomID = intent.getStringExtra(AdminBookedRooms.ROOM_ID);
        roomCapacity = intent.getStringExtra(AdminBookedRooms.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(AdminBookedRooms.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(AdminBookedRooms.ROOM_SOFTWARE);
        roomIsAvailable = intent.getStringExtra(AdminBookedRooms.ROOM_IS_AVAILABLE);
        block = intent.getStringExtra(AdminBookedRooms.ROOM_BLOCK);
        floor = intent.getStringExtra(AdminBookedRooms.ROOM_FLOOR);
        currentId = intent.getStringExtra(AdminBookedRooms.ROOM_STAFF_ID);
        date = intent.getStringExtra(AdminBookedRooms.ROOM_DATE);


        mRoomNo.setText(roomID);
        mRoomCapacity.setText(roomCapacity);
        mRoomHardware.setText(roomHardware);
        mRoomSoftware.setText(roomSoftware);
        mBlock.setText(block);
        mFloor.setText(floor);

        mBookingDatabase = FirebaseDatabase.getInstance().getReference("bookings").child(roomID);
        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentId).child("bookings");

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBookingDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            if (dataSnapshot.child(date).child(currentId).exists())
                            {

                                //remove from bookings table
                                mBookingDatabase.child(date).child(currentId).removeValue();

                                //remove from users table
                                mUserDatabase.child("bookings").child(roomID).removeValue();

                                //set Room to available
                                mRoomsDatabase.child("available").setValue(true);

                                Toast.makeText(getApplicationContext(), "Booking Removed!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
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