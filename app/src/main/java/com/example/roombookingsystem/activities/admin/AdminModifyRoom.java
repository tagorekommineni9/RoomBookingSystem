package com.example.roombookingsystem.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.example.roombookingsystem.activities.staff.RoomsAvailable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminModifyRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase;
    EditText ev_roomNo, ev_roomCapacity, ev_roomHardware, ev_roomSoftware, mBlock, mFloor;
    Spinner sp_available;
    Button btn_done;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor;
    int spinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_room);

        sp_available = (Spinner) findViewById(R.id.spinnerAvailable);

        ArrayList<String> availableList = new ArrayList<>();
        availableList.add("true");
        availableList.add("false");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, availableList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_available.setAdapter(adapter);

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms");

        ev_roomNo = findViewById(R.id.et_room_no);
        ev_roomCapacity = findViewById(R.id.et_room_capacity);
        ev_roomHardware = findViewById(R.id.et_hardware_equipment);
        ev_roomSoftware = findViewById(R.id.et_software_equipment);
        btn_done = findViewById(R.id.btn_done);

        Intent intent = getIntent();

        roomID = intent.getStringExtra(RoomsAvailable.ROOM_ID);
        roomCapacity = intent.getStringExtra(RoomsAvailable.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(RoomsAvailable.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(RoomsAvailable.ROOM_SOFTWARE);
        roomIsAvailable = intent.getStringExtra(RoomsAvailable.ROOM_IS_AVAILABLE);

        System.out.println("Room available intent extra: " + roomIsAvailable);
        spinnerPosition = adapter.getPosition(roomIsAvailable);
        System.out.println("Spinner position: " + spinnerPosition);
        sp_available.setSelection(spinnerPosition);
        ev_roomNo.setText(roomID);
        ev_roomCapacity.setText(roomCapacity);
        ev_roomHardware.setText(roomHardware);
        ev_roomSoftware.setText(roomSoftware);

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ev_roomNo.getText().toString())) {
                    ev_roomNo.setError("Enter Room Number");
                } else if (TextUtils.isEmpty(ev_roomCapacity.getText().toString())) {
                    ev_roomCapacity.setError("Enter Room Capacity");
                } else if (TextUtils.isEmpty(ev_roomSoftware.getText().toString())) {
                    ev_roomSoftware.setError("Enter Software Equipment");
                } else if (TextUtils.isEmpty(ev_roomHardware.getText().toString())) {
                    ev_roomHardware.setError("Enter Hardware Equipment");
                }
                else {
                    String room_no = ev_roomNo.getText().toString();
                    String room_capacity = ev_roomCapacity.getText().toString();
                    String room_software = ev_roomSoftware.getText().toString();
                    String room_hardware = ev_roomHardware.getText().toString();

                    boolean room_available;
                    if(sp_available.getSelectedItem().equals("true"))
                    {
                        room_available = true;
                    }
                    else {
                        room_available = false;
                    }
                    updateRoom(room_no, room_capacity, room_software, room_hardware, room_available);
                }
            }
        });
    }

    private boolean updateRoom(String roomno, String roomcapacity, String hardware, String software, boolean available) {
        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomno);

        Rooms room = new Rooms(roomno, roomcapacity, hardware, software, available);
        mRoomsDatabase.setValue(room);
        Toast.makeText(this, "Room updated", Toast.LENGTH_LONG).show();

        return true;
    }

    private ArrayList<Rooms> roomListingResult= new ArrayList<Rooms>();

    private List<Rooms> getRoomListing() {
        return roomListingResult;
    }
}