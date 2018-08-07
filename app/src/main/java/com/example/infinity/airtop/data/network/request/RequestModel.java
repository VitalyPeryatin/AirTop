package com.example.infinity.airtop.data.network.request;

public interface RequestModel {
    String
            TYPE_PHONE_VERIFY = "verify_user",
            TYPE_MESSAGE = "message",
            TYPE_SEARCH_USER = "searchable_users",
            TYPE_PHONE_AUTH = "phone_auth",
            TYPE_NICKNAME_AUTH = "nickname_auth",
            TYPE_UPDATE_USERNAME = "update_username";

    String toJson();
}
