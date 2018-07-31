package com.example.infinity.airtop.presentation.presenters;

import com.example.infinity.airtop.models.SearchableUsers;
import com.example.infinity.airtop.views.App;
import com.example.infinity.airtop.views.SearchUserActivity;

public class SearchUserPresenter implements Presenter<SearchUserActivity> {

    private static SearchUserPresenter presenter;
    private SearchUserActivity activity;
    private SearchableUsers searchableUsers;

    private SearchUserPresenter(){
        searchableUsers = new SearchableUsers();
    }

    public static synchronized SearchUserPresenter getInstance(){
        if(presenter == null) presenter = new SearchUserPresenter();
        return presenter;
    }

    @Override
    public void attachActivity(SearchUserActivity activity) {
        this.activity = activity;
    }

    @Override
    public void detachActivity() {
        this.activity = null;
    }

    public void sendSearchableString(String str){
        searchableUsers.searchableString = str;
        App.getInstance().getBackendClient().sendRequest(searchableUsers);
    }

    public void displaySearchableUsers(SearchableUsers searchableUsers){
        activity.runOnUiThread(()->activity.getSearchAdapter().updateList(searchableUsers.users));
    }
}
