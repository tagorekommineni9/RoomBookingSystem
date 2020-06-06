package com.example.roombookingsystem.activities.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.rooms.RoomsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase;
    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    private TableLayout mTableLayoutView;
    TextView tv_roomNo, tv_roomCapacity, tv_roomHardware, tv_roomSoftware;
    Button btn_book;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable;

    public BookRoom() {
        // Required empty public constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

        tv_roomNo = findViewById(R.id.et_room_no);
        tv_roomCapacity = findViewById(R.id.et_room_capacity);
        tv_roomHardware = findViewById(R.id.et_hardware_equipment);
        tv_roomSoftware = findViewById(R.id.et_software_equipment);
        btn_book = findViewById(R.id.btn_book);

        Intent intent = getIntent();

        roomID = intent.getStringExtra(RoomsAvailable.ROOM_ID);
        roomCapacity = intent.getStringExtra(RoomsAvailable.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(RoomsAvailable.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(RoomsAvailable.ROOM_SOFTWARE);
        roomIsAvailable = intent.getStringExtra(RoomsAvailable.ROOM_IS_AVAILABLE);

        tv_roomNo.setText(roomID);
        tv_roomCapacity.setText(roomCapacity);
        tv_roomHardware.setText(roomHardware);
        tv_roomSoftware.setText(roomSoftware);

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRoomsDatabase.child("available").setValue(false);
                Toast.makeText(BookRoom.this, "Room booked successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}