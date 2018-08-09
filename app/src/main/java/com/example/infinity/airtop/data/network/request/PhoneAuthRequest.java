package com.example.infinity.airtop.data.network.request;


public class PhoneAuthRequest extends RequestModel {
    private String phoneNumber;

    public PhoneAuthRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
