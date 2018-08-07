package com.example.infinity.airtop.data.network.request;

import com.example.infinity.airtop.data.network.request.RequestModel;
import com.google.gson.Gson;

public class VerifyUserRequest implements RequestModel {
    private final String TYPE = TYPE_PHONE_VERIFY;
    private String uuid;

    public VerifyUserRequest(String id) {
        this.uuid = id;
    }

    @Override
    public String toJson(){
        String jsonMessage = new Gson().toJson(this);
        return jsonMessage.length() + "@" + jsonMessage;
    }
}
