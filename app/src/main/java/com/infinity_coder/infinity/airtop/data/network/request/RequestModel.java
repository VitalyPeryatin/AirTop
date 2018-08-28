package com.infinity_coder.infinity.airtop.data.network.request;

import com.infinity_coder.infinity.airtop.App;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public abstract class RequestModel {
    protected final String TYPE = getClass().getSimpleName();
    protected String exchangeUUID;

    public RequestModel(){
        if(App.getInstance().getCurrentUser() != null)
            exchangeUUID = App.getInstance().getCurrentUser().uuid;
    }

    public String toJson() {
        String jsonMessage = new Gson().toJson(this);
        try {
            return jsonMessage.getBytes("UTF-8").length + "@" + jsonMessage;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
