package com.example.infinity.airtop.ui.searchUser;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.network.request.SearchUserRequest;
import com.example.infinity.airtop.data.network.response.SearchUserResponse;
import com.example.infinity.airtop.utils.serverWorker.ServerPostman;

@InjectViewState
public class SearchUserPresenter extends MvpPresenter<SearchUserView> implements OnSearchUserListener{

    private SearchUserRequest searchUserRequest;
    private ServerPostman serverPostman;

    SearchUserPresenter(){
        searchUserRequest = new SearchUserRequest();
    }

    public void onCreate(){
        App.getInstance().getResponseListeners().getSearchUserBus().subscribe(this);
        serverPostman = new ServerPostman();
    }

    public void sendSearchableUsername(String username){
        searchUserRequest.setSearchUsername(username);
        serverPostman.postRequest(searchUserRequest);
    }

    public void displaySearchableUsers(SearchUserResponse response){
        getViewState().displaySearchableUsers(response);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getSearchUserBus().unsubscribe(this);
    }
}
