package com.example.roombookingsystem.activities.staff;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
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
public class BookedRooms extends Fragment {

    public static final String ROOM_ID = "roomno";
    public static final String ROOM_CAPACITY = "roomcapacity";
    public static final String ROOM_SOFTWARE = "software";
    public static final String ROOM_HARDWARE = "hardware";
    public static final String ROOM_IS_AVAILABLE = "available";
    public static final String ROOM_BLOCK = "block";
    public static final String ROOM_START_TIME= "block";
    public static final String ROOM_FLOOR = "floor";
    public static final String ROOM_IMAGE = "roomimage";
    public static final String ROOM_STAFF_ID = "staffId";
    public static final String ROOM_DATE = "bookingDate";
    private DatabaseReference mRoomsDatabase, mUserDatabase;
    private DatabaseReference RoomKeyRef;
    private RecyclerView mRoomRecyclerView;
    private RoomsAdapter mRoomItemAdapter;
    private RecyclerView.LayoutManager mRoomLayoutManager;
    String roomID, roomCapacity, roomSoftware, roomHardware, block, floor, currentId, url, staff,currentUserName, staffId, requestedEquipment, bookingPurpose, bookingDate, startTime, endTime;
    public BookedRooms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booked_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRoomRecyclerView = getView().findViewById(R.id.recycler_all_rooms);
        mRoomRecyclerView.setNestedScrollingEnabled(false);
        mRoomRecyclerView.setHasFixedSize(true);
        mRoomLayoutManager = new LinearLayoutManager(getActivity());
        mRoomRecyclerView.setLayoutManager(mRoomLayoutManager);
        mRoomItemAdapter = new RoomsAdapter(getRoomListing(), getActivity());
        mRoomRecyclerView.setAdapter(mRoomItemAdapter);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.child(currentId).child("name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setRoomData();

        if(getRoomListing() != null)
        {
            mRoomItemAdapter.setOnItemClickListener(new RoomsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Rooms room = getRoomListing().get(position);
                    Intent intent = new Intent(getActivity(), ModifyBookedRoom.class);
                    intent.putExtra(ROOM_ID, room.getRoomno());
                    intent.putExtra(ROOM_CAPACITY, room.getRoomcapacity());
                    intent.putExtra(ROOM_HARDWARE, room.getHardware());
                    intent.putExtra(ROOM_SOFTWARE, room.getSoftware());
                    intent.putExtra(ROOM_IS_AVAILABLE, String.valueOf(room.isAvailable()));
                    intent.putExtra(ROOM_BLOCK, room.getBlock());
                    intent.putExtra(ROOM_FLOOR, room.getFloor());
                    intent.putExtra(ROOM_IMAGE, room.getRoomimage());
                    intent.putExtra(ROOM_STAFF_ID, room.getStaffId());
                    intent.putExtra(ROOM_DATE, room.getBookingDate());
                    intent.putExtra(ROOM_START_TIME, room.getStartTime());
                    startActivity(intent);
                }
            });
        }
    }

    private void setRoomData() {
        mRoomsDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");

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
        System.out.println("getRoomListDbInformation Key: " + key);
        RoomKeyRef = mRoomsDatabase.child(key);

        RoomKeyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dateRoomBooked: dataSnapshot.getChildren()) {
                    System.out.println("dateRoomBooked: " + dateRoomBooked);
                    System.out.println("currentId: " + currentId);
                    for (DataSnapshot startTimeRommbooked :dateRoomBooked.getChildren()) {
                        System.out.println("startTimeRommbooked: " + startTimeRommbooked.getKey());
                        System.out.println("startTime: " + startTime);
                        staffId = startTimeRommbooked.child("staffId").getValue().toString();

                        if(currentId.equals(staffId))
                        {
                            System.out.println(startTimeRommbooked.child("roomcapacity").getValue().toString());
                            roomID = startTimeRommbooked.child("roomno").getValue().toString();
                            roomCapacity = startTimeRommbooked.child("roomcapacity").getValue().toString();
                            roomSoftware = startTimeRommbooked.child("software").getValue().toString();
                            roomHardware = startTimeRommbooked.child("hardware").getValue().toString();
                            block = startTimeRommbooked.child("block").getValue().toString();
                            floor = startTimeRommbooked.child("floor").getValue().toString();
                            url  = startTimeRommbooked.child("roomimage").getValue().toString();
                            staff  = startTimeRommbooked.child("staff").getValue().toString();
                            requestedEquipment = startTimeRommbooked.child("requestedEquipment").getValue().toString();
                            bookingPurpose = startTimeRommbooked.child("bookingPurpose").getValue().toString();
                            bookingDate = startTimeRommbooked.child("bookingDate").getValue().toString();
                            startTime = startTimeRommbooked.child("startTime").getValue().toString();
                            endTime = startTimeRommbooked.child("endTime").getValue().toString();

                            System.out.println("requestedEquipment : " + requestedEquipment);
                            System.out.println("bookingPurpose: " + bookingPurpose);

                            //Get current staff name
                            Rooms roomObj = new Rooms(roomID,roomCapacity,roomHardware,roomSoftware, block, floor,url, staff, staffId, requestedEquipment, bookingPurpose, bookingDate, startTime, endTime);
                            roomListingResult.add(roomObj);
                            mRoomItemAdapter.notifyDataSetChanged();
                            /*if(currentUserName.equals(staff)){
                                Rooms roomObj = new Rooms(roomID,roomCapacity,roomHardware,roomSoftware, block, floor,url, staff, staffId, requestedEquipment, bookingPurpose);
                                roomListingResult.add(roomObj);
                                mRoomItemAdapter.notifyDataSetChanged();
                            }*/

                        }

                    }
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