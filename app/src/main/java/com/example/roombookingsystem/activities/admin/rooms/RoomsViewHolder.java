package com.example.roombookingsystem.activities.admin.rooms;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roombookingsystem.R;

public class RoomsViewHolder extends RecyclerView.ViewHolder {


    public TextView mRoomNo, mRoomCapacity, mHardware, mSoftware;


    public RoomsViewHolder(@NonNull View itemView) {
        super(itemView);

        mRoomNo = itemView.findViewById(R.id.tv_room_no);
        mRoomCapacity = itemView.findViewById(R.id.tv_room_capacity);
        mHardware = itemView.findViewById(R.id.tv_hardware_equipment);
        mSoftware = itemView.findViewById(R.id.tv_software_equipment);
    }




}
