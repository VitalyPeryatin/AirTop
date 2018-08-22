package com.infinity_coder.infinity.airtop.data.network.request;

public class UpdateBioRequest extends RequestModel {
    private String uuid, bio;

    public UpdateBioRequest(String uuid, String bio) {
        this.uuid = uuid;
        this.bio = bio;
    }
}
