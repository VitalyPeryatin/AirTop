package com.infinity_coder.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.infinity_coder.infinity.airtop.data.network.response.NicknameAuthResponse;

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

    public User(){

    }

    public User(@NonNull String uuid, String nickname, String username, @NonNull String phone) {
        this.phone = phone;
        this.uuid = uuid;
        this.username = username;
        this.nickname = nickname;
    }

    public User(NicknameAuthResponse response){
        username = response.getUsername();
        nickname = response.getNickname();
        bio = response.getBio();
        phone = response.getPhone();
        uuid = response.getUuid();
    }
}
