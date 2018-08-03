package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.network.UserRequest;

@Entity
public class Addressee {
    @NonNull
    @PrimaryKey
    public String phone;
    public String username;
    public String nickname;
    public String bio;

    public Addressee(){}

    public Addressee(UserRequest user) {
        phone = user.phone;
        username = user.username;
        nickname = user.nickname;
        bio = user.bio;
    }
}
