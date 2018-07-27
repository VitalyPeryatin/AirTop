package com.example.infinity.airtop.models;

import com.example.infinity.airtop.models.RequestModel;
import com.example.infinity.airtop.models.User;

import java.util.ArrayList;

public class SearchableUsers implements RequestModel{

    private final String TYPE = "searchable_users";
    public String searchableString;
    public ArrayList<User> users;

    public String getType() {
        return TYPE;
    }
}
