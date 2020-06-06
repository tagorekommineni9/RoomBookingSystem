package com.example.roombookingsystem.activities.admin.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roombookingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RoomsAdapter extends  RecyclerView.Adapter<RoomsViewHolder>{

    private List<Rooms> RoomList;
    Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public RoomsAdapter(List<Rooms> roomList, Context context) {
        RoomList = roomList;
        this.context = context;
    }


    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View roomLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rooms, null, false);
        RecyclerView.LayoutParams roomLayoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        roomLayoutView.setLayoutParams(roomLayoutParams);
        return new RoomsViewHolder(roomLayoutView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {

        holder.mSoftware.setText(RoomList.get(position).getSoftware());
        holder.mHardware.setText(RoomList.get(position).getHardware());
        holder.mRoomCapacity.setText(RoomList.get(position).getRoomcapacity());
        holder.mRoomNo.setText(RoomList.get(position).getRoomno());
        holder.mBlock.setText(RoomList.get(position).getBlock());
        holder.mFloor.setText(RoomList.get(position).getFloor());

    }

    @Override
    public int getItemCount() {
        return RoomList.size();
    }
}
