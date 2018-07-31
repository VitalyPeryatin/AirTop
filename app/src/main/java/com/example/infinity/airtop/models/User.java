package com.example.infinity.airtop.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NonNls;

@Entity(primaryKeys = {"phone"})
public class User implements RequestModel, RequestType{
    @NonNull
    public String phone;
    public String username;
    public String nickname;
    public String bio;

    @Ignore
    private String action;
    public static final String
            ACTION_CREATE = "create",
            ACTION_UPDATE = "update";
    @Ignore
    private final String TYPE = TYPE_USER;

    public User(String phone) {
        username = "NULL";
        nickname = "Anonym";
        bio = "None";
        action = ACTION_CREATE;
        this.phone = phone;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if(!action.equals(ACTION_CREATE) && !action.equals(ACTION_UPDATE))
            action = ACTION_CREATE;
        this.action = action;
    }
}
