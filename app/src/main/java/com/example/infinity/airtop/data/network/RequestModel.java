package com.example.infinity.airtop.data.network;

public interface RequestModel {
    String
            TYPE_USER = "user",
            TYPE_CHECK_USERNAME = "checkingUsername",
            TYPE_PHONE_VERIFY = "PhoneVerifier",
            TYPE_MESSAGE = "message",
            TYPE_SEARCH_USER = "searchable_users",
            TYPE_PHONE_AUTH = "phone_auth",
            TYPE_NICKNAME_AUTH = "nickname_auth",
            TYPE_UPDATE_USERNAME = "update_username";
}
