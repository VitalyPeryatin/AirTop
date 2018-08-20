package com.infinity_coder.infinity.airtop.data.network.request;


public class SearchUserRequest extends RequestModel {
    private String searchUsername;

    public SearchUserRequest(String searchUsername) {
        this.searchUsername = searchUsername;
    }
}
