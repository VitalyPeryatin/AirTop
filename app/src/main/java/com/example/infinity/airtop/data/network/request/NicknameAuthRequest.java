package com.example.infinity.airtop.data.network.request;

import com.google.gson.Gson;

import java.util.UUID;

public class NicknameAuthRequest implements RequestModel {
    private String phone, uuid, username, nickname, bio;

     private final String TYPE = TYPE_NICKNAME_AUTH;

     public NicknameAuthRequest(String phone, String nickname) {
         uuid = UUID.randomUUID().toString();
         this.phone = phone;
         this.nickname = nickname;
         username = "NULL";
         bio = "None";
     }

    @Override
    public String toJson() {
        String jsonMessage = new Gson().toJson(this);
        return jsonMessage.length() + "@" + jsonMessage;
    }
}
