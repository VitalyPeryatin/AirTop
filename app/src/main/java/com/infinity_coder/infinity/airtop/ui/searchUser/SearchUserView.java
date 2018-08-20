package com.infinity_coder.infinity.airtop.ui.searchUser;

import com.arellomobile.mvp.MvpView;
import com.infinity_coder.infinity.airtop.data.network.response.SearchUserResponse;

public interface SearchUserView extends MvpView {
    void displaySearchableUsers(SearchUserResponse response);
}
