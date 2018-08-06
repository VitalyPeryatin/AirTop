package com.example.infinity.airtop.data.network.request;

import com.example.infinity.airtop.data.network.RequestModel;

public class UpdateUsernameRequest implements RequestModel{
    private String phone;
    private String username;
    private String availableToUpdate;
    private final String TYPE = TYPE_UPDATE_USERNAME;

    public UpdateUsernameRequest(String phone, String username, String availableToUpdate) {
        this.phone = phone;
        this.username = username;
        this.availableToUpdate = availableToUpdate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
