package com.infinity_coder.infinity.airtop.data.network.response.updaters;

import com.infinity_coder.infinity.airtop.data.network.response.ResponseModel;

public class UpdateNameResponse implements ResponseModel {
    private String uuid;
    private String name;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}
