package com.example.infinity.airtop.ui.searchUser;

import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.infinity.airtop.App;
import com.example.infinity.airtop.data.network.SearchableUsers;
import com.example.infinity.airtop.service.ClientService;
import com.example.infinity.airtop.utils.JsonConverter;
import com.example.infinity.airtop.ui.listeners.OnSearchUserListener;

@InjectViewState
public class SearchUserPresenter extends MvpPresenter<SearchUserView> implements OnSearchUserListener{

    private SearchableUsers searchableUsers;
    private Context context;
    SearchUserPresenter(){
        searchableUsers = new SearchableUsers();
    }

    public void onCreate(Context context){
        this.context = context;
        App.getInstance().getResponseListeners().getSearchUserListener().subscribe(this);
    }

    public void sendSearchableString(String str){
        searchableUsers.searchableString = str;
        JsonConverter jsonConverter = new JsonConverter();
        String json = jsonConverter.toJson(searchableUsers);
        Intent intent = new Intent(context, ClientService.class);
        intent.putExtra("request", json);
        context.startService(intent);
    }

    public void displaySearchableUsers(SearchableUsers searchableUsers){
        getViewState().displaySearchableUsers(searchableUsers);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().getResponseListeners().getSearchUserListener().unsubscribe(this);
    }
}
