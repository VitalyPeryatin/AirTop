package com.infinity_coder.infinity.airtop.ui.searchUser;

import com.infinity_coder.infinity.airtop.data.network.response.SearchUserResponse;

public interface OnSearchUserListener {
    void displaySearchableUsers(SearchUserResponse response);
}
