package com.infinity_coder.infinity.airtop.data.network.response;

import com.infinity_coder.infinity.airtop.data.db.model.User;

import java.util.ArrayList;

public class SearchUserResponse implements ResponseModel {
    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }
}
