package com.example.infinity.airtop.data.network.request;

import com.example.infinity.airtop.data.network.RequestModel;

public class PhoneAuthRequest implements RequestModel {
    private final String TYPE = TYPE_PHONE_AUTH;
    private String phoneNumber;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
