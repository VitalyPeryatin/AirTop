package com.infinity_coder.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Addressee {
    public String phone;
    @NonNull
    @PrimaryKey
    public String uuid;
    public String username;
    public String nickname;
    public String bio;

    public Addressee(){}

    @Ignore
    public Addressee(String uuid){
        this.uuid = uuid;
        nickname = "Anonymous";
    }

    @Ignore
    public Addressee(User user) {
        uuid = user.uuid;
        phone = user.phone;
        username = user.username;
        nickname = user.nickname;
        bio = user.bio;
    }
}
