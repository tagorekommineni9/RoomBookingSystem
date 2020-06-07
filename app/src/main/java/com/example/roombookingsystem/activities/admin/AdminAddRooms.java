package com.example.roombookingsystem.activities.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddRooms extends Fragment {

    EditText mRoomNo, mRoomCapavity, mSoftwareEquip, mHardwareEquip, mBlock, mFloor;
    Button mAdd;
    String roomNo, roomCapacity, roomSoftware, roomHardware, block, floor;;
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
        mRoomCapavity = view.findViewById(R.id.et_room_capacity);
        mSoftwareEquip = view.findViewById(R.id.et_software_equipment);
        mHardwareEquip = view.findViewById(R.id.et_hardware_equipment);
        mBlock = view.findViewById(R.id.et_block);
        mFloor = view.findViewById(R.id.et_floor);

        mAdd = view.findViewById(R.id.btn_add);

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mRoomNo.getText().toString())) {
                    mRoomNo.setError("Enter Room Number");
                } else if (TextUtils.isEmpty(mRoomCapavity.getText().toString())) {
                    mRoomCapavity.setError("Enter Room Capavity");
                } else if (TextUtils.isEmpty(mSoftwareEquip.getText().toString())) {
                    mSoftwareEquip.setError("Enter Software Equipment");
                } else if (TextUtils.isEmpty(mHardwareEquip.getText().toString())) {
                    mHardwareEquip.setError("Enter Hardware Equipment");
                } else if (TextUtils.isEmpty(mBlock.getText().toString())) {
                    mBlock.setError("Enter Block");
                } else if (TextUtils.isEmpty(mFloor.getText().toString())) {
                    mFloor.setError("Enter Floor");
                }
                else {


                    roomNo = mRoomNo.getText().toString();
                    roomCapacity = mRoomCapavity.getText().toString();
                    roomSoftware = mSoftwareEquip.getText().toString();
                    roomHardware = mHardwareEquip.getText().toString();
                    block = mBlock.getText().toString();
                    floor = mFloor.getText().toString();

                    Query query = FirebaseDatabase.getInstance().getReference().child("rooms").orderByChild("roomno").equalTo(roomNo);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                //create new user
                                roomNo = mRoomNo.getText().toString();
                                roomCapacity = mRoomCapavity.getText().toString();
                                roomSoftware = mSoftwareEquip.getText().toString();
                                roomHardware = mHardwareEquip.getText().toString();
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
                                Toast.makeText(getContext(), "Room number already exists", Toast.LENGTH_LONG).show();
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
