package com.example.roombookingsystem.activities.admin.rooms;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roombookingsystem.R;

public class RoomsViewHolder extends RecyclerView.ViewHolder {


    public TextView mRoomNo, mRoomCapacity, mHardware, mSoftware, mBlock, mFloor;
    Spinner mAvailable;
    ImageView mRoomImage;


    public RoomsViewHolder(@NonNull View itemView, final RoomsAdapter.OnItemClickListener listener) {
        super(itemView);

        mRoomNo = itemView.findViewById(R.id.tv_room_no);
        mRoomCapacity = itemView.findViewById(R.id.tv_room_capacity);
        mHardware = itemView.findViewById(R.id.tv_hardware_equipment);
        mSoftware = itemView.findViewById(R.id.tv_software_equipment);
        mBlock = itemView.findViewById(R.id.tv_block);
        mFloor = itemView.findViewById(R.id.tv_floor);
        mRoomImage = itemView.findViewById(R.id.image_btn);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }




}
