package com.example.infinity.airtop.model;

public class User {
    private String name, phone;
    private final String TYPE = "user";

    public User(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
