package com.example.infinity.airtop.data.network.request;

import com.google.gson.Gson;

public abstract class RequestModel {
    protected final String TYPE = getClass().getSimpleName();

    public String toJson() {
        String jsonMessage = new Gson().toJson(this);
        return jsonMessage.length() + "@" + jsonMessage;
    }
}
