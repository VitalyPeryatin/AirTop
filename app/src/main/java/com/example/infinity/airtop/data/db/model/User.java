package com.example.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.network.response.NicknameAuthResponse;
import com.example.infinity.airtop.data.network.UserRequest;

@Entity(primaryKeys = {"phone", "uuid"})
public class User{
    @NonNull
    public String phone;
    @NonNull
    public String uuid;
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
        phone = user.phone;
        uuid = user.uuid;
    }

    public User(NicknameAuthResponse response){
        username = response.getUsername();
        nickname = response.getNickname();
        bio = response.getBio();
        phone = response.getPhone();
        uuid = response.getUuid();
    }
}
