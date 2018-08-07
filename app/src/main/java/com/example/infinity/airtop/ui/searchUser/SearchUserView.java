package com.example.infinity.airtop.ui.searchUser;

import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.network.request.SearchUserRequest;
import com.example.infinity.airtop.data.network.response.SearchUserResponse;

public interface SearchUserView extends MvpView {
    void displaySearchableUsers(SearchUserResponse response);
}
