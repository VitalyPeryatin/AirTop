package com.example.infinity.airtop.models;

public class PhoneVerifier implements RequestModel, RequestType {
    private final String TYPE = TYPE_PHONE_VERIFY;
    public String userPhone;
}
