package com.example.infinity.airtop.ui.searchUser;

import com.arellomobile.mvp.MvpView;
import com.example.infinity.airtop.data.network.SearchableUsers;

public interface SearchUserView extends MvpView {
    void displaySearchableUsers(SearchableUsers searchableUsers);
}
