package com.example.infinity.airtop.data.network.request;

import com.google.gson.Gson;

public class PhoneAuthRequest implements RequestModel {
    private final String TYPE = TYPE_PHONE_AUTH;
    private String phoneNumber;

    public PhoneAuthRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toJson() {
        String jsonMessage = new Gson().toJson(this);
        return jsonMessage.length() + "@" + jsonMessage;
    }
}
