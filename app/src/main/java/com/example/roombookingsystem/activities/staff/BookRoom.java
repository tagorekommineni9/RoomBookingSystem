package com.example.roombookingsystem.activities.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.admin.Item;
import com.example.roombookingsystem.activities.admin.MultiSelectionSpinner;
import com.example.roombookingsystem.activities.admin.rooms.Rooms;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase, mBookingsDatabase, mUserDatabase,mBookingTableDatabase;
    MaterialToolbar toolbar;
    TextView tv_roomNo, tv_roomCapacity, tv_roomHardware, tv_roomSoftware, tv_block, tv_floor;
    Button btn_book;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor, url, staffName;
    String dateFlag = "", currentId;
    Spinner startTimeSpinner, endTimeSpinner, bookingPurposeSpinner;
    //MultiSelectionSpinner sp_hardware, sp_software;
    private TextView mDate, mDuration;
    EditText requestedEquipment;
    DatabaseReference bookingReference;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Map<String, Integer> startDurationCalc = new HashMap<>();
    private Map<String, Integer> endDurationCalc = new HashMap<>();
    private String start, end, dateBooking;
    private String[] startSplit, endSplit;
    private int durationTime, startTime, endTime, hours;
    private int startTimeDb, endTimeDb;


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
        tv_block = findViewById(R.id.et_block);
        tv_floor = findViewById(R.id.et_floor);
        btn_book = findViewById(R.id.btn_book);
        mDate = (TextView) findViewById(R.id.et_date);
        startTimeSpinner = (Spinner) findViewById(R.id.spinnerStartTime);
        endTimeSpinner = (Spinner) findViewById(R.id.spinnerEndTime);
        mDuration = findViewById(R.id.et_duration);
        bookingPurposeSpinner = (Spinner) findViewById(R.id.spinnerBookingPurpose);
        requestedEquipment = findViewById(R.id.et_requestedEquipment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //software and hardware
        /*sp_software = findViewById(R.id.spinnerSoftware);
        sp_hardware = findViewById(R.id.spinnerHardware);

        ArrayList<Item> softwareItems = new ArrayList<>();
        softwareItems.add(Item.builder().name("Java").value(false).build());
        softwareItems.add(Item.builder().name("Android").value(false).build());
        softwareItems.add(Item.builder().name("Kotlin").value(false).build());
        softwareItems.add(Item.builder().name("My Sql Workbench").value(false).build());
        softwareItems.add(Item.builder().name("Git").value(false).build());
        softwareItems.add(Item.builder().name("Intelli J").value(false).build());
        softwareItems.add(Item.builder().name("Net Beans").value(false).build());
        softwareItems.add(Item.builder().name("Microsoft Office").value(false).build());
        sp_software.setItems(softwareItems);

        ArrayList<Item> hardwareItems = new ArrayList<>();
        hardwareItems.add(Item.builder().name("Mouse").value(false).build());
        hardwareItems.add(Item.builder().name("Keyboard").value(false).build());
        hardwareItems.add(Item.builder().name("Laptop").value(false).build());
        hardwareItems.add(Item.builder().name("Monitor").value(false).build());
        hardwareItems.add(Item.builder().name("Projector").value(false).build());
        hardwareItems.add(Item.builder().name("Cable Wire").value(false).build());
        hardwareItems.add(Item.builder().name("Usb").value(false).build());
        hardwareItems.add(Item.builder().name("Web Cam").value(false).build());
        sp_hardware.setItems(hardwareItems);*/

        //current logged in user id
        currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //Time and Duration
        ArrayList<String> startTimeList = new ArrayList<>();
        startTimeList.add("8:00am");
        startTimeList.add("9:00am");
        startTimeList.add("10:00am");
        startTimeList.add("11:00am");
        startTimeList.add("12:00pm");
        startTimeList.add("13:00pm");
        startTimeList.add("14:00pm");
        startTimeList.add("15:00pm");
        startTimeList.add("16:00pm");
        startTimeList.add("17:00pm");
        ArrayAdapter<String> startAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, startTimeList);
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setAdapter(startAdapter);

        ArrayList<String> endTimeList = new ArrayList<>();
        endTimeList.add("9:00am");
        endTimeList.add("10:00am");
        endTimeList.add("11:00am");
        endTimeList.add("12:00pm");
        endTimeList.add("13:00pm");
        endTimeList.add("14:00pm");
        endTimeList.add("15:00pm");
        endTimeList.add("16:00pm");
        endTimeList.add("17:00pm");
        endTimeList.add("18:00pm");
        ArrayAdapter<String> endAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, endTimeList);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTimeSpinner.setAdapter(endAdapter);

        ArrayList<String> bookingPurposeList = new ArrayList<>();
        bookingPurposeList.add("Exam");
        bookingPurposeList.add("Meeting");


        ArrayAdapter<String> bookingPurposeAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bookingPurposeList);
        bookingPurposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookingPurposeSpinner.setAdapter(bookingPurposeAdapter);


        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent staffIntent = new Intent(BookRoom.this, StaffDashboardActivity.class);
                startActivity(staffIntent);
            }
        });

        Intent intent = getIntent();

        startDurationCalc.put("day", 0);
        startDurationCalc.put("month", 0);
        startDurationCalc.put("year", 0);
        endDurationCalc.put("day", 0);
        endDurationCalc.put("month", 0);
        endDurationCalc.put("year", 0);

        roomID = intent.getStringExtra(RoomsAvailable.ROOM_ID);
        roomCapacity = intent.getStringExtra(RoomsAvailable.ROOM_CAPACITY);
        roomSoftware = intent.getStringExtra(RoomsAvailable.ROOM_HARDWARE);
        roomHardware = intent.getStringExtra(RoomsAvailable.ROOM_SOFTWARE);
        //roomIsAvailable = intent.getStringExtra(RoomsAvailable.ROOM_IS_AVAILABLE);
        block = intent.getStringExtra(RoomsAvailable.ROOM_BLOCK);
        floor = intent.getStringExtra(RoomsAvailable.ROOM_FLOOR);
        url = intent.getStringExtra(RoomsAvailable.ROOM_IMAGE);

        tv_roomNo.setText(roomID);
        tv_roomCapacity.setText(roomCapacity);
        tv_roomHardware.setText(roomHardware);
        tv_roomSoftware.setText(roomSoftware);
        tv_block.setText(block);
        tv_floor.setText(floor);
        startTimeSpinner.setSelection(0);
        endTimeSpinner.setSelection(0);
        bookingPurposeSpinner.setSelection(0);

        startTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                end = endTimeSpinner.getSelectedItem().toString();
                if(end != null)
                {
                    endSplit = end.split(":");
                    start = startTimeSpinner.getSelectedItem().toString();
                    startSplit = start.split(":");
                    startTime = Integer.parseInt(startSplit[0]);
                    endTime = Integer.parseInt(endSplit[0]);
                    durationTime = Integer.parseInt(endSplit[0]) - Integer.parseInt(startSplit[0]);
                    mDuration.setText("" +durationTime);
                }
                else {
                    Toast.makeText(BookRoom.this, "Please select the end time", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        endTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                start = startTimeSpinner.getSelectedItem().toString();
                if(start != null)
                {
                    startSplit = start.split(":");
                    end = endTimeSpinner.getSelectedItem().toString();
                    endSplit = end.split(":");
                    startTime = Integer.parseInt(startSplit[0]);
                    endTime = Integer.parseInt(endSplit[0]);
                    durationTime = Integer.parseInt(endSplit[0]) - Integer.parseInt(startSplit[0]);
                    mDuration.setText("" +durationTime);
                }
                else {
                    Toast.makeText(BookRoom.this, "Please select the start time", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BookRoom.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dateFlag = "start";
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });



        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("BookRoom", "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                int durationHour;
                String months = "";
                if(month<10)
                {
                    months = "0" + month;
                }
                else
                {
                    months = String.valueOf(day);
                }
                String date = months + "-" + day + "-" + year;
                System.out.println("Date : " + date);
                mDate.setText(date);
            }
        };

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomID);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mBookingTableDatabase = FirebaseDatabase.getInstance().getReference("bookings");
        mBookingsDatabase = FirebaseDatabase.getInstance().getReference("users").child(currentId).child("bookings");

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffName = dataSnapshot.child(currentId).child("name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Set bookId to the staff
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Software and hardware details
                /*String hardwares= "", softwares ="";
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

                roomSoftware = softwares;
                roomHardware = hardwares;*/

                /*if(roomHardware.equals("") || roomSoftware.equals(""))
                {
                    Toast.makeText(BookRoom.this, "Select Hardware and software", Toast.LENGTH_LONG).show();
                }*/
               if(mDate.getText().toString().equals(""))
                {
                    Toast.makeText(BookRoom.this, "Select Date", Toast.LENGTH_LONG).show();
                }
                else
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-YYYY HH:mm:ss");
                    Date date = new Date();
                    String currentDate = formatter.format(date);
                    System.out.println("Current date: " + currentDate);
                    System.out.println(currentDate.split(" ")[0]);
                    System.out.println("Test: " +mDate.getText().toString());
                    if(currentDate.split(" ")[0].equals(mDate.getText().toString()))
                    {
                        System.out.println("Inside if of current date time");
                        int currentTime = Integer.parseInt(currentDate.split(" ")[1].split(":")[0]);
                        start = startTimeSpinner.getSelectedItem().toString();
                        startSplit = start.split(":");
                        System.out.println("start split: " + Integer.parseInt(startSplit[0]));
                        System.out.println("current time: " + currentTime);
                        if(Integer.parseInt(startSplit[0]) < currentTime)
                        {
                            Toast.makeText(BookRoom.this, "Please select Start time ahead from current time", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    hours = Integer.parseInt(mDuration.getText().toString());
                    if (hours <= 0) {
                        Toast.makeText(BookRoom.this, "End time should be more than start time", Toast.LENGTH_LONG).show();
                    } else
                    {
                        // String key = mBookingsDatabase.push().getKey();
                        System.out.println("Inside else");
                        mBookingsDatabase.child(roomID).child("booking_id").setValue(roomID);
                        dateBooking = mDate.getText().toString();
                        bookingReference = FirebaseDatabase.getInstance().getReference("bookings").child(roomID).child(dateBooking);


                        //new
                        bookingReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot staffList : dataSnapshot.getChildren()){
                                        checkStaffListDbInformation(staffList.getKey(), dateBooking);
                                    }
                                }
                                else
                                {
                                    bookRoomDbSetData();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void checkStaffListDbInformation(String key, String dateBooking) {
        DatabaseReference StaffKeyRef = FirebaseDatabase.getInstance().getReference("bookings").child(roomID).child(dateBooking).child(key);
        System.out.println("Inside checkStaffListDbInformation");
        //Toast.makeText(this, "Inside checkStaffListDbInformation()", Toast.LENGTH_SHORT).show();
        StaffKeyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Inside onDataChange");
                if(dataSnapshot.exists()) {
                    startTimeDb = Integer.parseInt(dataSnapshot.child("startTime").getValue().toString());
                    System.out.println("startTimeDb: " + startTimeDb);
                    endTimeDb = Integer.parseInt(dataSnapshot.child("endTime").getValue().toString());
                    System.out.println("endTimeDb: " + endTimeDb);
                    if (dataSnapshot.exists() && (startTime >= startTimeDb && startTime < endTimeDb)) {
                        Toast.makeText(BookRoom.this, "Room is already booked from " + startTimeDb + " to " + endTimeDb, Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        bookRoomDbSetData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void bookRoomDbSetData(){
        //Map to get values from Rooms database, Store to strings, push to bookings db, make roomsdb not availble
        HashMap<String,Object> bookingsMap = new HashMap<>();

        bookingsMap.put("roomno",roomID);

        bookingsMap.put("available",true);
        bookingsMap.put("block",block);
        bookingsMap.put("floor",floor);
        bookingsMap.put("hardware",roomHardware);
        bookingsMap.put("software",roomSoftware);
        bookingsMap.put("roomcapacity",roomCapacity);
        bookingsMap.put("staff",staffName);
        bookingsMap.put("staffId",currentId);
        bookingsMap.put("duration",hours);
        bookingsMap.put("roomimage",url);
        bookingsMap.put("startTime", startTime);
        bookingsMap.put("endTime", endTime);
        bookingsMap.put("bookingDate",mDate.getText().toString());
        bookingsMap.put("bookingPurpose", bookingPurposeSpinner.getSelectedItem().toString());
        System.out.println("Testing requested");
        System.out.println("Requested Equipment: " + requestedEquipment.getText().toString());
        if(requestedEquipment.getText().toString().equals(""))
        {
            bookingsMap.put("requestedEquipment", "NA");
        }
        else
        {
            bookingsMap.put("requestedEquipment", requestedEquipment.getText().toString());
        }
        //mRoomsDatabase.child("available").setValue(false);
        //mRoomsDatabase.child("duration").setValue(hours);
        //mRoomsDatabase.child("bookingDate").setValue(mDate.getText().toString());

        //create bookings table with all room information
        bookingReference.child(currentId).updateChildren(bookingsMap);

        Toast.makeText(BookRoom.this, "Room booked successfully", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(BookRoom.this, StaffDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}