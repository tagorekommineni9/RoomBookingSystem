package com.example.roombookingsystem.activities.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.RegistrationActivity;
import com.example.roombookingsystem.activities.UserLoginActivity;
import com.example.roombookingsystem.activities.admin.rooms.RoomsAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookRoom extends AppCompatActivity {

    private DatabaseReference mRoomsDatabase;
    MaterialToolbar toolbar;
    TextView tv_roomNo, tv_roomCapacity, tv_roomHardware, tv_roomSoftware, tv_block, tv_floor;
    Button btn_book, btn_duration;
    String roomID, roomCapacity, roomSoftware, roomHardware, roomIsAvailable, block, floor;
    String dateFlag = "";
    Spinner startTimeSpinner, endTimeSpinner;
    private TextView mDate, mDuration;
    private EditText end_error, start_error;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Map<String, Integer> startDurationCalc = new HashMap<>();
    private Map<String, Integer> endDurationCalc = new HashMap<>();
    private String start, end;
    private String[] startSplit, endSplit;
    private int durationTime;


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
        btn_duration = findViewById(R.id.btn_duration);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        roomIsAvailable = intent.getStringExtra(RoomsAvailable.ROOM_IS_AVAILABLE);
        block = intent.getStringExtra(RoomsAvailable.ROOM_BLOCK);
        floor = intent.getStringExtra(RoomsAvailable.ROOM_FLOOR);

        tv_roomNo.setText(roomID);
        tv_roomCapacity.setText(roomCapacity);
        tv_roomHardware.setText(roomHardware);
        tv_roomSoftware.setText(roomSoftware);
        tv_block.setText(block);
        tv_floor.setText(floor);
        startTimeSpinner.setSelection(0);
        endTimeSpinner.setSelection(0);

        btn_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start = startTimeSpinner.getSelectedItem().toString();
                startSplit = start.split(":");
                end = endTimeSpinner.getSelectedItem().toString();
                endSplit = end.split(":");
                durationTime = Integer.parseInt(endSplit[0]) - Integer.parseInt(startSplit[0]);
                mDuration.setText("" +durationTime);
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
                String date = month + "/" + day + "/" + year;
                mDate.setText(date);
            }
        };

        mRoomsDatabase = FirebaseDatabase.getInstance().getReference("rooms").child(roomID);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDuration.getText().toString().equals(""))
                {
                    Toast.makeText(BookRoom.this, "Click duration button to confirm no of hours", Toast.LENGTH_LONG).show();
                }
                else {
                    int hours = Integer.parseInt(mDuration.getText().toString());
                    if (hours <= 0) {
                        Toast.makeText(BookRoom.this, "End time should be more than start time", Toast.LENGTH_LONG).show();
                    } else {
                        mRoomsDatabase.child("available").setValue(false);
                        mRoomsDatabase.child("duration").setValue(hours);
                        mRoomsDatabase.child("bookingDate").setValue(mDate.getText().toString());
                        Toast.makeText(BookRoom.this, "Room booked successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(BookRoom.this, StaffDashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}