package com.example.infinity.airtop.ui.searchUser;

import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.ui.listeners.OnSearchUserListener;

import java.util.ArrayList;

public class SearchUserListener {
    private ArrayList<OnSearchUserListener> searchUserListeners = new ArrayList<>();

    public void subscribe(OnSearchUserListener searchUserListener){
        searchUserListeners.add(searchUserListener);
    }

    public void unsubscribe(OnSearchUserListener searchUserListener){
        searchUserListeners.remove(searchUserListener);
    }

    public void displaySearchableUsers(SearchableUsers searchableUsers){
        for (OnSearchUserListener searchUserListener : searchUserListeners) {
            searchUserListener.displaySearchableUsers(searchableUsers);
        }
    }
}
