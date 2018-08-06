package com.example.infinity.airtop.data.network.request;

import android.support.annotation.NonNull;

import com.example.infinity.airtop.data.db.model.User;
import com.example.infinity.airtop.data.network.RequestModel;

import java.util.UUID;

public class NicknameAuthRequest implements RequestModel {
    @NonNull
    public String phone;
    private String uuid;
    private String username;
    private String nickname;
    private String bio;

     private final String TYPE = TYPE_NICKNAME_AUTH;

     public NicknameAuthRequest(String phone, String nickname) {
         uuid = UUID.randomUUID().toString();
         this.phone = phone;
         this.nickname = nickname;
         username = "NULL";
         bio = "None";
     }
}
