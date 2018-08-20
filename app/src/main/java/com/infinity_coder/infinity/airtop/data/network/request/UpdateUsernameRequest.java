package com.infinity_coder.infinity.airtop.data.network.request;

public class UpdateUsernameRequest extends RequestModel{
    private String uuid;
    private String username;
    private String availableToUpdate;

    public UpdateUsernameRequest(String uuid, String username, String availableToUpdate) {
        this.uuid = uuid;
        this.username = username;
        this.availableToUpdate = availableToUpdate;
    }
}
