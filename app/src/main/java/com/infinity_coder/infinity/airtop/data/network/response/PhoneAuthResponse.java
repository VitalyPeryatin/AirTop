package com.infinity_coder.infinity.airtop.data.network.response;

import com.infinity_coder.infinity.airtop.data.db.model.User;

public class PhoneAuthResponse implements ResponseModel {
    private String TYPE;
    private String result;
    private User user;

    public PhoneAuthResponse(String result, User user) {
        this.result = result;
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public User getUser() {
        return user;
    }
}
