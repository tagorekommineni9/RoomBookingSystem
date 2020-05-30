package com.example.roombookingsystem.activities.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roombookingsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAvailableRooms extends Fragment {

    public AdminAvailableRooms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_available_rooms, container, false);
    }
}
