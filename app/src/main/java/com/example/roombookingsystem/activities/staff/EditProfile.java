package com.example.roombookingsystem.activities.staff;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roombookingsystem.R;
import com.example.roombookingsystem.activities.RegistrationActivity;
import com.example.roombookingsystem.activities.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {

    EditText etName, etPhone, etEmail, etPassword;
    Button btnSubmit;
    CheckBox chkbChangePassword;

    String  name, phone, email, password;
    private DatabaseReference mUserDatabase;

    private FirebaseAuth firebaseAuth;
    String currentUser;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,10}" +               //at least 4 characters and maximum of 10 characters
            "$");

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
        etPassword = view.findViewById(R.id.et_staff_password);
        btnSubmit = view.findViewById(R.id.btn_submit);
        chkbChangePassword = view.findViewById(R.id.chkbox_change_password);

        etEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        // Checkbox will be checked by default(which enables staff to change password).
        // If user don't want to change the password, then they can uncheck the box(which will disable the password field). The colour of the edittext field will be change to RED colour.

        chkbChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((chkbChangePassword.isChecked())){
                    etPassword.setEnabled(true);
                    etPassword.setFocusable(true);
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etPassword.setHint("Enter new password");
                    etPassword.getBackground().setColorFilter(Color.rgb(255, 233, 192), PorterDuff.Mode.SRC_ATOP);
                }
                else{
                    etPassword.setEnabled(false);
                    etPassword.setFocusable(false);
                    etPassword.setInputType(InputType.TYPE_NULL);
                    etPassword.setHint("password change disabled");
                    //etPassword.setText("Password change disabled");
                    //etPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    etPassword.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
            }
        });



        //   etEmail.setEnabled(false);
        // etEmail.setFocusable(false);

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
                password = etPassword.getText().toString();

                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("Enter your Name");
                } else if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    etPhone.setError("Enter your Phone number");
                } else if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    etEmail.setError("Enter your Email");

                    //Email pattern check
                }  else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    etEmail.setError("Please enter a valid email address");
                }
                if(password.equals("")){
                    password="Empty@007";
                }
                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    etPassword.setError("Password too weak! please include atleast 1 number, 1 uppercase letter, 1 lowercase letter, 1 special character(@#$%^&+=)");
                }
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),password);

                // If user enter different email ID and password is not empty

                if(!(user.getEmail().equals(email))){

                    user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if(!(password.equals("Empty@007"))){
                                                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Map staffInfo = new HashMap();
                                                            staffInfo.put("name", name);
                                                            staffInfo.put("phone", phone);
                                                            staffInfo.put("email", email);

                                                            mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                                                                @Override
                                                                public void onComplete(@NonNull Task task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getContext(), StaffDashboardActivity.class);
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });

                                                            Toast.makeText(getContext(), "Please check your email & verify your updated email address", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            // If user enter different email ID, but the password field is empty
                                            else{
                                                Map staffInfo = new HashMap();
                                                staffInfo.put("name", name);
                                                staffInfo.put("phone", phone);
                                                staffInfo.put("email", email);

                                                mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getContext(), StaffDashboardActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                                Toast.makeText(getContext(), "Please check your email & verify your updated email address", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                }

                //If user keep the same email ID, but changed the password & password is not empty

                else {
                    user.reauthenticateAndRetrieveData(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!(task.isSuccessful())) {
                                if(!(password.equals("Empty@007"))){
                                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map staffInfo = new HashMap();
                                                staffInfo.put("name", name);
                                                staffInfo.put("phone", phone);
                                                mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getContext(), StaffDashboardActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                //If user keep the same email ID, but changed the password & password is empty
                                else{
                                    Map staffInfo = new HashMap();
                                    staffInfo.put("name", name);
                                    staffInfo.put("phone", phone);
                                    mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getContext(), StaffDashboardActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            }

                            // If user keep the same email ID and same password, but changed the remaining fields
                            else{
                                Map staffInfo = new HashMap();
                                staffInfo.put("name", name);
                                staffInfo.put("phone",phone);

                                mUserDatabase.updateChildren(staffInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getContext(), "Your profile is updated successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), StaffDashboardActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }
}
