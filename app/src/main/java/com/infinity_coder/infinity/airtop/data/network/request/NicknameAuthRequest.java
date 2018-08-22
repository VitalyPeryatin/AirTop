package com.infinity_coder.infinity.airtop.data.network.request;

import java.util.UUID;

public class NicknameAuthRequest extends RequestModel {
    private String phone, uuid, username, nickname, bio;

     public NicknameAuthRequest(String phone, String nickname) {
         uuid = UUID.randomUUID().toString();
         this.phone = phone;
         this.nickname = nickname;
         username = "";
         bio = "";
     }
}
