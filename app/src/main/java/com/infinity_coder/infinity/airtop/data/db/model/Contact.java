package com.infinity_coder.infinity.airtop.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

@Entity
public class Contact {
    public String phone;
    @NonNull
    @PrimaryKey
    public String uuid;
    public String username;
    public String nickname;
    public String bio;
    public String lastMessage;

    public Contact(){}

    @Ignore
    public Contact(@NonNull String uuid, @NonNull String nickname){
        this.uuid = uuid;
        this.nickname = nickname;
    }

    @Ignore
    public Contact(User user) {
        uuid = user.uuid;
        phone = user.phone;
        username = user.username;
        nickname = user.nickname;
        bio = user.bio;
    }
}
