package com.infinity_coder.infinity.airtop.data.network.request;


public class PhoneAuthRequest extends RequestModel {
    private String phone;

    public PhoneAuthRequest(String phoneNumber) {
        this.phone = phoneNumber;
    }
}
