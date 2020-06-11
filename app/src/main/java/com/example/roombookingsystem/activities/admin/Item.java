package com.example.roombookingsystem.activities.admin;

public class Item {

    private String name;
    private Boolean value;

    public Item() {
    }

    public Item(String name, Boolean value) {
        this.name = name;
        this.value = value;
    }

    //Builder Initialitation
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}