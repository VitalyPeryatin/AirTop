package com.infinity_coder.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.infinity_coder.infinity.airtop.data.network.response.NicknameAuthResponse;

@Entity
public class User{
    @NonNull
    @PrimaryKey
    public String uuid;
    public String phone;
    public String username;
    public String nickname;
    public String bio;

    public User(){}

    public User(NicknameAuthResponse response){
        username = response.getUsername();
        nickname = response.getNickname();
        bio = response.getBio();
        phone = response.getPhone();
        uuid = response.getUuid();
    }
}
