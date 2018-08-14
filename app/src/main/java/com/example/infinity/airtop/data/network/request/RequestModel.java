package com.example.infinity.airtop.data.network.request;

import com.example.infinity.airtop.App;
import com.google.gson.Gson;

public abstract class RequestModel {
    protected final String TYPE = getClass().getSimpleName();
    protected String exchangeUUID;

    public RequestModel(){
        if(App.getInstance().getCurrentUser() != null)
            exchangeUUID = App.getInstance().getCurrentUser().uuid;
    }

    public String toJson() {
        String jsonMessage = new Gson().toJson(this);
        return jsonMessage.length() + "@" + jsonMessage;
    }
}
