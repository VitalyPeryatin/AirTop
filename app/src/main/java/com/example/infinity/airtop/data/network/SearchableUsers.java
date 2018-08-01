package com.example.infinity.airtop.data.network;

import java.util.ArrayList;

public class SearchableUsers implements RequestModel{

    private final String TYPE = TYPE_SEARCH_USER;
    public String searchableString;
    public ArrayList<UserRequest> users;

    public String getType() {
        return TYPE;
    }
}
