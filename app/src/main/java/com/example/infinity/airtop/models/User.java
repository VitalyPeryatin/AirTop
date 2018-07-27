package com.example.infinity.airtop.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User implements RequestModel{
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String username;
    public String nickname;
    public String phone;
    public String bio;

    @Ignore
    private final String TYPE = "user";

    public User(String phone) {
        username = "NULL";
        nickname = "Anonym";
        bio = "None";
        this.phone = phone;
    }
}
