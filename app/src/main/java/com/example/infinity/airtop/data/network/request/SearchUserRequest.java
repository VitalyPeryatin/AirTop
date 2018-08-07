package com.example.infinity.airtop.data.network.request;

import com.example.infinity.airtop.data.db.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchUserRequest implements RequestModel {

    private final String TYPE = TYPE_SEARCH_USER;
    private String searchUsername;
    public ArrayList<User> users;

    public void setSearchUsername(String searchUsername) {
        this.searchUsername = searchUsername;
    }

    @Override
    public String toJson(){
        String jsonMessage = new Gson().toJson(this);
        jsonMessage = jsonMessage.length() + "@" + jsonMessage;
        return jsonMessage;
    }
}
