package com.example.roombookingsystem.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.example.roombookingsystem.activities.staff.BookedRooms;
import com.example.roombookingsystem.activities.staff.StaffDashboardActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminModifyBookedRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase;
    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    private TableLayout mTableLayoutView;
    TextView mRoomNo, mRoomCapacity, mRoomHardware, mRoomSoftware, mBlock, mFloor;
    Button btn_cancel;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor;

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
        btn_cancel = findViewById(R.id.btn_done);

        Intent intent = getIntent();

        roomID = intent.getStringExtra(AdminBookedRooms.ROOM_ID);
        roomCapacity = intent.getStringExtra(AdminBookedRooms.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(AdminBookedRooms.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(AdminBookedRooms.ROOM_SOFTWARE);
        roomIsAvailable = intent.getStringExtra(AdminBookedRooms.ROOM_IS_AVAILABLE);
        block = intent.getStringExtra(AdminBookedRooms.ROOM_BLOCK);
        floor = intent.getStringExtra(AdminBookedRooms.ROOM_FLOOR);

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

                String room_no = mRoomNo.getText().toString();
                String room_capacity = mRoomCapacity.getText().toString();
                String room_software = mRoomSoftware.getText().toString();
                String room_hardware = mRoomHardware.getText().toString();
                String room_block = mBlock.getText().toString();
                String room_floor = mFloor.getText().toString();
                updateRoom(room_no, room_capacity, room_software, room_hardware, true, room_block, room_floor);
                Intent intent = new Intent(getApplicationContext(), AdminDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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