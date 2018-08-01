package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.network.UserRequest;

@Entity(primaryKeys = {"phone"})
public class User{
    @NonNull
    public String phone;
    public String username;
    public String nickname;
    public String bio;

    public User(@NonNull String phone) {
        username = "NULL";
        nickname = "Anonym";
        bio = "None";
        this.phone = phone;
    }

    public User(UserRequest user){
        username = user.username;
        nickname = user.nickname;
        bio = user.bio;
        this.phone = user.phone;
    }
}
