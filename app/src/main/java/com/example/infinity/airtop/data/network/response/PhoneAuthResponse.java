package com.example.infinity.airtop.data.network.response;

import com.example.infinity.airtop.data.network.UserRequest;

public class PhoneAuthResponse {
    private String TYPE;
    private String result;
    private UserRequest userRequest;

    public String getResult() {
        return result;
    }

    public UserRequest getUserRequest() {
        return userRequest;
    }
}
