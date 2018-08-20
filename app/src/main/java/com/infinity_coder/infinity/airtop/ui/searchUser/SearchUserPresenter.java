package com.infinity_coder.infinity.airtop.ui.searchUser;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.infinity_coder.infinity.airtop.App;
import com.infinity_coder.infinity.airtop.data.network.request.SearchUserRequest;
import com.infinity_coder.infinity.airtop.data.network.response.SearchUserResponse;
import com.infinity_coder.infinity.airtop.service.client.ServerConnection;
import com.infinity_coder.infinity.airtop.utils.ServerPostman;

@InjectViewState
public class SearchUserPresenter extends MvpPresenter<SearchUserView> implements OnSearchUserListener{

    private ServerPostman serverPostman;

    public void onCreate(){
        App.getInstance().getResponseListeners().getSearchUserBus().subscribe(this);
        serverPostman = new ServerPostman();
    }

    public void sendSearchableUsername(String username){
        SearchUserRequest request = new SearchUserRequest(username);
        ServerConnection.getInstance().sendRequest(request.toJson());
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
