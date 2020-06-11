package com.example.roombookingsystem.activities.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddRooms extends Fragment {

    EditText mRoomNo, mBlock, mFloor;
    Button mAdd;
    String roomNo, roomCapacity, roomSoftware, roomHardware, block, floor;
    Spinner sp_capacity;
    MultiSelectionSpinner sp_hardware, sp_software;
    DatabaseReference RoomDb;

    public AdminAddRooms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_add_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRoomNo = view.findViewById(R.id.et_room_no);
        sp_capacity = (Spinner) view.findViewById(R.id.spinnerCapacity);
        sp_software = (MultiSelectionSpinner) view.findViewById(R.id.spinnerSoftware);
        sp_hardware = (MultiSelectionSpinner) view.findViewById(R.id.spinnerHardware);
        mBlock = view.findViewById(R.id.et_block);
        mFloor = view.findViewById(R.id.et_floor);
        mAdd = view.findViewById(R.id.btn_add);

        ArrayList<Integer> capacityList = new ArrayList<>();
        capacityList.add(5);
        capacityList.add(10);
        capacityList.add(15);
        capacityList.add(20);
        capacityList.add(25);
        capacityList.add(30);
        capacityList.add(35);
        capacityList.add(40);
        capacityList.add(45);
        capacityList.add(50);
        ArrayAdapter<Integer> endAdapter =
                new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, capacityList);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_capacity.setAdapter(endAdapter);

        ArrayList<Item> softwareItems = new ArrayList<>();
        softwareItems.add(Item.builder().name("Java").value(false).build());
        softwareItems.add(Item.builder().name("Android").value(false).build());
        softwareItems.add(Item.builder().name("Kotlin").value(false).build());
        sp_software.setItems(softwareItems);

        ArrayList<Item> hardwareItems = new ArrayList<>();
        hardwareItems.add(Item.builder().name("Mouse").value(false).build());
        hardwareItems.add(Item.builder().name("Keyboard").value(false).build());
        hardwareItems.add(Item.builder().name("Laptop").value(false).build());
        sp_hardware.setItems(hardwareItems);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mRoomNo.getText().toString())) {
                    mRoomNo.setError("Enter Room Number");
                } else if (TextUtils.isEmpty(mBlock.getText().toString())) {
                    mBlock.setError("Enter Block");
                } else if (TextUtils.isEmpty(mFloor.getText().toString())) {
                    mFloor.setError("Enter Floor");
                }
                else {
                    Query query = FirebaseDatabase.getInstance().getReference().child("rooms").orderByChild("roomno").equalTo(mRoomNo.getText().toString());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                //create new user
                                String hardwares= "", softwares ="";
                                int countS = 0, countH = 0;
                                ArrayList<Item> softwareList = new ArrayList<>();
                                softwareList = sp_software.getSelectedItems();
                                for (Item softwareItem: softwareList) {
                                    if(countS ==0) {
                                        softwares += softwareItem.getName();
                                        countS++;
                                    }
                                    else
                                    {
                                        softwares += ", " + softwareItem.getName();
                                    }
                                }
                                ArrayList<Item> hardwareList = new ArrayList<>();
                                hardwareList = sp_hardware.getSelectedItems();
                                for (Item hardwareItem: hardwareList) {
                                    if(countH==0) {
                                        hardwares += hardwareItem.getName();
                                        countH++;
                                    }
                                    else {
                                        hardwares += ", " + hardwareItem.getName();
                                    }
                                }
                                roomNo = mRoomNo.getText().toString();
                                roomCapacity = sp_capacity.getSelectedItem().toString();
                                roomSoftware = softwares;
                                roomHardware = hardwares;
                                block = mBlock.getText().toString();
                                floor = mFloor.getText().toString();
                                Boolean available = true;

                                RoomDb = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomNo);

                                Map roomInfo = new HashMap<>();
                                roomInfo.put("roomno", roomNo);
                                roomInfo.put("roomcapacity", roomCapacity);
                                roomInfo.put("software", roomSoftware);
                                roomInfo.put("hardware", roomHardware);
                                roomInfo.put("available", available);
                                roomInfo.put("block", block);
                                roomInfo.put("floor", floor);

                                RoomDb.updateChildren(roomInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(), "New Room is added successfully!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Room number already exists", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
