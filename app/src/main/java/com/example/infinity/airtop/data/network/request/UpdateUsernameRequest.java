package com.example.infinity.airtop.data.network.request;

public class UpdateUsernameRequest extends RequestModel{
    private String phone;
    private String username;
    private String availableToUpdate;

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
