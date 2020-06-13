package com.example.roombookingsystem.activities.admin.rooms;

public class Rooms {

    String roomno;
    String roomcapacity;
    String hardware;
    String software;
    String block;
    String floor;
    boolean available;

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }

    String staffname;
    String staffId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }

    String roomimage;


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

    public Rooms(String roomno, String roomcapacity, String hardware, String software, boolean available, String block, String floor, String roomimage) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
        this.available = available;
        this.roomimage = roomimage;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, boolean available, String block, String floor, String roomimage, String staffname) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
        this.available = available;
        this.roomimage = roomimage;
        this.staffname = staffname;
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

    public Rooms(String roomno, String roomcapacity, String hardware, String software, String block, String floor, String roomimage) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
        this.roomimage = roomimage;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, String block, String floor, String roomimage, String staffname, String staffId) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
        this.roomimage = roomimage;
        this.staffname = staffname;
        this.staffId = staffId;
    }

    public Rooms(String roomno, String roomcapacity, String hardware, String software, String block, String floor, String roomimage, String staffname) {
        this.roomno = roomno;
        this.roomcapacity = roomcapacity;
        this.hardware = hardware;
        this.software = software;
        this.block = block;
        this.floor = floor;
        this.roomimage = roomimage;
        this.staffname = staffname;
    }

}
