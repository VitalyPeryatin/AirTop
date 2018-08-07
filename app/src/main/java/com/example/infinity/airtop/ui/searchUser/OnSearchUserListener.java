package com.example.infinity.airtop.ui.searchUser;

import com.example.infinity.airtop.data.network.response.SearchUserResponse;

public interface OnSearchUserListener {
    void displaySearchableUsers(SearchUserResponse response);
}
