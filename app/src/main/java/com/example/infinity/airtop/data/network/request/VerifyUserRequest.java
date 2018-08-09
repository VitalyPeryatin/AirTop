package com.example.infinity.airtop.data.network.request;

public class VerifyUserRequest extends RequestModel {
    private String uuid;

    public VerifyUserRequest(String id) {
        this.uuid = id;
    }
}
