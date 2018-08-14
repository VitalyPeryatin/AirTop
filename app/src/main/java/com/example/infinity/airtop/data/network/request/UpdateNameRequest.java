package com.example.infinity.airtop.data.network.request;

public class UpdateNameRequest extends RequestModel {
    private String uuid;
    private String name;

    public UpdateNameRequest(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
