package com.example.roombookingsystem.activities.admin;

public class ItemBuilder {

    public String name;
    public Boolean value;

    public ItemBuilder() {
    }

    public ItemBuilder(Item item) {
        this.name = item.getName();
        this.value = item.getValue();
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder value(Boolean value) {
        this.value = value;
        return this;
    }

    public Item build() {
        Item item = new Item();
        item.setName(name);
        item.setValue(value);
        return item;
    }
}
