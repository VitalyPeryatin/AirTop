package com.example.infinity.airtop.data.network.response;

import android.support.annotation.NonNull;

import java.util.UUID;

import static com.example.infinity.airtop.data.network.RequestModel.TYPE_NICKNAME_AUTH;

public class NicknameAuthResponse {
    private String phone;
    private String uuid;
    private String username;
    private String nickname;
    private String bio;
    private String TYPE;

    public String getPhone() {
        return phone;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBio() {
        return bio;
    }
}
