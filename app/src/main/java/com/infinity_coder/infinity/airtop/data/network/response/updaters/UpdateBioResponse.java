package com.infinity_coder.infinity.airtop.data.network.response.updaters;

import com.infinity_coder.infinity.airtop.data.network.response.ResponseModel;

public class UpdateBioResponse implements ResponseModel {
    private String uuid;
    private String bio;

    public String getUuid() {
        return uuid;
    }

    public String getBio() {
        return bio;
    }
}
