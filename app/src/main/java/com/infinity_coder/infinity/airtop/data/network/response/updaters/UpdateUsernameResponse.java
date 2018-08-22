package com.infinity_coder.infinity.airtop.data.network.response.updaters;

import com.infinity_coder.infinity.airtop.data.network.response.ResponseModel;

public class UpdateUsernameResponse implements ResponseModel {
    public static String RESULT_OK = "RESULT_OK", RESULT_CANCEL = "RESULT_CANCEL";
    private String result;
    private String uuid;
    private String username;

    public String getResult() {
        return result;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
