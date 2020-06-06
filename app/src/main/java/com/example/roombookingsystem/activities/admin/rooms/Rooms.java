package com.example.roombookingsystem.activities.admin.rooms;

public class Rooms {

    String roomno;
    String roomcapacity;
    String hardware;
    String software;
    String block;
    String floor;
    boolean available;


    public String getRoomcapacity() {
        return roomcapacity;
    }

    public void setRoomcapacity(String roomcapacity) {
        this.roomcapacity = roomcapacity;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getBlock() { return block; }

    public void setBlock(String block) { this.block = block; }

    public String getFloor() { return floor; }

    public void setFloor(String floor) { this.floor = floor; }

    public Rooms(String roomno, String roomcapacity, String hardware, String software) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, boolean available) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.available = available;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, boolean available, String block, String floor) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.available = available;
        this.block = block;
        this.floor = floor;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, String block, String floor) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
    }

}
