package com.example.roombookingsystem.activities.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.RegistrationActivity;
import com.example.roombookingsystem.activities.UserLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    //Add class Image
    Uri RoomResultUri;
    ImageView classRoomImage;
    String classImageUrl;


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
        classRoomImage = view.findViewById(R.id.add_image_btn);

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
        sp_hardware.setItems(hardwareItems);

        classRoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

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

                                final Map roomInfo = new HashMap<>();

                                roomInfo.put("roomno", roomNo);
                                roomInfo.put("roomcapacity", roomCapacity);
                                roomInfo.put("software", roomSoftware);
                                roomInfo.put("hardware", roomHardware);
                                roomInfo.put("available", available);
                                roomInfo.put("block", block);
                                roomInfo.put("floor", floor);

                                //add Image to database
                                if (RoomResultUri != null) {

                                    StorageReference filepath = FirebaseStorage.getInstance().getReference().child("class_images").child(roomNo);

                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(), RoomResultUri);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                                    byte[] data = baos.toByteArray();

                                    //uploading the image
                                    UploadTask uploadTask = filepath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            getActivity().finish();
                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null) {
                                                if (taskSnapshot.getMetadata().getReference() != null) {
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            classImageUrl = uri.toString();
                                                            roomInfo.put("roomimage", classImageUrl);
                                                            RoomDb.updateChildren(roomInfo);
                                                        }
                                                    });

                                                }
                                            }

                                        }
                                    });

                                } else {
                                    classImageUrl = "default";
                                    roomInfo.put("roomimage", classImageUrl);
                                    RoomDb.updateChildren(roomInfo);
                                }
                                Toast.makeText(getContext(), "New Class room is added", Toast.LENGTH_SHORT).show();
                                goToDashboard();

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

    private void goToDashboard() {
        Intent staffIntent = new Intent(getContext(), AdminDashboardActivity.class);
        staffIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(staffIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the intent from activity code is 1
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            final Uri imageUri = data.getData();
            RoomResultUri = imageUri;
            classRoomImage.setImageURI(RoomResultUri);

        }

    }
}
