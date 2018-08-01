package com.example.infinity.airtop.data.network;

import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.db.model.User;

public class UserRequest implements RequestModel{
    @NonNull
    public String phone;
    public String username;
    public String nickname;
    public String bio;

    private String action;
    public static final String
            ACTION_CREATE = "create",
            ACTION_UPDATE = "update";
    private final String TYPE = TYPE_USER;

    public UserRequest(String phone) {
        username = "NULL";
        nickname = "Anonym";
        bio = "None";
        action = ACTION_CREATE;
        this.phone = phone;
    }

    public UserRequest(User user) {
        username = user.username;
        nickname = user.nickname;
        bio = user.bio;
        phone = user.phone;
        action = ACTION_CREATE;
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
