package com.example.roombookingsystem.activities.staff;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {

    EditText etName, etPhone, etEmail;
    Button btnSubmit;

    String  name, phone, email;
    private DatabaseReference mUserDatabase;

    private FirebaseAuth firebaseAuth;
    String currentUser;

    public EditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users")
                .child(currentUser);


        etName = view.findViewById(R.id.et_staff_name);
        etPhone = view.findViewById(R.id.et_staff_phone);
        etEmail = view.findViewById(R.id.et_staff_email);
        btnSubmit = view.findViewById(R.id.btn_submit);

        etEmail.setText(firebaseAuth.getCurrentUser().getEmail());


        etEmail.setEnabled(false);
        etEmail.setFocusable(false);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){

                    etName.setText(dataSnapshot.child("name").getValue().toString());
                    etPhone.setText(dataSnapshot.child("phone").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = etName.getText().toString();
                email = etEmail.getText().toString();
                phone = etPhone.getText().toString();

                Map staffInfo = new HashMap();
                staffInfo.put("name", name);
                staffInfo.put("phone",phone);

                mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Staff Information is updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }
}
