package com.example.roombookingsystem.activities.staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.example.roombookingsystem.activities.admin.rooms.RoomsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoomsAvailable extends Fragment {

    private DatabaseReference mRoomsDatabase;
    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    String roomID, roomCapacity, roomSoftware, roomHardware;

    public RoomsAvailable() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rooms_available, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRoomRecyclerView = getView().findViewById(R.id.recycler_available);
        mRoomRecyclerView.setNestedScrollingEnabled(false);
        mRoomRecyclerView.setHasFixedSize(true);
        mRoomLayoutManager = new LinearLayoutManager(getActivity());
        mRoomRecyclerView.setLayoutManager(mRoomLayoutManager);
        mRoomItemAdapter = new RoomsAdapter(getRoomListing(), getActivity());
        mRoomRecyclerView.setAdapter(mRoomItemAdapter);

        setRoomData();
    }

    private void setRoomData() {
        mRoomsDatabase = FirebaseDatabase.getInstance().getReference().child("rooms");

        mRoomsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for(DataSnapshot roomList : dataSnapshot.getChildren()){

                       getRoomListDbInformation(roomList.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRoomListDbInformation(String key) {

        DatabaseReference RoomKeyRef = mRoomsDatabase.child(key);

        RoomKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    roomID = dataSnapshot.child("roomno").getValue().toString();
                    roomCapacity = dataSnapshot.child("roomcapacity").getValue().toString();
                    roomSoftware = dataSnapshot.child("software").getValue().toString();
                    roomHardware = dataSnapshot.child("hardware").getValue().toString();

                    Rooms roomObj = new Rooms(roomID,roomCapacity,roomSoftware,roomHardware);
                    roomListingResult.add(roomObj);
                    mRoomItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<Rooms> roomListingResult= new ArrayList<Rooms>();

    private List<Rooms> getRoomListing() {
        return roomListingResult;
    }
}
